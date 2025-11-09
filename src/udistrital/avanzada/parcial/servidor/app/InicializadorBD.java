/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.app;

import java.io.File;
import udistrital.avanzada.parcial.cliente.conexion.ConexionBD;
import udistrital.avanzada.parcial.cliente.modelo.JugadorVO;
import udistrital.avanzada.parcial.cliente.modelo.dao.UsuarioDAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Clase utilitaria para inicializar la base de datos MySQL con usuarios desde un archivo properties.
 * 
 * <p>Esta clase se encarga de leer el archivo de configuración DatosConexionUsuarios.properties
 * y cargar todos los usuarios definidos en él a la base de datos MySQL. Es útil para la 
 * configuración inicial del sistema o para resetear la base de datos a un estado conocido.</p>
 * 
 * <p><b>IMPORTANTE:</b> Antes de ejecutar este inicializador, asegúrese de que:</p>
 * <ul>
 *   <li>XAMPP esté ejecutándose (Apache y MySQL activos)</li>
 *   <li>MySQL esté corriendo en el puerto 3306</li>
 *   <li>El driver mysql-connector-java-5.1.46 esté en el classpath</li>
 * </ul>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 2.0
 * @since 2025-11-09
 */

public class InicializadorBD {
    
    /**
     * Carga los usuarios desde un archivo properties a la base de datos MySQL.
     * 
     * <p>Lee el archivo properties especificado y busca todas las propiedades
     * que comienzan con "usuario.". Para cada una, crea un nuevo jugador en
     * la base de datos con contraseña correspondiente y valores iniciales de
     * puntaje y tiempo en 0.</p>
     * 
     * <p>Si un usuario ya existe en la base de datos, no se inserta nuevamente
     * y se muestra un mensaje informativo.</p>
     * 
     * @param rutaArchivo ruta del archivo properties con los datos de usuarios
     * @throws IOException si ocurre un error al leer el archivo
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    public static void cargarUsuariosDesdeProperties(String rutaArchivo) throws IOException, SQLException {
        Properties props = new Properties();
        
        System.out.println("Cargando archivo de propiedades: " + rutaArchivo);
        
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            props.load(fis);
        }
        
        // Verificar conexión a MySQL
        System.out.println("Conectando a MySQL...");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        int usuariosInsertados = 0;
        int usuariosExistentes = 0;
        
        // Iterar sobre todas las propiedades que empiezan con "usuario."
        for (String clave : props.stringPropertyNames()) {
            if (clave.startsWith("usuario.")) {
                String nombreUsuario = clave.substring("usuario.".length());
                String contraseña = props.getProperty(clave);
                
                // Crear un nuevo jugador con valores iniciales
                JugadorVO jugador = new JugadorVO(nombreUsuario, contraseña, 0.0, 0.0);
                
                // Intentar insertar en la base de datos
                if (usuarioDAO.insertarUsuario(jugador)) {
                    System.out.println("✓ Usuario insertado: " + nombreUsuario);
                    usuariosInsertados++;
                } else {
                    System.out.println("○ Usuario ya existe o error al insertar: " + nombreUsuario);
                    usuariosExistentes++;
                }
            }
        }
        
        System.out.println("\n=== RESUMEN DE CARGA ===");
        System.out.println("Usuarios nuevos insertados: " + usuariosInsertados);
        System.out.println("Usuarios ya existentes o con error: " + usuariosExistentes);
        System.out.println("Total procesados: " + (usuariosInsertados + usuariosExistentes));
        System.out.println("========================\n");
    }
    
    /**
     * Método principal para ejecutar la inicialización de la base de datos.
     * 
     * <p>Utiliza un JFileChooser para que el usuario seleccione el archivo
     * de configuración .properties que contiene los usuarios a cargar.</p>
     * 
     * <p><b>Prerequisitos:</b></p>
     * <ul>
     *   <li>XAMPP debe estar corriendo (Apache y MySQL)</li>
     *   <li>El archivo DatosConexionUsuarios.properties debe ser seleccionado</li>
     *   <li>El driver MySQL debe estar disponible</li>
     * </ul>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("=== INICIALIZADOR DE BASE DE DATOS PACMAN ===");
        System.out.println("MySQL en XAMPP - Puerto 3306\n");
        
        try {
            // Crear JFileChooser para seleccionar el archivo properties
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione el archivo de configuración de usuarios");
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "Archivos Properties (*.properties)", "properties"));
            fileChooser.setCurrentDirectory(new File(".")); // Directorio actual
            
            int resultado = fileChooser.showOpenDialog(null);
            
            if (resultado != JFileChooser.APPROVE_OPTION) {
                System.out.println("✗ No se seleccionó ningún archivo. Operación cancelada.");
                return;
            }
            
            File archivoConfig = fileChooser.getSelectedFile();
            String rutaArchivo = archivoConfig.getAbsolutePath();
            
            System.out.println("Archivo seleccionado: " + rutaArchivo);
            
            System.out.println("\nPASO 1: Verificando XAMPP y MySQL...");
            
            // Intentar obtener la conexión para verificar que MySQL está corriendo
            ConexionBD conexion = ConexionBD.getInstancia();
            
            if (conexion.isConexionActiva()) {
                System.out.println("✓ MySQL está corriendo y accesible\n");
                
                System.out.println("PASO 2: Cargando usuarios desde properties...");
                cargarUsuariosDesdeProperties(rutaArchivo);
                
                System.out.println("PASO 3: Cerrando conexión...");
                conexion.cerrarConexion();
                
                System.out.println("\n✓✓✓ INICIALIZACIÓN COMPLETADA EXITOSAMENTE ✓✓✓");
            } else {
                System.err.println("✗ No se pudo conectar a MySQL. Verifique que XAMPP esté corriendo.");
            }
            
        } catch (IOException e) {
            System.err.println("\n✗ ERROR: No se pudo leer el archivo properties");
            System.err.println("Detalle: " + e.getMessage());
            System.err.println("\nVerifique que el archivo seleccionado existe y es accesible.");
            
        } catch (SQLException e) {
            System.err.println("\n✗ ERROR: No se pudo conectar a MySQL");
            System.err.println("Detalle: " + e.getMessage());
            System.err.println("\nVerifique que:");
            System.err.println("1. XAMPP está corriendo");
            System.err.println("2. MySQL está activo en el puerto 3306");
            System.err.println("3. El usuario 'root' tiene acceso sin contraseña");
            System.err.println("4. El driver mysql-connector-java-5.1.46.jar está en el classpath");
            
        } catch (Exception e) {
            System.err.println("\n✗ ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
