/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.servicios;

import udistrital.avanzada.parcial.cliente.modelo.dao.IUsuarioDAO;
import udistrital.avanzada.parcial.cliente.modelo.JugadorVO;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;
import java.sql.SQLException;

/**
 * Implementación del servicio de autenticación.
 * 
 * <p>Esta clase contiene toda la lógica de negocio relacionada con
 * la autenticación de usuarios. Actúa como la capa de servicio en
 * la arquitectura, separando la lógica de negocio del acceso a datos
 * y de la presentación.</p>
 * 
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo maneja lógica de autenticación</li>
 *   <li><b>O - Open/Closed:</b> Abierto a extensión mediante herencia</li>
 *   <li><b>D - Dependency Inversion:</b> Depende de IUsuarioDAO (abstracción)</li>
 * </ul>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-10
 */
public class AutenticacionService implements IAutenticacionService {
    
    /** DAO para acceso a datos de usuarios (inyección de dependencia) */
    private final IUsuarioDAO usuarioDAO;
    
    /**
     * Constructor con inyección de dependencias.
     * 
     * <p>Recibe la implementación del DAO a través del constructor,
     * permitiendo la inversión de dependencias y facilitando el testing.</p>
     * 
     * @param usuarioDAO implementación del DAO de usuarios
     */
    public AutenticacionService(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Implementa la lógica de negocio de autenticación:</p>
     * <ul>
     *   <li>Valida que los parámetros no sean nulos o vacíos</li>
     *   <li>Consulta las credenciales en la base de datos</li>
     *   <li>Genera mensajes apropiados según el resultado</li>
     *   <li>Maneja excepciones de base de datos</li>
     * </ul>
     */
    @Override
    public RespuestaAutenticacion autenticar(String usuario, String contraseña) {
        // Validación de parámetros
        if (usuario == null || usuario.trim().isEmpty()) {
            return new RespuestaAutenticacion(false, "El nombre de usuario no puede estar vacío");
        }
        
        if (contraseña == null || contraseña.trim().isEmpty()) {
            return new RespuestaAutenticacion(false, "La contraseña no puede estar vacía");
        }
        
        try {
            // Validar credenciales usando el DAO
            boolean credencialesValidas = usuarioDAO.validarCredenciales(usuario, contraseña);
            
            if (credencialesValidas) {
                return new RespuestaAutenticacion(true, "Bienvenido " + usuario + "!");
            } else {
                return new RespuestaAutenticacion(false, "Credenciales inválidas. Acceso denegado.");
            }
            
        } catch (SQLException e) {
            // Manejo de errores de base de datos
            System.err.println("Error de base de datos durante autenticación: " + e.getMessage());
            return new RespuestaAutenticacion(false, "Error del servidor. Intente nuevamente.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verificarUsuarioActivo(String usuario) {
        try {
            JugadorVO jugador = usuarioDAO.buscarPorUsuario(usuario);
            return jugador != null;
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario activo: " + e.getMessage());
            return false;
        }
    }
}
