/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.red;

import udistrital.avanzada.parcial.cliente.modelo.dao.IUsuarioDAO;
import udistrital.avanzada.parcial.cliente.modelo.dao.UsuarioDAO;
import udistrital.avanzada.parcial.mensajes.SolicitudAutenticacion;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;
import udistrital.avanzada.parcial.servidor.control.AutenticacionController;
import udistrital.avanzada.parcial.servidor.servicios.AutenticacionService;
import udistrital.avanzada.parcial.servidor.servicios.IAutenticacionService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
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
 *   <li>Cerrar conexiones de forma segura</li>
 * </ul>
 * 
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo maneja comunicación por socket</li>
 *   <li><b>D - Dependency Inversion:</b> Depende de interfaces (IAutenticacionService, IUsuarioDAO)</li>
 * </ul>
 * 
 * @author Juan Esteban Ariza Ortiz
 * @version 1.0
 * @since 2025-11-10
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
     * <p>Este método mantiene la conexión abierta y gestiona el flujo del juego.
     * En esta versión, solo mantiene la conexión activa. En futuras versiones,
     * aquí se procesarían los comandos de juego.</p>
     * 
     * @throws IOException si ocurre un error de comunicación
     * @throws ClassNotFoundException si no se puede deserializar un objeto
     */
    private void iniciarSesionJuego() throws IOException, ClassNotFoundException {
        System.out.println("Sesión de juego iniciada para cliente: " + socket.getInetAddress());
        
        // TODO: Aquí irá la lógica del juego
        // Por ahora, solo mantiene la conexión abierta
        // En el futuro:
        // - Recibir ComandoMovimiento
        // - Actualizar estado del juego
        // - Enviar SnapshotTablero
        
        System.out.println("Manteniendo sesión activa...");
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