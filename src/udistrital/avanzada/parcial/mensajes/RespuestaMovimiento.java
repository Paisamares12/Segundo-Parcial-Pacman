package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;

/**
 * Mensaje de respuesta que el servidor envía al cliente después de procesar un
 * comando de movimiento.
 *
 * <p>
 * Contiene toda la información necesaria para que el cliente actualice su
 * estado local y muestre retroalimentación al jugador.</p>
 *
 * <h3>Información incluida:</h3>
 * <ul>
 * <li>Coordenadas actuales de Pac-Man</li>
 * <li>Puntaje acumulado</li>
 * <li>Indicador de colisión con pared</li>
 * <li>Indicador de fruta comida</li>
 * <li>Estado del juego (terminado o en progreso)</li>
 * </ul>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-11
 */
public class RespuestaMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Coordenada X actual de Pac-Man
     */
    private final int pacmanX;

    /**
     * Coordenada Y actual de Pac-Man
     */
    private final int pacmanY;

    /**
     * Puntaje acumulado
     */
    private final int puntaje;

    /**
     * Indica si Pac-Man chocó contra una pared en este movimiento
     */
    private final boolean chocoConPared;

    /**
     * Indica si Pac-Man comió una fruta en este movimiento
     */
    private final boolean comioFruta;

    /**
     * Puntos ganados en este movimiento (0 si no comió fruta)
     */
    private final int puntosGanados;

    /**
     * Indica si el juego ha terminado (todas las frutas comidas)
     */
    private final boolean juegoTerminado;

    /**
     * Cantidad de frutas restantes
     */
    private final int frutasRestantes;

    /**
     * Constructor completo para crear una respuesta de movimiento.
     *
     * @param pacmanX coordenada X de Pac-Man
     * @param pacmanY coordenada Y de Pac-Man
     * @param puntaje puntaje total acumulado
     * @param chocoConPared true si hubo colisión con pared
     * @param comioFruta true si comió una fruta
     * @param puntosGanados puntos ganados en este turno
     * @param juegoTerminado true si el juego terminó
     * @param frutasRestantes cantidad de frutas sin comer
     */
    public RespuestaMovimiento(int pacmanX, int pacmanY, int puntaje,
            boolean chocoConPared, boolean comioFruta,
            int puntosGanados, boolean juegoTerminado,
            int frutasRestantes) {
        this.pacmanX = pacmanX;
        this.pacmanY = pacmanY;
        this.puntaje = puntaje;
        this.chocoConPared = chocoConPared;
        this.comioFruta = comioFruta;
        this.puntosGanados = puntosGanados;
        this.juegoTerminado = juegoTerminado;
        this.frutasRestantes = frutasRestantes;
    }

    // Getters
    /**
     * Obtiene la posición horizontal actual de Pacman en el mapa.
     *
     * @return coordenada X de Pacman como un valor entero.
     */
    public int getPacmanX() {
        return pacmanX;
    }

    /**
     * Obtiene la posición vertical actual de Pacman en el mapa.
     *
     * @return coordenada Y de Pacman como un valor entero.
     */
    public int getPacmanY() {
        return pacmanY;
    }

    /**
     * Retorna el puntaje actual acumulado por el jugador.
     *
     * <p>
     * Este valor puede incrementarse conforme Pacman coma frutas u otros ítems.
     * </p>
     *
     * @return puntaje actual del jugador.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Indica si Pacman ha chocado contra una pared tras su último movimiento.
     *
     * @return {@code true} si hubo colisión con una pared; {@code false} en
     * caso contrario.
     */
    public boolean isChocoConPared() {
        return chocoConPared;
    }

    /**
     * Determina si Pacman ha comido una fruta durante su último movimiento.
     *
     * @return {@code true} si Pacman comió una fruta; {@code false} si no.
     */
    public boolean isComioFruta() {
        return comioFruta;
    }

    /**
     * Devuelve la cantidad de puntos ganados por Pacman en el último movimiento
     * exitoso.
     *
     * <p>
     * Este valor suele corresponder al puntaje otorgado por una fruta o evento
     * especial.
     * </p>
     *
     * @return puntos ganados recientemente.
     */
    public int getPuntosGanados() {
        return puntosGanados;
    }

    /**
     * Indica si la partida ha finalizado.
     *
     * <p>
     * Puede ser {@code true} si el jugador ganó, perdió o se completaron las
     * condiciones de fin del juego.
     * </p>
     *
     * @return {@code true} si el juego terminó; {@code false} en caso
     * contrario.
     */
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    /**
     * Obtiene la cantidad de frutas que aún permanecen en el tablero.
     *
     * @return número de frutas restantes por comer.
     */
    public int getFrutasRestantes() {
        return frutasRestantes;
    }
}
