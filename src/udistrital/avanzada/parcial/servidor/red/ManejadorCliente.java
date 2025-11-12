package udistrital.avanzada.parcial.servidor.red;

import udistrital.avanzada.parcial.cliente.modelo.dao.IUsuarioDAO;
import udistrital.avanzada.parcial.cliente.modelo.dao.UsuarioDAO;
import udistrital.avanzada.parcial.mensajes.*;
import udistrital.avanzada.parcial.servidor.control.*;
import udistrital.avanzada.parcial.servidor.modelo.*;
import udistrital.avanzada.parcial.servidor.servicios.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejador de comunicación con un cliente específico.
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 2.3
 * @since 2025-11-11
 */
public class ManejadorCliente implements IManejadorCliente {
    
    private final Socket socket;
    private AutenticacionController autenticacionController;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String nombreJugador;
    
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }
    
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
    
    private void inicializarDependencias() throws SQLException {
        IUsuarioDAO usuarioDAO = new UsuarioDAO();
        IAutenticacionService autenticacionService = new AutenticacionService(usuarioDAO);
        this.autenticacionController = new AutenticacionController(autenticacionService);
    }
    
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
        
        // Servicio de streaming para capturar frames
        ServicioStreaming streaming = new ServicioStreaming();
        
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
                
                int frutasComidasAntes = (int) estado.getFrutas().stream()
                        .filter(Fruta::isComida)
                        .count();
                
                Direccion direccion = Direccion.desdeTexto(comando.getDireccion());
                ResultadoMovimiento resultado = controlJuego.procesarComando(direccion);
                
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
                
                // 1. Enviar respuesta del movimiento
                out.writeObject(respuesta);
                out.flush();
                
                // 2. NUEVO: Capturar y enviar frame del tablero
                try {
                    byte[] frameBytes = streaming.capturarYCodificar(
                        componentes.getVista().getPanelJuego()
                    );
                    out.writeInt(frameBytes.length);
                    out.write(frameBytes);
                    out.flush();
                } catch (IOException e) {
                    System.err.println("Error al enviar frame: " + e.getMessage());
                }
                
                System.out.println("Respuesta y frame enviados");
                
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
    
    private String formatearTiempo(long ms) {
        long min = ms / 60000;
        long sec = (ms % 60000) / 1000;
        long mil = ms % 1000;
        return String.format("%02d:%02d.%03d", min, sec, mil);
    }
    
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
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
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