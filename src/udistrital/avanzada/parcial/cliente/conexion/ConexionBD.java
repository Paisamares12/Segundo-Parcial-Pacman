package udistrital.avanzada.parcial.cliente.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase Singleton que gestiona la conexión a la base de datos MySQL.
 * 
 * <p>Esta clase implementa el patrón Singleton para garantizar una única
 * instancia de conexión a la base de datos durante la ejecución del programa.
 * Proporciona métodos para obtener la conexión y cerrarla de manera segura.</p>
 * 
 * <p>La base de datos utilizada es MySQL ejecutándose en XAMPP (Apache + MySQL)
 * en el puerto 3306. Se conecta a la base de datos 'pacman_db' con el usuario
 * 'root' sin contraseña (configuración por defecto de XAMPP).</p>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 3.0
 * @since 2025-11-09
 */
public class ConexionBD {
    
    /** URL de conexión a la base de datos MySQL */
    private static final String URL = "jdbc:mysql://localhost:3306/pacman_db";
    
    /** Usuario de la base de datos (por defecto 'root' en XAMPP) */
    private static final String USUARIO = "root";
    
    /** Contraseña de la base de datos (por defecto vacía en XAMPP) */
    private static final String PASSWORD = "";
    
    /** Instancia única de la clase (patrón Singleton) */
    private static ConexionBD instancia;
    
    /** Conexión activa a la base de datos */
    private Connection conexion;
    
    /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa la conexión a la base de datos MySQL y crea la tabla de usuarios si no existe.
     * 
     * @throws SQLException si ocurre un error al establecer la conexión o crear la tabla
     */
    private ConexionBD() throws SQLException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.jdbc.Driver");
            
            // Primero, conectar sin especificar la base de datos para crearla si no existe
            Connection connTemp = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/", USUARIO, PASSWORD);
            
            Statement stmt = connTemp.createStatement();
            // Crear la base de datos si no existe
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS pacman_db");
            stmt.close();
            connTemp.close();
            
            // Ahora conectar a la base de datos específica
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a MySQL - Base de datos: pacman_db");
            
            // Crear la tabla de usuarios si no existe
            crearTablaUsuarios();
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC de MySQL no encontrado: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene la instancia única de ConexionBD (patrón Singleton).
     * 
     * <p>Si la instancia no existe o la conexión está cerrada, crea una nueva instancia.</p>
     * 
     * @return la instancia única de ConexionBD
     * @throws SQLException si ocurre un error al crear la conexión
     */
    public static ConexionBD getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
    
    /**
     * Obtiene la conexión activa a la base de datos.
     * 
     * @return objeto Connection para realizar operaciones en la base de datos
     */
    public Connection getConexion() {
        return conexion;
    }
    
    /**
     * Crea la tabla de usuarios en la base de datos si no existe.
     * 
     * <p>La tabla contiene los siguientes campos:</p>
     * <ul>
     *   <li><b>nombre</b>: nombre de usuario (VARCHAR 50, clave primaria)</li>
     *   <li><b>contraseña</b>: contraseña del usuario (VARCHAR 100)</li>
     *   <li><b>puntaje</b>: puntaje acumulado del jugador (DOUBLE, por defecto 0)</li>
     *   <li><b>tiempo</b>: tiempo total de juego (DOUBLE, por defecto 0)</li>
     * </ul>
     * 
     * @throws SQLException si ocurre un error al crear la tabla
     */
    private void crearTablaUsuarios() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "nombre VARCHAR(50) PRIMARY KEY,"
                + "contraseña VARCHAR(100) NOT NULL,"
                + "puntaje DOUBLE DEFAULT 0,"
                + "tiempo DOUBLE DEFAULT 0"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'usuarios' verificada/creada correctamente.");
        }
    }
    
    /**
     * Cierra la conexión a la base de datos de forma segura.
     * 
     * <p>Es importante llamar a este método antes de finalizar la aplicación
     * para liberar los recursos adecuadamente.</p>
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión a MySQL cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si la conexión está activa.
     * 
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConexionActiva() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
