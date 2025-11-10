/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.control;

import udistrital.avanzada.parcial.mensajes.SolicitudAutenticacion;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;
import udistrital.avanzada.parcial.servidor.servicios.IAutenticacionService;

/**
 * Controlador que maneja las solicitudes de autenticación.
 * 
 * <p>Este controlador actúa como intermediario entre la capa de presentación
 * (red/socket) y la capa de servicio. Se encarga de coordinar las operaciones
 * de autenticación sin contener lógica de negocio.</p>
 * 
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Recibir solicitudes de autenticación</li>
 *   <li>Delegar la validación al servicio correspondiente</li>
 *   <li>Retornar las respuestas apropiadas</li>
 *   <li>Logging de eventos de autenticación</li>
 * </ul>
 * 
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo coordina autenticación</li>
 *   <li><b>D - Dependency Inversion:</b> Depende de IAutenticacionService</li>
 * </ul>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-10
 */
public class AutenticacionController {
    
    /** Servicio de autenticación (inyección de dependencia) */
    private final IAutenticacionService autenticacionService;
    
    /**
     * Constructor con inyección de dependencias.
     * 
     * @param autenticacionService servicio de autenticación a utilizar
     */
    public AutenticacionController(IAutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }
    
    /**
     * Procesa una solicitud de autenticación.
     * 
     * <p>Este método coordina el flujo de autenticación:</p>
     * <ol>
     *   <li>Recibe la solicitud</li>
     *   <li>Extrae las credenciales</li>
     *   <li>Delega al servicio</li>
     *   <li>Registra el resultado</li>
     *   <li>Retorna la respuesta</li>
     * </ol>
     * 
     * @param solicitud solicitud de autenticación del cliente
     * @return respuesta de autenticación (éxito o fallo)
     */
    public RespuestaAutenticacion procesarAutenticacion(SolicitudAutenticacion solicitud) {
        if (solicitud == null) {
            System.err.println("Solicitud de autenticación nula recibida");
            return new RespuestaAutenticacion(false, "Solicitud inválida");
        }
        
        String usuario = solicitud.getUsuario();
        String contraseña = solicitud.getContraseña();
        
        System.out.println("Procesando autenticación para usuario: " + usuario);
        
        // Delegar al servicio
        RespuestaAutenticacion respuesta = autenticacionService.autenticar(usuario, contraseña);
        
        // Logging del resultado
        if (respuesta.isExitosa()) {
            System.out.println("✓ Autenticación exitosa: " + usuario);
        } else {
            System.out.println("✗ Autenticación fallida: " + usuario + " - " + respuesta.getMensaje());
        }
        
        return respuesta;
    }
    
    /**
     * Verifica si un usuario está activo.
     * 
     * @param usuario nombre de usuario a verificar
     * @return true si el usuario está activo, false en caso contrario
     */
    public boolean verificarUsuarioActivo(String usuario) {
        return autenticacionService.verificarUsuarioActivo(usuario);
    }
}
