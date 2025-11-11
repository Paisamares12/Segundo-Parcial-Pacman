package udistrital.avanzada.parcial.cliente.app;

import udistrital.avanzada.parcial.cliente.control.ConfiguracionCliente;
import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.cliente.vista.VentanaConfiguracionCliente;

import java.io.File;
import java.io.IOException;

/**
 * Clase principal del lado del cliente para el juego Pac-Man distribuido.
 * 
 * <p>Esta clase actúa como punto de inicio del cliente. Su responsabilidad es
 * coordinar la carga de la configuración, la solicitud de credenciales y la
 * inicialización del controlador principal que gestionará la comunicación con
 * el servidor.</p>
 * 
 * <p>No contiene ningún componente gráfico directamente, siguiendo el principio
 * de separación de responsabilidades del patrón MVC. Toda interacción con el
 * usuario se delega a la clase {@link VentanaConfiguracionCliente}.</p>
 * 
 * <p>Flujo de ejecución:</p>
 * <ol>
 *   <li>Solicita el archivo de configuración a través de la vista.</li>
 *   <li>Carga el host y puerto desde el archivo .properties.</li>
 *   <li>Solicita las credenciales del usuario.</li>
 *   <li>Inicializa el estado del cliente y el controlador.</li>
 *   <li>Conecta con el servidor para autenticación.</li>
 * </ol>
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 3.0
 * @since 2025-11-11
 */
public class ClientePrincipal {

    /**
     * Punto de entrada principal del cliente.
     * 
     * <p>Coordina la selección del archivo de configuración, la solicitud de credenciales
     * y la conexión inicial al servidor. No realiza operaciones visuales directamente.</p>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Instanciar vista de configuración
            VentanaConfiguracionCliente vista = new VentanaConfiguracionCliente();

            // Seleccionar archivo de configuración
            File archivoConfig = vista.seleccionarArchivoConfiguracion();
            if (archivoConfig == null) {
                System.out.println("No se seleccionó ningún archivo de configuración. Saliendo...");
                return;
            }

            // Cargar configuración desde el archivo .properties
            ConfiguracionCliente config = new ConfiguracionCliente(archivoConfig);
            String host = config.getHost();
            int puerto = config.getPuerto();

            System.out.println("Configuración cargada - Host: " + host + ", Puerto: " + puerto);

            // Solicitar credenciales al usuario
            String usuario = vista.solicitarUsuario();
            if (usuario == null || usuario.trim().isEmpty()) {
                System.out.println("Usuario no proporcionado. Saliendo...");
                return;
            }

            String contraseña = vista.solicitarContraseña();
            if (contraseña == null) {
                System.out.println("Contraseña no proporcionada. Saliendo...");
                return;
            }

            // Crear estado y controlador del cliente
            ClienteEstado estado = new ClienteEstado();
            ControlCliente controlCliente = new ControlCliente(estado);

            // Suscribirse a eventos del estado para mostrar logs en consola
            estado.addPropertyChangeListener(evt -> {
                if (ClienteEstado.PROP_LOG.equals(evt.getPropertyName())) {
                    System.out.println("[LOG] " + evt.getNewValue());
                }
            });

            // Conectar al servidor
            System.out.println("Conectando al servidor...");
            controlCliente.conectar(host, puerto, usuario, contraseña);

            // Mantener la ejecución del cliente
            System.out.println("Cliente en ejecución. Presione Ctrl+C para salir.");
            Thread.currentThread().join();

        } catch (IOException e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Aplicación interrumpida.");
        }
    }
}

