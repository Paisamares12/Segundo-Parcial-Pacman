package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;

/**
 * Objeto de transferencia enviado por el servidor al cliente para actualizar la
 * vista del tablero (modo espectador).
 *
 * Contiene solo datos simples, sin referencias a clases del modelo.
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 *
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-09
 */
public class SnapshotTablero implements Serializable {

    private static final long serialVersionUID = 1L;

    // Coordenadas de Pac-Man
    private int pacmanX;
    private int pacmanY;

    // Límites del tablero
    private int limiteMinX;
    private int limiteMinY;
    private int limiteMaxX;
    private int limiteMaxY;

    // Información de frutas (arrays paralelos)
    private int[] frutaX;
    private int[] frutaY;
    private boolean[] frutaComida;

    // Puntaje actual
    private int puntaje;

    // Dirección textual del Pac-Man ("ARRIBA", "ABAJO", etc.)
    private String direccionPacman;

    // --- Métodos de acceso (getters y setters) ---
    /**
     * Obtiene la posición horizontal actual de Pacman en el tablero.
     *
     * @return coordenada X de Pacman.
     */
    public int getPacmanX() {
        return pacmanX;
    }

    /**
     * Obtiene la posición vertical actual de Pacman en el tablero.
     *
     * @return coordenada Y de Pacman.
     */
    public int getPacmanY() {
        return pacmanY;
    }

    /**
     * Establece la posición horizontal (X) de Pacman.
     *
     * @param pacmanX nueva coordenada X de Pacman.
     */
    public void setPacmanX(int pacmanX) {
        this.pacmanX = pacmanX;
    }

    /**
     * Establece la posición vertical (Y) de Pacman.
     *
     * @param pacmanY nueva coordenada Y de Pacman.
     */
    public void setPacmanY(int pacmanY) {
        this.pacmanY = pacmanY;
    }

    /**
     * Devuelve el límite mínimo permitido en el eje X.
     *
     * @return coordenada mínima X dentro del mapa.
     */
    public int getLimiteMinX() {
        return limiteMinX;
    }

    /**
     * Devuelve el límite mínimo permitido en el eje Y.
     *
     * @return coordenada mínima Y dentro del mapa.
     */
    public int getLimiteMinY() {
        return limiteMinY;
    }

    /**
     * Devuelve el límite máximo permitido en el eje X.
     *
     * @return coordenada máxima X dentro del mapa.
     */
    public int getLimiteMaxX() {
        return limiteMaxX;
    }

    /**
     * Devuelve el límite máximo permitido en el eje Y.
     *
     * @return coordenada máxima Y dentro del mapa.
     */
    public int getLimiteMaxY() {
        return limiteMaxY;
    }

    /**
     * Define los límites del tablero en ambos ejes.
     *
     * <p>
     * Estos valores se usan para evitar que Pacman se desplace fuera del área
     * de juego.
     * </p>
     *
     * @param minX límite mínimo en X.
     * @param minY límite mínimo en Y.
     * @param maxX límite máximo en X.
     * @param maxY límite máximo en Y.
     */
    public void setLimites(int minX, int minY, int maxX, int maxY) {
        this.limiteMinX = minX;
        this.limiteMinY = minY;
        this.limiteMaxX = maxX;
        this.limiteMaxY = maxY;
    }

    /**
     * Obtiene el puntaje actual acumulado por el jugador.
     *
     * @return puntaje de Pacman.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Establece el puntaje actual del jugador.
     *
     * @param puntaje nuevo puntaje total.
     */
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * Configura las posiciones y el estado de las frutas del tablero.
     *
     * <p>
     * Cada arreglo debe tener el mismo tamaño, donde el índice representa la
     * misma fruta:
     * <ul>
     * <li>{@code x[i]} → coordenada X de la fruta i.</li>
     * <li>{@code y[i]} → coordenada Y de la fruta i.</li>
     * <li>{@code comida[i]} → indica si la fruta fue comida.</li>
     * </ul>
     * </p>
     *
     * @param x coordenadas X de las frutas.
     * @param y coordenadas Y de las frutas.
     * @param comida arreglo de banderas que indica si cada fruta ha sido
     * comida.
     */
    public void setFrutas(int[] x, int[] y, boolean[] comida) {
        this.frutaX = x;
        this.frutaY = y;
        this.frutaComida = comida;
    }

    /**
     * Obtiene la cantidad total de frutas actualmente en el tablero.
     *
     * @return número de frutas disponibles; 0 si no hay ninguna configurada.
     */
    public int getNumFrutas() {
        return frutaX == null ? 0 : frutaX.length;
    }

    /**
     * Retorna la coordenada X de una fruta específica.
     *
     * @param i índice de la fruta.
     * @return coordenada X de la fruta.
     */
    public int getFrutaX(int i) {
        return frutaX[i];
    }

    /**
     * Retorna la coordenada Y de una fruta específica.
     *
     * @param i índice de la fruta.
     * @return coordenada Y de la fruta.
     */
    public int getFrutaY(int i) {
        return frutaY[i];
    }

    /**
     * Indica si una fruta en particular ya ha sido comida.
     *
     * @param i índice de la fruta a consultar.
     * @return {@code true} si la fruta fue comida; {@code false} si sigue
     * disponible.
     */
    public boolean isFrutaComida(int i) {
        return frutaComida[i];
    }

    /**
     * Devuelve la dirección actual de movimiento de Pacman.
     *
     * <p>
     * Normalmente este valor será uno de: {@code "ARRIBA"}, {@code "ABAJO"},
     * {@code "IZQUIERDA"} o {@code "DERECHA"}.
     * </p>
     *
     * @return dirección de movimiento de Pacman.
     */
    public String getDireccionPacman() {
        return direccionPacman;
    }

    /**
     * Establece la dirección actual de movimiento de Pacman.
     *
     * @param direccionPacman nueva dirección (por ejemplo, {@code "ARRIBA"}).
     */
    public void setDireccionPacman(String direccionPacman) {
        this.direccionPacman = direccionPacman;
    }

}
