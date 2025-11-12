/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;
import java.util.List;

/**
 * Mensaje final enviado por el servidor al cliente cuando termina el juego.
 *
 * <p>Contiene toda la información de la partida completada para que el cliente
 * pueda guardarla y mostrar el ranking.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-11
 */
public class RespuestaFinal implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nombre del jugador */
    private final String nombreJugador;

    /** Puntaje total obtenido */
    private final int puntajeTotal;

    /** Tiempo en milisegundos */
    private final long tiempoMs;

    /** Lista de nombres de frutas comidas */
    private final List<String> frutasComidas;

    /**
     * Constructor completo.
     *
     * @param nombreJugador nombre del jugador
     * @param puntajeTotal puntaje total
     * @param tiempoMs tiempo en milisegundos
     * @param frutasComidas lista de frutas comidas
     */
    public RespuestaFinal(String nombreJugador, int puntajeTotal, long tiempoMs, List<String> frutasComidas) {
        this.nombreJugador = nombreJugador;
        this.puntajeTotal = puntajeTotal;
        this.tiempoMs = tiempoMs;
        this.frutasComidas = frutasComidas;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public long getTiempoMs() {
        return tiempoMs;
    }

    public List<String> getFrutasComidas() {
        return frutasComidas;
    }

    @Override
    public String toString() {
        return "RespuestaFinal{" +
                "nombreJugador='" + nombreJugador + '\'' +
                ", puntajeTotal=" + puntajeTotal +
                ", tiempoMs=" + tiempoMs +
                ", frutasComidas=" + frutasComidas +
                '}';
    }
}
