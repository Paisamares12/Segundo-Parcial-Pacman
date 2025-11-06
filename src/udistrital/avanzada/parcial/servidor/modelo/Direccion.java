package udistrital.avanzada.parcial.servidor.modelo;

/**
 * Enumeración que define las direcciones cardinales de movimiento de Pac-Man
 * dentro del tablero de juego.
 *
 * <p>
 * Cada dirección expone un desplazamiento unitario en X ({@link #dx()}) y en Y
 * ({@link #dy()}), que el controlador multiplica por el paso configurado (por
 * ejemplo, 4 píxeles por comando).
 * </p>
 *
 * <p>
 * Convención de coordenadas en Swing/AWT: el eje X crece hacia la derecha y el
 * eje Y hacia abajo; por ello, {@code ARRIBA} implica {@code dy = -1} y
 * {@code ABAJO} implica {@code dy = 1}.
 * </p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public enum Direccion {

    /**
     * Desplazamiento hacia arriba (dx = 0, dy = -1).
     */
    ARRIBA(0, -1),
    /**
     * Desplazamiento hacia abajo (dx = 0, dy = 1).
     */
    ABAJO(0, 1),
    /**
     * Desplazamiento hacia la izquierda (dx = -1, dy = 0).
     */
    IZQUIERDA(-1, 0),
    /**
     * Desplazamiento hacia la derecha (dx = 1, dy = 0).
     */
    DERECHA(1, 0);

    private final int dx;
    private final int dy;

    /**
     * Crea una dirección con sus incrementos unitarios asociados.
     *
     * @param dx incremento en X (positivo a la derecha, negativo a la
     * izquierda)
     * @param dy incremento en Y (positivo hacia abajo, negativo hacia arriba)
     */
    Direccion(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Devuelve el incremento unitario sobre el eje X.
     *
     * @return delta X (−1, 0 o 1)
     */
    public int dx() {
        return dx;
    }

    /**
     * Devuelve el incremento unitario sobre el eje Y.
     *
     * @return delta Y (−1, 0 o 1)
     */
    public int dy() {
        return dy;
    }
}
