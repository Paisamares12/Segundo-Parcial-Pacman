package udistrital.avanzada.parcial.cliente.app;

import java.io.*;
import java.net.*;

/**
 * Clase principal del lado del cliente para el juego Pac-Man distribuido.
 * 
 * <p>Esta clase establece la conexión con el servidor a través de un socket 
 * orientado a la conexión, permitiendo el intercambio inicial de mensajes
 * entre el cliente y el servidor. 
 * 
 * <p>En esta versión preliminar, el cliente:
 * <ul>
 *   <li>Se conecta al servidor especificado (por defecto, <b>localhost</b> y puerto <b>5000</b>).</li>
 *   <li>Envía un mensaje de saludo al servidor.</li>
 *   <li>Recibe y muestra la respuesta del servidor.</li>
 * </ul>
 * 
 * <p>En futuras versiones, este cliente deberá:
 * <ul>
 *   <li>Cargar la dirección IP y el puerto desde un archivo <b>.properties</b>.</li>
 *   <li>Solicitar las credenciales del jugador y enviarlas al servidor.</li>
 *   <li>Enviar las coordenadas de movimiento del Pac-Man según la interacción del usuario.</li>
 *   <li>Recibir mensajes o resultados del servidor y mostrarlos en una interfaz gráfica.</li>
 * </ul>
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 1.0
 * @since 2025-11-06
 */
public class ClientePrincipal {

    /**
     * Punto de entrada principal del cliente.
     * 
     * <p>Establece una conexión TCP con el servidor, envía un mensaje de saludo y 
     * muestra la respuesta recibida. Este método demuestra la comunicación básica 
     * entre cliente y servidor antes de incorporar la lógica del juego.</p>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        String host = "localhost"; // TODO: este valor debe cargarse desde un archivo .properties
        int puerto = 5000;  // TODO: este valor debe cargarse desde un archivo .properties

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("Conectado al servidor.");

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            salida.println("Hola servidor, soy el cliente Pac-Man!");
            String respuesta = entrada.readLine();
            System.out.println("Servidor responde: " + respuesta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
