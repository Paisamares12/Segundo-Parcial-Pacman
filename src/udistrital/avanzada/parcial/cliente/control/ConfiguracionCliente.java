package udistrital.avanzada.parcial.cliente.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase que gestiona la carga de configuración del cliente desde un archivo
 * properties.
 *
 * <p>
 * Esta clase permite cargar la configuración necesaria para que el cliente se
 * conecte al servidor, incluyendo el host, puerto y credenciales de usuarios
 * disponibles. Utiliza la clase Properties de Java para leer archivos
 * .properties.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-09
 */
public class ConfiguracionCliente {

    /**
     * Propiedades cargadas desde el archivo
     */
    private Properties propiedades;

    /**
     * Host del servidor
     */
    private String host;

    /**
     * Puerto del servidor
     */
    private int puerto;

    /**
     * Constructor que carga la configuración desde un archivo especificado.
     *
     * @param archivoConfig archivo de configuración .properties
     * @throws IOException si ocurre un error al leer el archivo
     */
    public ConfiguracionCliente(File archivoConfig) throws IOException {
        this.propiedades = new Properties();
        cargarConfiguracion(archivoConfig);
    }

    /**
     * Carga las propiedades desde el archivo de configuración.
     *
     * <p>
     * Lee el archivo properties y extrae los valores de host y puerto
     * configurados bajo las claves 'servidor.host' y 'servidor.puerto'.</p>
     *
     * @param archivo archivo de configuración
     * @throws IOException si ocurre un error al leer el archivo
     */
    private void cargarConfiguracion(File archivo) throws IOException {
        try (FileInputStream fis = new FileInputStream(archivo)) {
            propiedades.load(fis);

            this.host = propiedades.getProperty("servidor.host", "localhost");
            this.puerto = Integer.parseInt(propiedades.getProperty("servidor.puerto", "5000"));
        }
    }

    /**
     * Obtiene el host del servidor configurado.
     *
     * @return dirección del servidor
     */
    public String getHost() {
        return host;
    }

    /**
     * Obtiene el puerto del servidor configurado.
     *
     * @return puerto del servidor
     */
    public int getPuerto() {
        return puerto;
    }

    /**
     * Obtiene todas las propiedades cargadas.
     *
     * @return objeto Properties con toda la configuración
     */
    public Properties getPropiedades() {
        return propiedades;
    }

    /**
     * Verifica si un usuario existe en la configuración.
     *
     * @param nombreUsuario nombre del usuario a verificar
     * @return true si el usuario existe en el archivo properties, false en caso
     * contrario
     */
    public boolean existeUsuario(String nombreUsuario) {
        return propiedades.containsKey("usuario." + nombreUsuario);
    }

    /**
     * Obtiene la contraseña de un usuario desde la configuración.
     *
     * @param nombreUsuario nombre del usuario
     * @return contraseña del usuario, o null si no existe
     */
    public String getContraseñaUsuario(String nombreUsuario) {
        return propiedades.getProperty("usuario." + nombreUsuario);
    }
}
