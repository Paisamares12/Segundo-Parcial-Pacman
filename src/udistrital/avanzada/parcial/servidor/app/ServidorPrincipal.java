package udistrital.avanzada.parcial.servidor.app;

import java.io.*;
import java.net.*;

/**
 * Clase principal del lado del servidor para el juego Pac-Man distribuido.
 * 
 * <p>Esta clase se encarga de levantar un {@link ServerSocket} en un puerto
 * determinado (temporalmente configurado en 5000), aceptar una conexión de
 * un cliente y establecer la comunicación inicial a través de flujos de texto.
 * 
 * <p>En esta versión preliminar, el servidor:
 * <ul>
 *   <li>Escucha en el puerto especificado.</li>
 *   <li>Acepta una conexión de un cliente.</li>
 *   <li>Recibe un mensaje enviado por el cliente.</li>
 *   <li>Envía una respuesta de confirmación al cliente.</li>
 *   <li>Cierra la conexión de forma controlada.</li>
 * </ul>
 * 
 * <p>Más adelante, este servidor deberá evolucionar para:
 * <ul>
 *   <li>Leer la configuración (puerto, base de datos, etc.) desde un archivo de propiedades.</li>
 *   <li>Gestionar múltiples clientes a través de hilos dedicados.</li>
 *   <li>Implementar la lógica del juego Pac-Man en la máquina del servidor.</li>
 * </ul>
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 1.0
 * @since 2025-11-06
 */
public class ServidorPrincipal {

    /**
     * Punto de entrada principal del servidor.
     * 
     * <p>Inicializa un {@link ServerSocket} en el puerto 5000, espera una conexión
     * entrante de un cliente, establece los flujos de entrada/salida para la
     * comunicación y cierra la conexión una vez intercambiados los mensajes.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        int puerto = 5000; // TODO: este valor debe cargarse desde un archivo .properties

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexión en el puerto " + puerto + "...");

            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado desde " + socket.getInetAddress());

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            String mensajeCliente = entrada.readLine();
            System.out.println("Cliente dice: " + mensajeCliente);

            salida.println("Conexión aceptada por el servidor");

            socket.close();
            System.out.println("Servidor cerrado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
