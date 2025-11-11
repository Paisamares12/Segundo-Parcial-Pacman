package udistrital.avanzada.parcial.servidor.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import udistrital.avanzada.parcial.cliente.modelo.JugadorVO;
import udistrital.avanzada.parcial.cliente.modelo.dao.UsuarioDAO;

/**
 * Clase de persistencia encargada de cargar usuarios desde un archivo .properties
 * a la base de datos MySQL.
 *
 * <p>Esta clase no contiene ninguna interfaz ni control de flujo.
 * Su única responsabilidad es leer el archivo de propiedades y
 * registrar los usuarios en la base de datos usando UsuarioDAO.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 3.0
 * @since 2025-11-06
 */
public class InicializadorBD {

    /**
     * Carga los usuarios definidos en un archivo .properties a la base de datos MySQL.
     *
     * @param rutaArchivo ruta del archivo .properties con las credenciales de los usuarios
     * @throws IOException si ocurre un error al leer el archivo
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    public void cargarUsuariosDesdeProperties(String rutaArchivo) throws IOException, SQLException {
        Properties props = new Properties();
        props.load(new FileInputStream(rutaArchivo));

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        int usuariosInsertados = 0;
        int usuariosExistentes = 0;

        for (String clave : props.stringPropertyNames()) {
            if (clave.startsWith("usuario.")) {
                String nombreUsuario = clave.substring("usuario.".length());
                String contraseña = props.getProperty(clave);

                JugadorVO jugador = new JugadorVO(nombreUsuario, contraseña, 0.0, 0.0);

                if (usuarioDAO.insertarUsuario(jugador)) {
                    usuariosInsertados++;
                } else {
                    usuariosExistentes++;
                }
            }
        }

        System.out.println("=== RESUMEN DE CARGA ===");
        System.out.println("Usuarios nuevos insertados: " + usuariosInsertados);
        System.out.println("Usuarios ya existentes o con error: " + usuariosExistentes);
        System.out.println("Total procesados: " + (usuariosInsertados + usuariosExistentes));
        System.out.println("========================");
    }
}
