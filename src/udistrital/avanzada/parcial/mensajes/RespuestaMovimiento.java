/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;

/**
 * Mensaje de respuesta que el servidor envía al cliente después de procesar
 * un comando de movimiento.
 *
 * <p>Contiene toda la información necesaria para que el cliente actualice su
 * estado local y muestre retroalimentación al jugador.</p>
 *
 * <h3>Información incluida:</h3>
 * <ul>
 *   <li>Coordenadas actuales de Pac-Man</li>
 *   <li>Puntaje acumulado</li>
 *   <li>Indicador de colisión con pared</li>
 *   <li>Indicador de fruta comida</li>
 *   <li>Estado del juego (terminado o en progreso)</li>
 * </ul>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-11
 */
public class RespuestaMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Coordenada X actual de Pac-Man */
    private final int pacmanX;

    /** Coordenada Y actual de Pac-Man */
    private final int pacmanY;

    /** Puntaje acumulado */
    private final int puntaje;

    /** Indica si Pac-Man chocó contra una pared en este movimiento */
    private final boolean chocoConPared;

    /** Indica si Pac-Man comió una fruta en este movimiento */
    private final boolean comioFruta;

    /** Puntos ganados en este movimiento (0 si no comió fruta) */
    private final int puntosGanados;

    /** Indica si el juego ha terminado (todas las frutas comidas) */
    private final boolean juegoTerminado;

    /** Cantidad de frutas restantes */
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
    public int getPacmanX() { return pacmanX; }
    public int getPacmanY() { return pacmanY; }
    public int getPuntaje() { return puntaje; }
    public boolean isChocoConPared() { return chocoConPared; }
    public boolean isComioFruta() { return comioFruta; }
    public int getPuntosGanados() { return puntosGanados; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public int getFrutasRestantes() { return frutasRestantes; }

    @Override
    public String toString() {
        return "RespuestaMovimiento{" +
                "pos=(" + pacmanX + "," + pacmanY + ")" +
                ", puntaje=" + puntaje +
                ", chocoConPared=" + chocoConPared +
                ", comioFruta=" + comioFruta +
                ", puntosGanados=" + puntosGanados +
                ", juegoTerminado=" + juegoTerminado +
                ", frutasRestantes=" + frutasRestantes +
                '}';
    }
}