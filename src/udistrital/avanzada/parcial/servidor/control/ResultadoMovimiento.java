/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.control;

/**
 * Clase auxiliar que encapsula el resultado de procesar un movimiento
 * en el controlador de juego.
 *
 * <p>Usado internamente por {@link ControlJuego} para comunicar al
 * {@link ManejadorCliente} qué ocurrió durante la actualización.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-11
 */
public class ResultadoMovimiento {

    /** Indica si hubo colisión con pared */
    private final boolean chocoConPared;

    /** Cantidad de frutas comidas en este movimiento */
    private final int frutasComidas;

    /** Puntos ganados en este movimiento */
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

    public boolean isChocoConPared() {
        return chocoConPared;
    }

    public int getFrutasComidas() {
        return frutasComidas;
    }

    public int getPuntosGanados() {
        return puntosGanados;
    }

    @Override
    public String toString() {
        return "ResultadoMovimiento{" +
                "chocoConPared=" + chocoConPared +
                ", frutasComidas=" + frutasComidas +
                ", puntosGanados=" + puntosGanados +
                '}';
    }
}
