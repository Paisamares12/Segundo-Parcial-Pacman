package udistrital.avanzada.parcial.cliente.app;

//Importar clases desde otros paquetes
import udistrital.avanzada.parcial.cliente.control.ConfiguracionCliente;
import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.cliente.vista.VentanaConfiguracionCliente;
import udistrital.avanzada.parcial.cliente.vista.MarcoCliente;
import udistrital.avanzada.parcial.cliente.control.ControlInterfazCliente;
//Importar librerias necesarias
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * Clase principal del lado del cliente para el juego distribuido
 * <b>Pac-Man</b>.
 *
 * <p>
 * Representa el punto de entrada (método {@code main}) de la aplicación
 * cliente. Se encarga de orquestar la inicialización completa del entorno del
 * jugador, incluyendo los siguientes pasos:
 * </p>
 *
 * <ul>
 * <li>Carga de la configuración desde un archivo externo (.properties).</li>
 * <li>Solicitud de credenciales al usuario (nombre y contraseña).</li>
 * <li>Creación del estado del cliente, su controlador y las vistas
 * gráficas.</li>
 * <li>Conexión con el servidor y la instalación de los controles de juego.</li>
 * </ul>
 *
 * <p>
 * Este flujo garantiza la correcta separación de responsabilidades bajo el
 * patrón
 * <b>MVC</b> (Modelo - Vista - Controlador):
 * </p>
 *
 * <ul>
 * <li><b>Modelo:</b> {@link ClienteEstado}</li>
 * <li><b>Vista:</b>
 * {@link VentanaConfiguracionCliente}, {@link MarcoCliente}</li>
 * <li><b>Controlador:</b>
 * {@link ControlCliente}, {@link ControlInterfazCliente}</li>
 * </ul>
 *
 * <p>
 * <b>Modificado por:</b> Paula Martínez<br>
 * <b>Autores originales:</b> Juan Estevan Ariza Ortiz y Juan Sebastián Bravo
 * Rojas<br>
 * <b>Versión:</b> 4.0<br>
 * <b>Desde:</b> 2025-11-11
 * </p>
 */
public class ClientePrincipal {

    /**
     * Método principal de ejecución de la aplicación cliente.
     *
     * <p>
     * Coordina el proceso completo de arranque del cliente en este orden:
     * </p>
     *
     * <ol>
     * <li>Solicita la configuración del servidor desde un archivo.</li>
     * <li>Solicita las credenciales de usuario.</li>
     * <li>Inicializa los objetos de control y vista.</li>
     * <li>Establece conexión con el servidor.</li>
     * <li>Inicia la interfaz gráfica y mantiene el hilo principal activo.</li>
     * </ol>
     *
     * @param args Argumentos de línea de comandos (no utilizados en esta
     * aplicación).
     */
    public static void main(String[] args) {
        try {
            // ---- 1. CREACIÓN DE LA VISTA DE CONFIGURACIÓN ----
            // Se instancia una ventana encargada de solicitar la configuración inicial al usuario.
            VentanaConfiguracionCliente vista = new VentanaConfiguracionCliente();

            // ---- 2. SELECCIÓN DEL ARCHIVO DE CONFIGURACIÓN ----
            // El usuario selecciona el archivo de configuración (.properties) que contiene host y puerto.
            File archivoConfig = vista.seleccionarArchivoConfiguracion();

            // Si no se selecciona ningún archivo, el programa termina con un mensaje informativo.
            if (archivoConfig == null) {
                System.out.println("No se seleccionó ningún archivo de configuración. Saliendo...");
                return;
            }

            // ---- 3. CARGA DE LA CONFIGURACIÓN ----
            // Se crea un objeto de configuración a partir del archivo elegido.
            ConfiguracionCliente config = new ConfiguracionCliente(archivoConfig);

            // Se obtienen los datos del servidor (host y puerto) desde el archivo.
            String host = config.getHost();
            int puerto = config.getPuerto();

            // Se imprime la información de configuración para verificación.
            System.out.println("Configuración cargada - Host: " + host + ", Puerto: " + puerto);

            // ---- 4. SOLICITUD DE CREDENCIALES ----
            // El cliente solicita al usuario su nombre de usuario (nickname).
            String usuario = vista.solicitarUsuario();
            if (usuario == null || usuario.trim().isEmpty()) {
                System.out.println("Usuario no proporcionado. Saliendo...");
                return;
            }

            // Luego se solicita la contraseña correspondiente.
            String contraseña = vista.solicitarContraseña();
            if (contraseña == null) {
                System.out.println("Contraseña no proporcionada. Saliendo...");
                return;
            }

            // ---- 5. CREACIÓN DEL ESTADO Y CONTROLADOR PRINCIPAL ----
            // El estado almacena la información dinámica del cliente (posición, puntuación, etc.)
            ClienteEstado estado = new ClienteEstado();

            // El controlador principal gestiona la lógica de comunicación con el servidor.
            ControlCliente controlCliente = new ControlCliente(estado);

            // ---- 6. CREACIÓN DE LA VISTA PRINCIPAL DEL JUEGO ----
            // Se inicializa la interfaz gráfica del cliente (ventana principal del juego).
            MarcoCliente vistaJuego = new MarcoCliente();

            // Se enlaza la vista con el estado del cliente.
            vistaJuego.setEstado(estado);

            // Se enlaza también con el controlador para manejar acciones de usuario.
            vistaJuego.setControl(controlCliente);

            // ---- 7. INICIALIZACIÓN DE LA INTERFAZ GRÁFICA (en el hilo de eventos de Swing) ----
            SwingUtilities.invokeLater(() -> {
                // Se muestra la ventana principal del juego.
                vistaJuego.mostrar();

                // Se instalan los key bindings (controles de teclado) para mover al jugador.
                new ControlInterfazCliente(controlCliente)
                        .instalarKeyBindings(vistaJuego.getComponenteJuego());
            });

            // ---- 8. CONEXIÓN CON EL SERVIDOR ----
            System.out.println("Conectando al servidor...");
            controlCliente.conectar(host, puerto, usuario, contraseña);

            // ---- 9. MANTENER LA EJECUCIÓN DEL CLIENTE ----
            // Se evita que el hilo principal termine, manteniendo la aplicación activa.
            System.out.println("Cliente en ejecución. Presione Ctrl+C para salir.");
            Thread.currentThread().join();

            // ---- 10. MANEJO DE EXCEPCIONES ----
        } catch (IOException e) {
            // Captura de errores al cargar el archivo de configuración o al conectarse.
            System.err.println("Error al cargar la configuración: " + e.getMessage());

        } catch (InterruptedException e) {
            // Si el hilo principal es interrumpido, se muestra un mensaje informativo.
            System.out.println("Aplicación interrumpida.");
        }
    }
}
