package udistrital.avanzada.parcial.cliente.app;

import udistrital.avanzada.parcial.cliente.control.ConfiguracionCliente;
import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Clase principal del lado del cliente para el juego Pac-Man distribuido.
 * 
 * <p>Esta clase establece la conexión con el servidor a través de un socket 
 * orientado a la conexión, permitiendo el intercambio de mensajes entre el 
 * cliente y el servidor. Incorpora un selector de archivos (JFileChooser) para 
 * cargar la configuración desde un archivo .properties.</p>
 * 
 * <p>Funcionalidades implementadas:</p>
 * <ul>
 *   <li>Selección del archivo de configuración mediante JFileChooser.</li>
 *   <li>Carga de host y puerto desde el archivo properties.</li>
 *   <li>Solicitud de credenciales del usuario mediante diálogos.</li>
 *   <li>Conexión al servidor y envío de credenciales para autenticación.</li>
 *   <li>Manejo de respuesta de autenticación del servidor.</li>
 * </ul>
 * 
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 2.0
 * @since 2025-11-06
 */
public class ClientePrincipal {

    /**
     * Punto de entrada principal del cliente.
     * 
     * <p>Solicita al usuario que seleccione el archivo de configuración,
     * carga los parámetros necesarios, solicita las credenciales y establece
     * la conexión con el servidor para autenticación.</p>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Seleccionar archivo de configuración con JFileChooser
            JFileChooser fileChooser = new JFileChooser("src/data/DatosConexionUsuarios.properties");
            fileChooser.setDialogTitle("Seleccione el archivo de configuración");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Archivos Properties", "properties"));
            
            int resultado = fileChooser.showOpenDialog(null);
            
            if (resultado != JFileChooser.APPROVE_OPTION) {
                System.out.println("No se seleccionó ningún archivo. Saliendo...");
                return;
            }
            
            File archivoConfig = fileChooser.getSelectedFile();
            System.out.println("Archivo seleccionado: " + archivoConfig.getAbsolutePath());
            
            // Cargar configuración
            ConfiguracionCliente config = new ConfiguracionCliente(archivoConfig);
            String host = config.getHost();
            int puerto = config.getPuerto();
            
            System.out.println("Configuración cargada - Host: " + host + ", Puerto: " + puerto);
            
            // Solicitar credenciales al usuario
            String usuario = JOptionPane.showInputDialog(null, 
                    "Ingrese su nombre de usuario:", 
                    "Autenticación", 
                    JOptionPane.QUESTION_MESSAGE);
            
            if (usuario == null || usuario.trim().isEmpty()) {
                System.out.println("Usuario no proporcionado. Saliendo...");
                return;
            }
            
            JPasswordField passwordField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(null, 
                    passwordField, 
                    "Ingrese su contraseña:", 
                    JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
            
            if (option != JOptionPane.OK_OPTION) {
                System.out.println("Contraseña no proporcionada. Saliendo...");
                return;
            }
            
            String contraseña = new String(passwordField.getPassword());
            
            // Crear estado y controlador del cliente
            ClienteEstado estado = new ClienteEstado();
            ControlCliente controlCliente = new ControlCliente(estado);
            
            // Suscribirse a cambios en el log para mostrarlos en consola
            estado.addPropertyChangeListener(evt -> {
                if (ClienteEstado.PROP_LOG.equals(evt.getPropertyName())) {
                    System.out.println("[LOG] " + evt.getNewValue());
                }
            });
            
            // Conectar al servidor con autenticación
            System.out.println("Conectando al servidor...");
            controlCliente.conectar(host, puerto, usuario, contraseña);
            
            // Aquí se iniciaría la interfaz gráfica del juego si la autenticación es exitosa
            // Por ahora, solo mantenemos el programa en ejecución
            System.out.println("Cliente en ejecución. Presione Ctrl+C para salir.");
            
            // Mantener el programa en ejecución
            Thread.currentThread().join();

        } catch (IOException e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                    "Error al cargar el archivo de configuración: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            System.out.println("Aplicación interrumpida.");
        }
    }
}
