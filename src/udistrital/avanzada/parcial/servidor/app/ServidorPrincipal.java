package udistrital.avanzada.parcial.servidor.app;

import udistrital.avanzada.parcial.servidor.red.ManejadorCliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase principal del servidor para el juego Pac-Man distribuido.
 * 
 * <p>Esta clase se encarga ÚNICAMENTE de la infraestructura de red:
 * levantar el servidor, aceptar conexiones y crear hilos para cada cliente.
 * Toda la lógica de negocio y autenticación está delegada a otras capas.</p>
 * 
 * <p>Responsabilidades (según SOLID - Single Responsibility):</p>
 * <ul>
 *   <li>Inicializar el ServerSocket</li>
 *   <li>Aceptar conexiones entrantes</li>
 *   <li>Crear hilos para manejar clientes</li>
 * </ul>
 * 
 * <p>NO se encarga de:</p>
 * <ul>
 *   <li>❌ Lógica de autenticación (AutenticacionService)</li>
 *   <li>❌ Acceso a base de datos (UsuarioDAO)</li>
 *   <li>❌ Comunicación por socket con clientes (ManejadorCliente)</li>
 *   <li>❌ Coordinación de flujos (AutenticacionController)</li>
 * </ul>
 * 
 * <p>Arquitectura MVC + Servicios:</p>
 * <pre>
 * ServidorPrincipal (Infraestructura)
 *    → ManejadorCliente (Capa de Red)
 *       → AutenticacionController (Controlador)
 *          → AutenticacionService (Servicio/Modelo)
 *             → UsuarioDAO (Acceso a Datos)
 * </pre>
 * 
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo infraestructura de red</li>
 *   <li><b>O - Open/Closed:</b> Extensible mediante diferentes manejadores</li>
 * </ul>
 * 
 * @author Juan Esteban Ariza Ortiz
 * @version 3.0
 * @since 2025-11-10
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
}