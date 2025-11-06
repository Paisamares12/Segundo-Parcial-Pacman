package udistrital.avanzada.parcial.modelo;

/**
 * Representa una fruta en el tablero de Pac-Man.
 *
 * <p>
 * Cada fruta tiene un tipo, una posición y un estado que indica si ha sido
 * comida.
 * </p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public class Fruta {

    private final TipoFruta tipo;
    private Posicion posicion;
    private boolean comida;

    /**
     * Crea una fruta del tipo especificado en la posición dada.
     *
     * @param tipo tipo de fruta (no nulo)
     * @param posicion posición inicial de la fruta
     */
    public Fruta(TipoFruta tipo, Posicion posicion) {
        this.tipo = tipo;
        this.posicion = posicion;
        this.comida = false;
    }

    /**
     * @return tipo de fruta.
     */
    public TipoFruta getTipo() {
        return tipo;
    }

    /**
     * @return posición actual de la fruta.
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /**
     * @param posicion nueva posición de la fruta.
     */
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    /**
     * @return {@code true} si la fruta ya fue comida.
     */
    public boolean isComida() {
        return comida;
    }

    /**
     * Marca la fruta como comida.
     */
    public void comer() {
        this.comida = true;
    }
}
