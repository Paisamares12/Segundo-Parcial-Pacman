package udistrital.avanzada.parcial.servidor.modelo;

/**
 * Representa una posición discreta (x, y) en el tablero de juego.
 *
 * <p>
 * Es una clase mutable que permite mover o ajustar coordenadas según límites.
 * </p>
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-06
 */
public class Posicion {

    private int x;
    private int y;

    /**
     * Crea una posición con las coordenadas iniciales indicadas.
     *
     * @param x posición en el eje X
     * @param y posición en el eje Y
     */
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return coordenada X actual.
     */
    public int getX() {
        return x;
    }

    /**
     * @return coordenada Y actual.
     */
    public int getY() {
        return y;
    }

    /**
     * @param x nueva coordenada X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y nueva coordenada Y
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
