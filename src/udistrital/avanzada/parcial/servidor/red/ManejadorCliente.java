package udistrital.avanzada.parcial.servidor.red;

import udistrital.avanzada.parcial.cliente.modelo.dao.IUsuarioDAO;
import udistrital.avanzada.parcial.cliente.modelo.dao.UsuarioDAO;
import udistrital.avanzada.parcial.mensajes.*;
import udistrital.avanzada.parcial.servidor.control.*;
import udistrital.avanzada.parcial.servidor.modelo.*;
import udistrital.avanzada.parcial.servidor.servicios.AutenticacionService;
import udistrital.avanzada.parcial.servidor.servicios.IAutenticacionService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejador de comunicación con un cliente específico.
 *
 * <p>
 * Esta clase se ejecuta en un hilo independiente y gestiona toda la
 * comunicación por {@link Socket} con un cliente determinado. Se encarga de:
 * </p>
 * <ul>
 * <li>Procesar la autenticación del usuario.</li>
 * <li>Iniciar y coordinar la sesión de juego.</li>
 * <li>Enviar y recibir objetos serializados entre cliente y servidor.</li>
 * <li>Administrar la finalización y cierre seguro de la conexión.</li>
 * </ul>
 *
 * <p>
 * Cumple el principio de responsabilidad única (SRP) dentro de la arquitectura
 * del servidor, delegando la lógica de autenticación y del juego a los
 * controladores especializados.
 * </p>
 *
 * @author Paula Martínez
 * @author Juan Estevan Ariza Ortiz
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-11
 */
public class ManejadorCliente implements IManejadorCliente {

    // -------------------------------------------------------------
    // Atributos
    // -------------------------------------------------------------
    /**
     * Socket de conexión con el cliente remoto.
     */
    private final Socket socket;

    /**
     * Controlador responsable del proceso de autenticación del usuario.
     */
    private AutenticacionController autenticacionController;

    /**
     * Flujo de salida para enviar objetos al cliente.
     */
    private ObjectOutputStream out;

    /**
     * Flujo de entrada para recibir objetos del cliente.
     */
    private ObjectInputStream in;

    /**
     * Nombre del jugador autenticado en esta sesión.
     */
    private String nombreJugador;

