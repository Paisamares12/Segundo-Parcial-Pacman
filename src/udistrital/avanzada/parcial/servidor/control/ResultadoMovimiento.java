package udistrital.avanzada.parcial.servidor.control;

/**
 * Clase auxiliar que encapsula el resultado de procesar un movimiento en el
 * controlador de juego.
 *
 * <p>
 * Usado internamente por {@link ControlJuego} para comunicar al
 * {@link ManejadorCliente} qué ocurrió durante la actualización.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-11
 */
public class ResultadoMovimiento {

    /**
     * Indica si hubo colisión con pared
     */
    private final boolean chocoConPared;

    /**
     * Cantidad de frutas comidas en este movimiento
     */
    private final int frutasComidas;

    /**
     * Puntos ganados en este movimiento
     */
    private final int puntosGanados;

    /**
     * Constructor que crea un resultado de movimiento.
     *
     * @param chocoConPared true si Pac-Man chocó contra una pared
     * @param frutasComidas cantidad de frutas comidas
     * @param puntosGanados puntos acumulados en este movimiento
     */
    public ResultadoMovimiento(boolean chocoConPared, int frutasComidas, int puntosGanados) {
        this.chocoConPared = chocoConPared;
        this.frutasComidas = frutasComidas;
        this.puntosGanados = puntosGanados;
    }

    /**
     * Indica si Pacman ha chocado con una pared en su último movimiento.
     *
     * <p>
     * Este valor se actualiza después de cada intento de desplazamiento. Si es
     * {@code true}, significa que el movimiento fue bloqueado por un obstáculo
     * del mapa.
     * </p>
     *
     * @return {@code true} si Pacman chocó con una pared; {@code false} en caso
     * contrario.
     */
    public boolean isChocoConPared() {
        return chocoConPared;
    }

    /**
     * Obtiene la cantidad total de frutas que Pacman ha comido hasta el
     * momento.
     *
     * <p>
     * Este valor incrementa cada vez que Pacman consume una fruta y contribuye
     * al puntaje total.
     * </p>
     *
     * @return número de frutas comidas por el jugador.
     */
    public int getFrutasComidas() {
        return frutasComidas;
    }

    /**
     * Devuelve la cantidad de puntos obtenidos en el último movimiento exitoso.
     *
     * <p>
     * Normalmente corresponde al valor asociado a una fruta u otro elemento del
     * mapa que haya sido consumido recientemente.
     * </p>
     *
     * @return puntos ganados en el movimiento más reciente.
     */
    public int getPuntosGanados() {
        return puntosGanados;
    }
}
