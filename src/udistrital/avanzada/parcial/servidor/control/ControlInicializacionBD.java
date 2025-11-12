package udistrital.avanzada.parcial.servidor.control;

import java.io.IOException;
import java.sql.SQLException;
import udistrital.avanzada.parcial.cliente.conexion.ConexionBD;
import udistrital.avanzada.parcial.servidor.persistencia.InicializadorBD;

/**
 * Controlador encargado de gestionar el proceso de inicialización
 * de la base de datos del servidor.
 *
 * <p>Se comunica con la capa de persistencia para cargar los usuarios
 * desde un archivo .properties. También valida la conexión a la base
 * de datos y maneja los mensajes de estado para el usuario.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 3.0
 * @since 2025-11-06
 */
public class ControlInicializacionBD {

    private final InicializadorBD inicializador;
    private final ConexionBD conexion;

    public ControlInicializacionBD() {
        this.inicializador = new InicializadorBD();
        ConexionBD temp = null;
        try {
            temp = ConexionBD.getInstancia();
        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar la conexión con la base de datos: " + e.getMessage());
        }
        this.conexion = temp;
    }


    /**
     * Ejecuta el proceso completo de inicialización de la base de datos.
     *
     * @param rutaArchivo ruta del archivo .properties con los usuarios
     */
    public void ejecutarInicializacion(String rutaArchivo) {
        try {
            if (conexion.isConexionActiva()) {
                System.out.println("✓ MySQL está corriendo y accesible.");
                inicializador.cargarUsuariosDesdeProperties(rutaArchivo);
                conexion.cerrarConexion();
                System.out.println("\n✓✓✓ INICIALIZACIÓN COMPLETADA EXITOSAMENTE ✓✓✓");
            } else {
                System.err.println("✗ No se pudo conectar a MySQL. Verifique que XAMPP esté corriendo.");
            }

        } catch (IOException e) {
            System.err.println("✗ Error al leer el archivo de propiedades: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Error al acceder a la base de datos: " + e.getMessage());
        }
    }
}
