/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;

/**
 * Clase que representa una solicitud de autenticación del cliente al servidor.
 * 
 * <p>Esta clase encapsula las credenciales del usuario (nombre y contraseña)
 * que se envían desde el cliente al servidor para validar el acceso al sistema.
 * Implementa Serializable para poder ser transmitida a través de ObjectStreams.</p>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-09
 */
public class SolicitudAutenticacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** Nombre de usuario */
    private String usuario;
    
    /** Contraseña del usuario */
    private String contraseña;
    
    /**
     * Constructor que crea una solicitud de autenticación.
     * 
     * @param usuario nombre del usuario
     * @param contraseña contraseña del usuario
     */
    public SolicitudAutenticacion(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }
    
    /**
     * Obtiene el nombre de usuario.
     * 
     * @return nombre del usuario
     */
    public String getUsuario() {
        return usuario;
    }
    
    /**
     * Establece el nombre de usuario.
     * 
     * @param usuario nombre del usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Obtiene la contraseña.
     * 
     * @return contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }
    
    /**
     * Establece la contraseña.
     * 
     * @param contraseña contraseña del usuario
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    @Override
    public String toString() {
        return "SolicitudAutenticacion{" +
                "usuario='" + usuario + '\'' +
                '}';
    }
}
