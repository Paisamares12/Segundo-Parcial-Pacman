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

/**
 * 
 * "MODIFICACION"
 * 
 * Manejador de comunicación con un cliente específico.
 * 
 * <p>Esta clase se encarga exclusivamente de la comunicación por socket
 * con un cliente. Se ejecuta en un hilo separado y coordina con el
 * controlador para procesar las solicitudes.</p>
 * 
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Gestionar streams de entrada/salida</li>
 *   <li>Recibir objetos del cliente</li>
 *   <li>Enviar objetos al cliente</li>
 *   <li>Coordinar con el controlador</li>
 *   <li>Manejar el ciclo de juego completo</li>
 *   <li>Cerrar conexiones de forma segura</li>
 * </ul>
 * 
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo maneja comunicación por socket</li>
 *   <li><b>D - Dependency Inversion:</b> Depende de interfaces (IAutenticacionService, IUsuarioDAO)</li>
 * </ul>
 * 
 * Modificado: Juan Ariza
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 2.0
 * @since 2025-11-11
 */
public class ManejadorCliente implements IManejadorCliente {
    
    /** Socket de comunicación con el cliente */
    private final Socket socket;
    
    /** Controlador de autenticación */
    private AutenticacionController autenticacionController;
    
    /** Stream de salida para enviar objetos al cliente */
    private ObjectOutputStream out;
    
    /** Stream de entrada para recibir objetos del cliente */
    private ObjectInputStream in;
    
