package udistrital.avanzada.parcial.cliente.app;

import udistrital.avanzada.parcial.cliente.control.ConfiguracionCliente;
import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.cliente.vista.VentanaConfiguracionCliente;
import javax.swing.SwingUtilities;
import udistrital.avanzada.parcial.cliente.vista.MarcoCliente;
import udistrital.avanzada.parcial.cliente.control.ControlInterfazCliente;

import java.io.File;
import java.io.IOException;

/**
 * Clase principal del lado del cliente para el juego Pac-Man distribuido.
 *
 * <p>
 * Esta clase actúa como punto de inicio del cliente. Su responsabilidad es
 * coordinar la carga de la configuración, la solicitud de credenciales y la
 * inicialización del controlador principal que gestionará la comunicación con
 * el servidor.</p>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-11
 */
public class ClientePrincipal {

    public static void main(String[] args) {
        try {
            // Vista de configuración
            VentanaConfiguracionCliente vista = new VentanaConfiguracionCliente();

            // Seleccionar archivo de configuración
            File archivoConfig = vista.seleccionarArchivoConfiguracion();
            if (archivoConfig == null) {
                System.out.println("No se seleccionó ningún archivo de configuración. Saliendo...");
                return;
            }

            // Cargar configuración
            ConfiguracionCliente config = new ConfiguracionCliente(archivoConfig);
            String host = config.getHost();
            int puerto = config.getPuerto();

            System.out.println("Configuración cargada - Host: " + host + ", Puerto: " + puerto);

            // Solicitar credenciales
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

            // Crear estado y controlador
            ClienteEstado estado = new ClienteEstado();
            ControlCliente controlCliente = new ControlCliente(estado);

            // Crear vista del juego
            MarcoCliente vistaJuego = new MarcoCliente();
            vistaJuego.setEstado(estado);
            vistaJuego.setControl(controlCliente);

            SwingUtilities.invokeLater(() -> {
                vistaJuego.mostrar();
                new ControlInterfazCliente(controlCliente)
                        .instalarKeyBindings(vistaJuego.getComponenteJuego());
            });

            // Conectar al servidor
            System.out.println("Conectando al servidor...");
            controlCliente.conectar(host, puerto, usuario, contraseña);

            // Mantener ejecución
            System.out.println("Cliente en ejecución. Presione Ctrl+C para salir.");
            Thread.currentThread().join();

        } catch (IOException e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Aplicación interrumpida.");
        }
    }
}