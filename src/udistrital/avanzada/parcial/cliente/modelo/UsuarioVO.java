/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.cliente.modelo;

import java.io.Serializable;

/**
 * Clase principal Abstracta para representar las características básicas de un usuario
 * 
 * @author Juan Estevan Ariza Orti
 * @version 1.1
 * @since 2025-11-09
 */
public abstract class UsuarioVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String contraseña;
    private double puntaje;
    private double tiempo;

    /**
     * Constructor completo para crear un usuario con todos sus atributos.
     * 
     * @param nombre nombre del usuario
     * @param contraseña contraseña del usuario
     * @param puntaje puntaje acumulado
     * @param tiempo tiempo total de juego
     */
    public UsuarioVO(String nombre, String contraseña, double puntaje, double tiempo) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.puntaje = puntaje;
        this.tiempo = tiempo;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param nombre nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param contraseña nueva contraseña del usuario
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Obtiene el puntaje del usuario.
     * 
     * @return puntaje acumulado
     */
    public double getPuntaje() {
        return puntaje;
    }

    /**
     * Establece el puntaje del usuario.
     * 
     * @param puntaje nuevo puntaje
     */
    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * Obtiene el tiempo de juego del usuario.
     * 
     * @return tiempo total de juego
     */
    public double getTiempo() {
        return tiempo;
    }

    /**
     * Establece el tiempo de juego del usuario.
     * 
     * @param tiempo nuevo tiempo de juego
     */
    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }
    
    @Override
    public String toString() {
        return "UsuarioVO{" +
                "nombre='" + nombre + '\'' +
                ", puntaje=" + puntaje +
                ", tiempo=" + tiempo +
                '}';
    }
}