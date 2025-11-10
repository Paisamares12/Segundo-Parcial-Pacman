/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.cliente.modelo;

/**
 * Clase concreta que representa un jugador del sistema.
 * 
 * <p>Esta clase extiende de {@link UsuarioVO} y proporciona una implementación
 * concreta para usuarios de tipo jugador en el sistema Pac-Man distribuido.</p>
 * 
 * @author Juan Estevan Ariza Orti
 * @version 1.0
 * @since 2025-11-09
 */
public class JugadorVO extends UsuarioVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor completo para crear un jugador con todos sus atributos.
     * 
     * @param nombre nombre del jugador
     * @param contraseña contraseña del jugador
     * @param puntaje puntaje acumulado
     * @param tiempo tiempo total de juego
     */
    public JugadorVO(String nombre, String contraseña, double puntaje, double tiempo) {
        super(nombre, contraseña, puntaje, tiempo);
    }
    
    /**
     * Constructor simplificado para crear un jugador con valores predeterminados.
     * 
     * <p>Inicializa el puntaje y tiempo en 0.</p>
     * 
     * @param nombre nombre del jugador
     * @param contraseña contraseña del jugador
     */
    public JugadorVO(String nombre, String contraseña) {
        super(nombre, contraseña, 0.0, 0.0);
    }
    
    /**
     * Constructor por defecto sin parámetros.
     * Inicializa todos los valores en valores por defecto.
     */
    public JugadorVO() {
        super("", "", 0.0, 0.0);
    }
    
    @Override
    public String toString() {
        return "JugadorVO{" +
                "nombre='" + getNombre() + '\'' +
                ", puntaje=" + getPuntaje() +
                ", tiempo=" + getTiempo() +
                '}';
    }
}