    // -------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------
    /**
     * Crea un nuevo manejador de cliente asociado a un socket específico.
     *
     * @param socket conexión activa con el cliente.
     */
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    // -------------------------------------------------------------
    // Ejecución principal
    // -------------------------------------------------------------
    /**
     * Inicia el ciclo de vida del manejador.
     *
     * <p>
     * Establece los flujos de entrada y salida, inicializa las dependencias
     * necesarias, gestiona la autenticación y, si es exitosa, comienza la
     * sesión de juego.
     * </p>
     */
    @Override
    public void run() {
        try {
            inicializarDependencias();

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Conexión establecida con: " + socket.getInetAddress());

            if (procesarAutenticacion()) {
                iniciarSesionJuego();
            }

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            enviarRespuestaError("Error del servidor al conectar con la base de datos");
        } catch (IOException e) {
            System.err.println("Error en comunicación con cliente: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error al deserializar objeto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    // -------------------------------------------------------------
    // Métodos auxiliares
    // -------------------------------------------------------------
    /**
     * Inicializa los componentes requeridos para la autenticación del usuario.
     *
     * @throws SQLException si ocurre un error al obtener la conexión con la
     * base de datos.
     */
    private void inicializarDependencias() throws SQLException {
        IUsuarioDAO usuarioDAO = new UsuarioDAO();
        IAutenticacionService autenticacionService = new AutenticacionService(usuarioDAO);
        this.autenticacionController = new AutenticacionController(autenticacionService);
    }

    /**
     * Procesa la autenticación del cliente.
     *
     * <p>
     * Recibe una {@link SolicitudAutenticacion}, la valida a través del
     * {@link AutenticacionController} y envía una
     * {@link RespuestaAutenticacion} al cliente.
     * </p>
     *
     * @return {@code true} si la autenticación fue exitosa; {@code false} en
     * caso contrario.
     * @throws IOException si ocurre un error de comunicación.
     * @throws ClassNotFoundException si el objeto recibido no puede
     * deserializarse.
     */
    private boolean procesarAutenticacion() throws IOException, ClassNotFoundException {
        Object solicitudObj = in.readObject();

        if (!(solicitudObj instanceof SolicitudAutenticacion)) {
            System.err.println("Objeto recibido no es una SolicitudAutenticacion");
            enviarRespuestaError("Tipo de solicitud no válido");
            return false;
        }

        SolicitudAutenticacion solicitud = (SolicitudAutenticacion) solicitudObj;
        this.nombreJugador = solicitud.getUsuario();

        RespuestaAutenticacion respuesta = autenticacionController.procesarAutenticacion(solicitud);

        out.writeObject(respuesta);
        out.flush();

        return respuesta.isExitosa();
    }

    /**
     * Inicia la sesión de juego una vez autenticado el usuario.
     *
     * <p>
     * Este método mantiene un ciclo de lectura de comandos del cliente, procesa
     * los movimientos, actualiza el estado del juego y envía respuestas al
     * cliente en tiempo real.
     * </p>
     *
     * @throws IOException si ocurre un error de E/S durante la sesión.
     * @throws ClassNotFoundException si se recibe un objeto no reconocido.
     */
    private void iniciarSesionJuego() throws IOException, ClassNotFoundException {
        System.out.println("===========================================");
        System.out.println("Sesión de juego iniciada para: " + nombreJugador);
        System.out.println("===========================================\n");

        InicializadorJuego inicializador = new InicializadorJuego();
        InicializadorJuego.ComponentesJuego componentes = inicializador.inicializar();

        ControlJuego controlJuego = componentes.getControlJuego();
        EstadoJuego estado = componentes.getEstado();
        ControlInterfazServidor controlInterfaz = componentes.getControlInterfaz();
        var servicioTiempo = componentes.getServicioTiempo();

        List<String> frutasComidas = new ArrayList<>();
        boolean juegoActivo = true;
        int turno = 0;

        System.out.println("Esperando comandos del cliente...\n");

        while (juegoActivo) {
            try {
                Object comandoObj = in.readObject();

                if (!(comandoObj instanceof ComandoMovimiento)) {
                    System.err.println("Objeto recibido no es un ComandoMovimiento");
                    continue;
                }

                ComandoMovimiento comando = (ComandoMovimiento) comandoObj;
                turno++;

                System.out.println("--- Turno " + turno + " ---");
                System.out.println("Comando recibido: " + comando.getDireccion());

                Direccion direccion = Direccion.desdeTexto(comando.getDireccion());

                // Registrar frutas antes del movimiento
                int frutasComidasAntes = (int) estado.getFrutas().stream()
                        .filter(Fruta::isComida)
                        .count();

                ResultadoMovimiento resultado = controlJuego.procesarComando(direccion);

                // Detectar frutas comidas
                if (resultado.getFrutasComidas() > 0) {
                    for (Fruta f : estado.getFrutas()) {
                        if (f.isComida() && !frutasComidas.contains(f.getTipo().name())) {
                            frutasComidas.add(f.getTipo().name());
                        }
                    }
                }

                long tiempoMs = servicioTiempo.milisegundosTranscurridos();
                controlInterfaz.actualizarHUD(estado.getPuntaje(), tiempoMs);

                Pacman pac = estado.getPacman();
                RespuestaMovimiento respuesta = new RespuestaMovimiento(
                        pac.getPosicion().getX(),
                        pac.getPosicion().getY(),
                        estado.getPuntaje(),
                        resultado.isChocoConPared(),
                        resultado.getFrutasComidas() > 0,
                        resultado.getPuntosGanados(),
                        controlJuego.juegoTerminado(),
                        controlJuego.getFrutasRestantes()
                );

                out.writeObject(respuesta);
                out.flush();

                System.out.println("Respuesta enviada: " + respuesta);

                // Finalización del juego
                if (respuesta.isJuegoTerminado()) {
                    servicioTiempo.detener();
                    long tiempoFinal = servicioTiempo.milisegundosTranscurridos();

                    RespuestaFinal respuestaFinal = new RespuestaFinal(
                            nombreJugador,
                            estado.getPuntaje(),
                            tiempoFinal,
                            frutasComidas
                    );

                    out.writeObject(respuestaFinal);
                    out.flush();

                    System.out.println("\n===========================================");
                    System.out.println("¡JUEGO TERMINADO!");
                    System.out.println("Jugador: " + nombreJugador);
                    System.out.println("Puntaje final: " + estado.getPuntaje());
                    System.out.println("Tiempo total: " + formatearTiempo(tiempoFinal));
                    System.out.println("Frutas comidas: " + frutasComidas);
                    System.out.println("===========================================\n");

                    juegoActivo = false;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error durante el juego: " + e.getMessage());
                juegoActivo = false;
            }
        }

        System.out.println("Sesión de juego finalizada para: " + nombreJugador);
    }

    /**
     * Convierte el tiempo en milisegundos a un formato legible (MM:SS.mmm).
     *
     * @param ms tiempo en milisegundos.
     * @return cadena con el formato de tiempo.
     */
    private String formatearTiempo(long ms) {
        long min = ms / 60000;
        long sec = (ms % 60000) / 1000;
        long mil = ms % 1000;
        return String.format("%02d:%02d.%03d", min, sec, mil);
    }

    /**
     * Envía una respuesta de error al cliente.
     *
     * @param mensaje texto descriptivo del error.
     */
    private void enviarRespuestaError(String mensaje) {
        try {
            RespuestaAutenticacion respuesta = new RespuestaAutenticacion(false, mensaje);
            out.writeObject(respuesta);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error al enviar respuesta de error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // Métodos de la interfaz IManejadorCliente
    // -------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public Socket getSocket() {
        return socket;
    }

    /**
     * Cierra todos los flujos y la conexión con el cliente de forma segura.
     */
    @Override
    public void cerrarConexion() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            System.out.println("Conexión cerrada con: "
                    + (socket != null ? socket.getInetAddress() : "cliente desconocido"));
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión con el cliente sigue activa.
     *
     * @return {@code true} si el socket sigue conectado; {@code false} en caso
     * contrario.
     */
    @Override
    public boolean estaConectado() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
}
