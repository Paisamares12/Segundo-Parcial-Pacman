package udistrital.avanzada.parcial.modelo;

/**
 * Entidad principal controlada por el jugador dentro del juego.
 *
 * <p>
 * Su movimiento es gestionado por el controlador de lógica, y sus coordenadas
 * se actualizan de acuerdo con los límites del tablero.
 * </p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public class Pacman {

    private Posicion posicion;

    /**
     * Crea un Pac-Man en la posición inicial por defecto (100, 100).
     */
    public Pacman() {
        this.posicion = new Posicion(100, 100);
    }

    /**
     * @return posición actual de Pac-Man.
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /**
     * @param posicion nueva posición de Pac-Man.
     */
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}
