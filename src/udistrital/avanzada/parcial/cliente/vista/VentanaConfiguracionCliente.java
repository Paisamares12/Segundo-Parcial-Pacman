package udistrital.avanzada.parcial.cliente.vista;

import javax.swing.*;
import java.io.File;

/**
 * Clase de interfaz gráfica encargada de gestionar la configuración
 * inicial del cliente.
 * 
 * <p>Esta clase permite al usuario:</p>
 * <ul>
 *   <li>Seleccionar un archivo de configuración (.properties) mediante {@link JFileChooser}.</li>
 *   <li>Ingresar su nombre de usuario y contraseña mediante cuadros de diálogo.</li>
 * </ul>
 * 
 * <p>Forma parte del paquete <b>vista</b>, cumpliendo con la separación de responsabilidades
 * del patrón MVC, donde toda interacción visual se maneja exclusivamente en esta capa.</p>
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-11
 */
public class VentanaConfiguracionCliente {

    /**
     * Muestra un cuadro de diálogo para seleccionar el archivo de configuración
     * (.properties) que contiene los datos de conexión del cliente.
     * 
     * @return el archivo seleccionado por el usuario o {@code null} si se cancela la operación.
     */
    public File seleccionarArchivoConfiguracion() {
        JFileChooser fileChooser = new JFileChooser("src/data/DatosConexionUsuarios.properties");
        fileChooser.setDialogTitle("Seleccione el archivo de configuración");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos Properties", "properties"));

        int resultado = fileChooser.showOpenDialog(null);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return fileChooser.getSelectedFile();
    }

    /**
     * Solicita al usuario que ingrese su nombre de usuario mediante un cuadro de diálogo.
     * 
     * @return el nombre de usuario ingresado o {@code null} si se cancela la operación.
     */
    public String solicitarUsuario() {
        return JOptionPane.showInputDialog(
                null,
                "Ingrese su nombre de usuario:",
                "Autenticación",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    /**
     * Solicita al usuario que ingrese su contraseña mediante un cuadro de diálogo con campo oculto.
     * 
     * @return la contraseña ingresada como {@link String}, o {@code null} si se cancela la operación.
     */
    public String solicitarContraseña() {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                null,
                passwordField,
                "Ingrese su contraseña:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option != JOptionPane.OK_OPTION) {
            return null;
        }
        return new String(passwordField.getPassword());
    }
}
