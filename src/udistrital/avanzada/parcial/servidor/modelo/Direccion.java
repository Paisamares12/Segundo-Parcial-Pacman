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
 * <p>
 * Se incluye también una dirección {@link #NINGUNA} para representar un estado
 * inmóvil (sin movimiento).
 * </p>
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula Martínez
 * @version 1.1
 * @since 2025-11-11
 */
public enum Direccion {

    /** Desplazamiento hacia arriba (dx = 0, dy = -1). */
    ARRIBA(0, -1),

    /** Desplazamiento hacia abajo (dx = 0, dy = 1). */
    ABAJO(0, 1),

    /** Desplazamiento hacia la izquierda (dx = -1, dy = 0). */
    IZQUIERDA(-1, 0),

    /** Desplazamiento hacia la derecha (dx = 1, dy = 0). */
    DERECHA(1, 0),

    /** Sin desplazamiento (Pac-Man detenido). */
    NINGUNA(0, 0);

    private final int dx;
    private final int dy;

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

    /**
     * Convierte una cadena de texto en la dirección correspondiente.
     * Si el valor no coincide con ninguna, devuelve {@link #NINGUNA}.
     *
     * @param valor texto con el nombre de la dirección (por ejemplo "ARRIBA")
     * @return dirección equivalente o {@code NINGUNA} si no coincide
     */
    public static Direccion desdeTexto(String valor) {
        if (valor == null) return NINGUNA;
        try {
            return Direccion.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NINGUNA;
        }
    }
}
