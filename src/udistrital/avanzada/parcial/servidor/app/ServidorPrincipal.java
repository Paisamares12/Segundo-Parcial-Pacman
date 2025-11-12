package udistrital.avanzada.parcial.servidor.app;

import udistrital.avanzada.parcial.servidor.red.ManejadorCliente;
import udistrital.avanzada.parcial.servidor.vista.VentanaInicializacionBD;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase principal del servidor para el juego Pac-Man distribuido.
 *
 * <p>
 * Esta clase representa el punto de entrada del servidor. Su responsabilidad
 * principal es levantar la infraestructura de red, aceptar conexiones de
 * clientes y delegar la atención de cada cliente a un hilo independiente
 * mediante la clase {@link ManejadorCliente}.</p>
 *
 * <p>
 * Antes de iniciar el servicio de red, se ejecuta el proceso de inicialización
 * de la base de datos mediante la vista {@link VentanaInicializacionBD}, la
 * cual permite al usuario cargar los datos de jugadores desde un archivo
 * .properties hacia la base de datos MySQL.</p>
 *
 * <p>
 * <b>Arquitectura MVC + Servicios:</b></p>
 * <pre>
 * ServidorPrincipal (Infraestructura)
 *    → VentanaInicializacionBD (Vista)
 *       → ControlInicializacionBD (Control)
 *          → InicializadorBD (Persistencia)
 *    → ManejadorCliente (Capa de Red)
 *       → AutenticacionController (Controlador)
 *          → AutenticacionService (Servicio/Modelo)
 *             → UsuarioDAO (Acceso a Datos)
 * </pre>
 *
 * <p>
 * <b>Cumple con principios SOLID:</b></p>
 * <ul>
 * <li><b>S - Single Responsibility:</b> Solo maneja infraestructura y flujo de
 * red.</li>
 * <li><b>D - Dependency Inversion:</b> Depende de abstracciones
 * (controladores), no implementaciones directas.</li>
 * </ul>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-06
 */
public class ServidorPrincipal {

    /**
     * Puerto TCP en el que escuchará el servidor
     */
    private static final int PUERTO = 5000;

    /**
     * Punto de entrada principal del servidor Pac-Man.
     *
     * <p>
     * Flujo general:</p>
     * <ol>
     * <li>Inicializa la base de datos mediante la vista
     * {@code VentanaInicializacionBD}.</li>
     * <li>Levanta un {@link ServerSocket} en el puerto 5000.</li>
     * <li>Acepta conexiones entrantes de múltiples clientes.</li>
     * <li>Para cada cliente, lanza un hilo {@link ManejadorCliente}
     * independiente.</li>
     * </ol>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("=== SERVIDOR PAC-MAN DISTRIBUIDO ===");
        System.out.println("Versión 4.0 - Arquitectura MVC + Servicios\n");

        // Paso 1: Inicializar la base de datos antes de iniciar el servidor
        System.out.println("Inicializando base de datos de usuarios...");
        VentanaInicializacionBD ventanaInicializacion = new VentanaInicializacionBD();
        ventanaInicializacion.mostrar();
        System.out.println("Inicialización completada. Servidor listo para recibir clientes.\n");

        // Paso 2: Levantar servidor de red
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + PUERTO + "...");
            System.out.println("Esperando conexiones de clientes...\n");

            // Bucle infinito para atender múltiples clientes
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socketCliente.getInetAddress());

                // Crear un hilo dedicado para atender al cliente
                Thread hiloCliente = new Thread(new ManejadorCliente(socketCliente));
                hiloCliente.start();
            }

        } catch (IOException e) {
            System.err.println("✗ Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
