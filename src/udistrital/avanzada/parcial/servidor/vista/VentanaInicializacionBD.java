package udistrital.avanzada.parcial.servidor.vista;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import udistrital.avanzada.parcial.servidor.control.ControlInicializacionBD;

/**
 * Vista encargada de permitir al usuario seleccionar el archivo .properties con
 * los datos de usuarios y ejecutar la inicialización de la base de datos.
 *
 * <p>
 * Usa JFileChooser como interfaz gráfica para elegir el archivo.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-06
 */
public class VentanaInicializacionBD {

    private final ControlInicializacionBD control;

    /**
     * Crea una nueva ventana encargada de la inicialización de la base de
     * datos.
     *
     * <p>
     * Al construirse, se instancia automáticamente un
     * {@link ControlInicializacionBD}, el cual gestiona la lógica relacionada
     * con la configuración e inicialización de la base de datos antes de
     * ejecutar el resto de la aplicación.
     * </p>
     *
     * <p>
     * Este constructor prepara el entorno necesario para las operaciones de
     * conexión, verificación de tablas y carga de datos iniciales.
     * </p>
     */
    public VentanaInicializacionBD() {
        this.control = new ControlInicializacionBD();
    }

    /**
     * Muestra un JFileChooser para seleccionar el archivo de configuración y
     * ejecuta el proceso de inicialización.
     */
    public void mostrar() {
        System.out.println("=== INICIALIZADOR DE BASE DE DATOS PACMAN ===");
        System.out.println("MySQL en XAMPP - Puerto 3306\n");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione el archivo de configuración de usuarios");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos Properties (*.properties)", "properties"));
        fileChooser.setCurrentDirectory(new File("."));

        int resultado = fileChooser.showOpenDialog(null);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            System.out.println("✗ No se seleccionó ningún archivo. Operación cancelada.");
            return;
        }

        String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
        System.out.println("Archivo seleccionado: " + rutaArchivo);

        control.ejecutarInicializacion(rutaArchivo);
    }
}
