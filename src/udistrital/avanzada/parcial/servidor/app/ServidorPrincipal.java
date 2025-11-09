package udistrital.avanzada.parcial.servidor.app;

import udistrital.avanzada.parcial.cliente.modelo.dao.UsuarioDAO;
import udistrital.avanzada.parcial.mensajes.SolicitudAutenticacion;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

/**
 * Clase principal del lado del servidor para el juego Pac-Man distribuido.
 * 
 * <p>Esta clase se encarga de levantar un {@link ServerSocket} en un puerto
 * determinado, aceptar conexiones de clientes y validar sus credenciales
 * contra la base de datos antes de permitir el acceso al juego.</p>
 * 
 * <p>Funcionalidades implementadas:</p>
 * <ul>
 *   <li>Escucha de conexiones entrantes en el puerto especificado.</li>
 *   <li>Recepción de solicitudes de autenticación de clientes.</li>
 *   <li>Validación de credenciales contra la base de datos usando UsuarioDAO.</li>
 *   <li>Envío de respuesta de autenticación (éxito o fallo).</li>
 *   <li>Cierre de conexión en caso de autenticación fallida.</li>
 *   <li>Gestión de múltiples clientes mediante hilos dedicados.</li>
 * </ul>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 2.0
 * @since 2025-11-06
 */
public class ServidorPrincipal {

    /** Puerto en el que escucha el servidor */
    private static final int PUERTO = 5000;

    /**
     * Punto de entrada principal del servidor.
     * 
     * <p>Inicializa un {@link ServerSocket} en el puerto especificado y entra
     * en un bucle infinito para aceptar conexiones de clientes. Cada cliente
     * es manejado en un hilo separado para permitir conexiones concurrentes.</p>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("Iniciando servidor Pac-Man...");
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor esperando conexión en el puerto " + PUERTO + "...");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socketCliente.getInetAddress());
                
                // Crear un hilo para manejar este cliente
                Thread hiloCliente = new Thread(new ManejadorCliente(socketCliente));
                hiloCliente.start();
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Clase interna que maneja la comunicación con un cliente específico.
     * 
     * <p>Se ejecuta en un hilo separado y se encarga de la autenticación del cliente
     * y del intercambio de mensajes durante el juego.</p>
     */
    private static class ManejadorCliente implements Runnable {
        
        /** Socket de comunicación con el cliente */
        private Socket socket;
        
        /**
         * Constructor que recibe el socket del cliente.
         * 
         * @param socket socket de comunicación con el cliente
         */
        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                // Recibir solicitud de autenticación
                Object solicitudObj = in.readObject();
                
                if (solicitudObj instanceof SolicitudAutenticacion solicitud) {
                    System.out.println("Solicitud de autenticación de: " + solicitud.getUsuario());
                    
                    // Validar credenciales con UsuarioDAO
                    boolean autenticado = validarCredenciales(
                            solicitud.getUsuario(), 
                            solicitud.getContraseña());
                    
                    RespuestaAutenticacion respuesta;
                    
                    if (autenticado) {
                        respuesta = new RespuestaAutenticacion(true, 
                                "Bienvenido " + solicitud.getUsuario() + "!");
                        out.writeObject(respuesta);
                        out.flush();
                        
                        System.out.println("Usuario autenticado: " + solicitud.getUsuario());
                        
                        // Aquí continuaría la lógica del juego
                        // Por ahora, solo mantenemos la conexión abierta
                        manejarJuego(in, out);
                        
                    } else {
                        respuesta = new RespuestaAutenticacion(false, 
                                "Credenciales inválidas. Conexión rechazada.");
                        out.writeObject(respuesta);
                        out.flush();
                        
                        System.out.println("Autenticación fallida para: " + solicitud.getUsuario());
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al manejar cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                    System.out.println("Conexión cerrada con cliente.");
                } catch (IOException e) {
                    System.err.println("Error al cerrar socket: " + e.getMessage());
                }
            }
        }

        /**
         * Valida las credenciales del usuario contra la base de datos.
         * 
         * <p>Utiliza el UsuarioDAO para verificar si el usuario existe y si
         * la contraseña proporcionada es correcta.</p>
         * 
         * @param usuario nombre de usuario
         * @param contraseña contraseña del usuario
         * @return true si las credenciales son válidas, false en caso contrario
         */
        private boolean validarCredenciales(String usuario, String contraseña) {
            try {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                return usuarioDAO.validarCredenciales(usuario, contraseña);
            } catch (SQLException e) {
                System.err.println("Error al validar credenciales: " + e.getMessage());
                return false;
            }
        }

        /**
         * Maneja la lógica del juego después de la autenticación exitosa.
         * 
         * <p>Este método se ejecuta en un bucle para recibir comandos del cliente
         * y enviar actualizaciones del estado del juego. En esta versión preliminar,
         * solo mantiene la conexión abierta.</p>
         * 
         * @param in flujo de entrada de objetos
         * @param out flujo de salida de objetos
         * @throws IOException si ocurre un error en la comunicación
         * @throws ClassNotFoundException si no se puede deserializar un objeto
         */
        private void manejarJuego(ObjectInputStream in, ObjectOutputStream out) 
                throws IOException, ClassNotFoundException {
            // Aquí iría la lógica del juego
            // Por ahora, solo esperamos para mantener la conexión abierta
            System.out.println("Sesión de juego iniciada...");
            
            // Placeholder: en una implementación completa, aquí se recibirían
            // ComandoMovimiento y se enviarían SnapshotTablero
        }
    }
}