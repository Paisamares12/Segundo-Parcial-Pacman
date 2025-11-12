package udistrital.avanzada.parcial.cliente.modelo.dao;

import udistrital.avanzada.parcial.cliente.conexion.ConexionBD;
import udistrital.avanzada.parcial.cliente.modelo.UsuarioVO;
import udistrital.avanzada.parcial.cliente.modelo.JugadorVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar las operaciones CRUD de
 * usuarios en la base de datos.
 *
 * <p>
 * Esta clase implementa el patrón DAO, proporcionando una abstracción entre la
 * lógica de negocio y la capa de persistencia de datos. Permite realizar
 * operaciones como insertar, buscar, actualizar y eliminar usuarios sin que el
 * resto de la aplicación conozca los detalles de la implementación de la base
 * de datos.</p>
 *
 * <p>
 * Modificado por Paula Martínez
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-09
 */
public class UsuarioDAO implements IUsuarioDAO {

    /**
     * Instancia de la conexión a la base de datos
     */
    private ConexionBD conexionBD;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @throws SQLException si ocurre un error al obtener la conexión
     */
    public UsuarioDAO() throws SQLException {
        this.conexionBD = ConexionBD.getInstancia();
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario objeto UsuarioVO con los datos del usuario a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertarUsuario(UsuarioVO usuario) {
        String sql = "INSERT INTO usuarios (nombre, contraseña, puntaje, tiempo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexionBD.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getContraseña());
            pstmt.setDouble(3, usuario.getPuntaje());
            pstmt.setDouble(4, usuario.getTiempo());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un usuario en la base de datos por su nombre.
     *
     * @param nombre nombre del usuario a buscar
     * @return objeto JugadorVO con los datos del usuario, o null si no existe
     */
    public JugadorVO buscarUsuario(String nombre) {
        String sql = "SELECT * FROM usuarios WHERE nombre = ?";

        try (PreparedStatement pstmt = conexionBD.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JugadorVO jugador = new JugadorVO(
                        rs.getString("nombre"),
                        rs.getString("contraseña"),
                        rs.getDouble("puntaje"),
                        rs.getDouble("tiempo")
                );
                rs.close();
                return jugador;
            }
            rs.close();

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    /**
     * Valida las credenciales de un usuario.
     *
     * <p>
     * Verifica que el usuario exista y que la contraseña proporcionada coincida
     * con la almacenada en la base de datos.</p>
     *
     * @param nombre nombre del usuario
     * @param contraseña contraseña a validar
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean validarCredenciales(String nombre, String contraseña) {
        String sql = "SELECT contraseña FROM usuarios WHERE nombre = ?";

        try (PreparedStatement pstmt = conexionBD.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String contraseñaDB = rs.getString("contraseña");
                rs.close();
                return contraseñaDB.equals(contraseña);
            }
            rs.close();

        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
        }

        return false;
    }

    /**
     * Actualiza los datos de un usuario existente en la base de datos.
     *
     * @param usuario objeto UsuarioVO con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarUsuario(UsuarioVO usuario) {
        String sql = "UPDATE usuarios SET contraseña = ?, puntaje = ?, tiempo = ? WHERE nombre = ?";

        try (PreparedStatement pstmt = conexionBD.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getContraseña());
            pstmt.setDouble(2, usuario.getPuntaje());
            pstmt.setDouble(3, usuario.getTiempo());
            pstmt.setString(4, usuario.getNombre());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param nombre nombre del usuario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarUsuario(String nombre) {
        String sql = "DELETE FROM usuarios WHERE nombre = ?";

        try (PreparedStatement pstmt = conexionBD.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, nombre);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una lista de todos los usuarios registrados en la base de datos.
     *
     * @return lista de objetos JugadorVO
     */
    public List<JugadorVO> listarUsuarios() {
        List<JugadorVO> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Statement stmt = conexionBD.getConexion().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                JugadorVO jugador = new JugadorVO(
                        rs.getString("nombre"),
                        rs.getString("contraseña"),
                        rs.getDouble("puntaje"),
                        rs.getDouble("tiempo")
                );
                usuarios.add(jugador);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Verifica si un usuario existe en la base de datos.
     *
     * @param nombre nombre del usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existeUsuario(String nombre) {
        String sql = "SELECT COUNT(*) as total FROM usuarios WHERE nombre = ?";

        try (PreparedStatement pstmt = conexionBD.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                rs.close();
                return total > 0;
            }
            rs.close();

        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de usuario: " + e.getMessage());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Busca un jugador en la base de datos a partir de su nombre de usuario.
     * </p>
     *
     * <p>
     * Este método está pendiente de implementación. Actualmente lanza una
     * excepción {@link UnsupportedOperationException}.
     * </p>
     *
     * @param usuario nombre de usuario del jugador que se desea buscar.
     * @return un objeto {@link JugadorVO} con la información del jugador
     * encontrado, o {@code null} si no existe.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     * @throws UnsupportedOperationException siempre que el método no esté
     * implementado.
     */
    @Override
    public JugadorVO buscarPorUsuario(String usuario) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Método no implementado
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Crea un nuevo registro de jugador en la base de datos con la información
     * contenida en el objeto {@link JugadorVO}.
     * </p>
     *
     * <p>
     * Este método aún no está implementado.
     * </p>
     *
     * @param jugador objeto con los datos del jugador a registrar.
     * @return {@code true} si la operación se completa con éxito; {@code false}
     * en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la operación SQL.
     * @throws UnsupportedOperationException siempre que el método no esté
     * implementado.
     */
    @Override
    public boolean crear(JugadorVO jugador) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Método no implementado
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Actualiza la información de un jugador existente en la base de datos
     * utilizando los datos contenidos en el objeto {@link JugadorVO}.
     * </p>
     *
     * <p>
     * Este método aún no está implementado.
     * </p>
     *
     * @param jugador objeto que contiene la información actualizada del
     * jugador.
     * @return {@code true} si la actualización fue exitosa; {@code false} en
     * caso contrario.
     * @throws SQLException si ocurre un error en la ejecución de la sentencia
     * SQL.
     * @throws UnsupportedOperationException siempre que el método no esté
     * implementado.
     */
    @Override
    public boolean actualizar(JugadorVO jugador) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Método no implementado
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Elimina de la base de datos al jugador identificado por su nombre de
     * usuario.
     * </p>
     *
     * <p>
     * Este método aún no está implementado.
     * </p>
     *
     * @param usuario nombre de usuario del jugador a eliminar.
     * @return {@code true} si la eliminación fue exitosa; {@code false} en caso
     * contrario.
     * @throws SQLException si ocurre un error al acceder o modificar la base de
     * datos.
     * @throws UnsupportedOperationException siempre que el método no esté
     * implementado.
     */
    @Override
    public boolean eliminar(String usuario) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Método no implementado
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Retorna una lista con todos los jugadores registrados en la base de
     * datos.
     * </p>
     *
     * <p>
     * Este método aún no está implementado.
     * </p>
     *
     * @return una lista de objetos {@link JugadorVO} con la información de
     * todos los jugadores.
     * @throws SQLException si ocurre un error al consultar la base de datos.
     * @throws UnsupportedOperationException siempre que el método no esté
     * implementado.
     */
    @Override
    public List<JugadorVO> listarTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Método no implementado
    }
}
