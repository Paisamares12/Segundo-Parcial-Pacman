/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.servicios;

import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;

/**
 * Interfaz que define el contrato para el servicio de autenticación.
 * 
 * <p>Esta interfaz abstrae la lógica de negocio relacionada con la autenticación,
 * permitiendo diferentes implementaciones y facilitando el testing.</p>
 * 
 * <p>Cumple con:</p>
 * <ul>
 *   <li><b>I - Interface Segregation:</b> Interfaz específica para autenticación</li>
 *   <li><b>D - Dependency Inversion:</b> Los controladores dependen de esta abstracción</li>
 * </ul>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-10
 */
public interface IAutenticacionService {
    
    /**
     * Autentica un usuario con sus credenciales.
     * 
     * <p>Este método encapsula toda la lógica de negocio relacionada con
     * la autenticación, incluyendo validación de credenciales, generación
     * de respuestas y cualquier otra regla de negocio.</p>
     * 
     * @param usuario nombre de usuario
     * @param contraseña contraseña del usuario
     * @return RespuestaAutenticacion con el resultado de la autenticación
     */
    RespuestaAutenticacion autenticar(String usuario, String contraseña);
    
    
    /**
     * Verifica si un usuario está activo en el sistema.
     * 
     * @param usuario nombre de usuario
     * @return true si el usuario está activo, false en caso contrario
     */
    boolean verificarUsuarioActivo(String usuario);
}
