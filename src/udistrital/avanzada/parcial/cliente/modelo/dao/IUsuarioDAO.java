package udistrital.avanzada.parcial.cliente.modelo.dao;

import udistrital.avanzada.parcial.cliente.modelo.JugadorVO;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos de usuarios.
 *
 * <p>
 * Esta interfaz permite la inyección de dependencias y facilita el testing
 * mediante mocks. Cumple con el principio de Inversión de Dependencias (D de
 * SOLID).</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-10
 */
public interface IUsuarioDAO {

    /**
     * Valida las credenciales de un usuario.
     *
     * @param usuario nombre de usuario
     * @param contraseña contraseña del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     * @throws SQLException si ocurre un error de base de datos
     */
    boolean validarCredenciales(String usuario, String contraseña) throws SQLException;

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param usuario nombre de usuario
     * @return el JugadorVO si existe, null en caso contrario
     * @throws SQLException si ocurre un error de base de datos
     */
    JugadorVO buscarPorUsuario(String usuario) throws SQLException;

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param jugador datos del jugador a crear
     * @return true si se creó correctamente, false en caso contrario
     * @throws SQLException si ocurre un error de base de datos
     */
    boolean crear(JugadorVO jugador) throws SQLException;

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param jugador datos actualizados del jugador
     * @return true si se actualizó correctamente, false en caso contrario
     * @throws SQLException si ocurre un error de base de datos
     */
    boolean actualizar(JugadorVO jugador) throws SQLException;

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param usuario nombre de usuario a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws SQLException si ocurre un error de base de datos
     */
    boolean eliminar(String usuario) throws SQLException;

    /**
     * Obtiene la lista de todos los usuarios.
     *
     * @return lista de todos los jugadores
     * @throws SQLException si ocurre un error de base de datos
     */
    List<JugadorVO> listarTodos() throws SQLException;
}
