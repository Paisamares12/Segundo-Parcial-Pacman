package udistrital.avanzada.parcial.servidor.modelo;

/**
 * Define los límites rectangulares del área de juego.
 *
 * <p>
 * Se usa para validar posiciones y restringir el movimiento de Pac-Man y las
 * frutas.
 * </p>
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-06
 */
public class LimitesTablero {

    private final int minX, minY, maxX, maxY;

    /**
     * Crea un rectángulo que representa los límites del tablero.
     *
     * @param minX valor mínimo del eje X
     * @param minY valor mínimo del eje Y
     * @param maxX valor máximo del eje X
     * @param maxY valor máximo del eje Y
     */
    public LimitesTablero(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Retorna el valor mínimo permitido en el eje X del tablero.
     *
     * <p>
     * Corresponde al límite izquierdo del área de juego. Cualquier posición con
     * una coordenada X menor que este valor se considera fuera de los límites
     * válidos.
     * </p>
     *
     * @return coordenada mínima X (en píxeles)
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Retorna el valor mínimo permitido en el eje Y del tablero.
     *
     * <p>
     * Representa el límite superior del área de juego. Cualquier posición con
     * una coordenada Y menor que este valor se considera fuera del área válida.
     * </p>
     *
     * @return coordenada mínima Y (en píxeles)
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Retorna el valor máximo permitido en el eje X del tablero.
     *
     * <p>
     * Define el borde derecho del área de juego. Cualquier posición con una
     * coordenada X mayor que este valor excede los límites válidos del tablero.
     * </p>
     *
     * @return coordenada máxima X (en píxeles)
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Retorna el valor máximo permitido en el eje Y del tablero.
     *
     * <p>
     * Define el borde inferior del área de juego. Cualquier posición con una
     * coordenada Y mayor que este valor se considera fuera de los límites del
     * tablero.
     * </p>
     *
     * @return coordenada máxima Y (en píxeles)
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Verifica si una posición está dentro de los límites del tablero.
     *
     * @param p posición a verificar
     * @return {@code true} si la posición está dentro, de lo contrario
     * {@code false}
     */
    public boolean contiene(Posicion p) {
        return p.getX() >= minX && p.getX() <= maxX
                && p.getY() >= minY && p.getY() <= maxY;
    }
}