    /**
     * Constructor que recibe el socket del cliente.
     * 
     * <p>Las dependencias (DAO, Service, Controller) se crean internamente
     * cuando se inicia el hilo para mantener el ServidorPrincipal simple.</p>
     * 
     * @param socket socket de comunicación con el cliente
     */
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Flujo de ejecución:</p>
     * <ol>
     *   <li>Inicializar dependencias (DAO, Service, Controller)</li>
     *   <li>Inicializar streams de comunicación</li>
     *   <li>Recibir solicitud de autenticación</li>
     *   <li>Procesar autenticación mediante el controlador</li>
     *   <li>Enviar respuesta al cliente</li>
     *   <li>Si autenticación exitosa, iniciar sesión de juego</li>
     *   <li>Si falla, cerrar conexión</li>
     * </ol>
     */
    @Override
    public void run() {
        try {
            // Inicializar dependencias para este cliente
            inicializarDependencias();
            
            // Inicializar streams
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("Conexión establecida con: " + socket.getInetAddress());
            
            // Procesar autenticación
            if (procesarAutenticacion()) {
                // Si la autenticación fue exitosa, iniciar sesión de juego
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
    
    /**
     * Inicializa las dependencias necesarias para la autenticación.
     * 
     * <p>Crea la cadena de dependencias:</p>
     * <ol>
     *   <li>UsuarioDAO (acceso a datos)</li>
     *   <li>AutenticacionService (lógica de negocio)</li>
     *   <li>AutenticacionController (coordinación)</li>
     * </ol>
     * 
     * @throws SQLException si no se puede conectar a la base de datos
     */
    private void inicializarDependencias() throws SQLException {
        IUsuarioDAO usuarioDAO = new UsuarioDAO();
        IAutenticacionService autenticacionService = new AutenticacionService(usuarioDAO);
        this.autenticacionController = new AutenticacionController(autenticacionService);
    }
    
    /**
     * Procesa la autenticación del cliente.
     * 
     * @return true si la autenticación fue exitosa, false en caso contrario
     * @throws IOException si ocurre un error de comunicación
     * @throws ClassNotFoundException si no se puede deserializar el objeto
     */
    private boolean procesarAutenticacion() throws IOException, ClassNotFoundException {
        // Recibir solicitud del cliente
        Object solicitudObj = in.readObject();
        
        if (!(solicitudObj instanceof SolicitudAutenticacion)) {
            System.err.println("Objeto recibido no es una SolicitudAutenticacion");
            enviarRespuestaError("Tipo de solicitud no válido");
            return false;
        }
        
        SolicitudAutenticacion solicitud = (SolicitudAutenticacion) solicitudObj;
        
        // Procesar mediante el controlador
        RespuestaAutenticacion respuesta = autenticacionController.procesarAutenticacion(solicitud);
        
        // Enviar respuesta al cliente
        out.writeObject(respuesta);
        out.flush();
        
        return respuesta.isExitosa();
    }
    
    /**
     * Inicia la sesión de juego después de una autenticación exitosa.
     * 
     * <p>Este método gestiona el ciclo completo del juego:</p>
     * <ol>
     *   <li>Inicializa el estado del juego y la vista del servidor</li>
     *   <li>Entra en un bucle que recibe comandos del cliente</li>
     *   <li>Procesa cada comando y actualiza el estado</li>
     *   <li>Envía respuesta con el resultado del movimiento</li>
     *   <li>Termina cuando se comen todas las frutas</li>
     * </ol>
     * 
     * @throws IOException si ocurre un error de comunicación
     * @throws ClassNotFoundException si no se puede deserializar un objeto
     */
    private void iniciarSesionJuego() throws IOException, ClassNotFoundException {
        System.out.println("===========================================");
        System.out.println("Sesión de juego iniciada para: " + socket.getInetAddress());
        System.out.println("===========================================\n");
        
        // 1. Inicializar el juego
        InicializadorJuego inicializador = new InicializadorJuego();
        InicializadorJuego.ComponentesJuego componentes = inicializador.inicializar();
        
        ControlJuego controlJuego = componentes.getControlJuego();
        EstadoJuego estado = componentes.getEstado();
        
        // 2. Enviar snapshot inicial al cliente
        SnapshotTablero snapshotInicial = SnapshotFactory.fromEstado(estado);
        out.writeObject(snapshotInicial);
        out.flush();
        System.out.println("Snapshot inicial enviado al cliente\n");
        
        // 3. Bucle principal del juego
        boolean juegoActivo = true;
        int turno = 0;
        
        while (juegoActivo) {
            try {
                // Recibir comando del cliente
                Object comandoObj = in.readObject();
                
                if (!(comandoObj instanceof ComandoMovimiento)) {
                    System.err.println("Objeto recibido no es un ComandoMovimiento");
                    continue;
                }
                
                ComandoMovimiento comando = (ComandoMovimiento) comandoObj;
                turno++;
                
                System.out.println("--- Turno " + turno + " ---");
                System.out.println("Comando recibido: " + comando.getDireccion());
                
                // Convertir dirección de texto a enum
                Direccion direccion = Direccion.desdeTexto(comando.getDireccion());
                
                // Procesar el movimiento
                ResultadoMovimiento resultado = controlJuego.procesarComando(direccion);
                
                // Construir respuesta
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
                
                // Enviar respuesta al cliente
                out.writeObject(respuesta);
                out.flush();
                
                // Log del resultado
                System.out.println("Respuesta enviada: " + respuesta);
                
                // Verificar si el juego terminó
                if (respuesta.isJuegoTerminado()) {
                    System.out.println("\n===========================================");
                    System.out.println("¡JUEGO TERMINADO!");
                    System.out.println("Puntaje final: " + respuesta.getPuntaje());
                    System.out.println("Turnos jugados: " + turno);
                    System.out.println("===========================================\n");
                    juegoActivo = false;
                }
                
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error durante el juego: " + e.getMessage());
                juegoActivo = false;
            }
        }
        
        System.out.println("Sesión de juego finalizada para: " + socket.getInetAddress());
    }
    
    /**
     * Envía una respuesta de error al cliente.
     * 
     * @param mensaje mensaje de error
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
    
    @Override
    public Socket getSocket() {
        return socket;
    }
    
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
            System.out.println("Conexión cerrada con: " + 
                (socket != null ? socket.getInetAddress() : "cliente desconocido"));
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
    
    @Override
    public boolean estaConectado() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
}