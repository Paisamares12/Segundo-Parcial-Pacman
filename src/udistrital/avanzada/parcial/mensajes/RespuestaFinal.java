package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;
import java.util.List;

/**
 * Mensaje final enviado por el servidor al cliente cuando termina el juego.
 *
 * <p>
 * Contiene toda la información de la partida completada para que el cliente
 * pueda guardarla y mostrar el ranking.</p>
 *
 * <p>
 * Modificado por Paula Martínez.
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-11
 */
public class RespuestaFinal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nombre del jugador
     */
    private final String nombreJugador;

    /**
     * Puntaje total obtenido
     */
    private final int puntajeTotal;

    /**
     * Tiempo en milisegundos
     */
    private final long tiempoMs;

    /**
     * Lista de nombres de frutas comidas
     */
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

    /**
     * Obtiene el nombre del jugador asociado al registro.
     *
     * @return nombre del jugador como {@code String}.
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     * Retorna el puntaje total acumulado por el jugador.
     *
     * <p>
     * Este valor representa la suma de los puntos obtenidos durante la partida.
     * </p>
     *
     * @return puntaje total del jugador como un {@code int}.
     */
    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    /**
     * Devuelve el tiempo total que el jugador tardó en completar la partida.
     *
     * <p>
     * El tiempo se expresa en milisegundos desde el inicio hasta la
     * finalización de la sesión.
     * </p>
     *
     * @return duración de la partida en milisegundos.
     */
    public long getTiempoMs() {
        return tiempoMs;
    }

    /**
     * Obtiene la lista de frutas que el jugador ha comido durante la partida.
     *
     * <p>
     * Cada elemento de la lista corresponde al nombre o tipo de fruta
     * recolectada.
     * </p>
     *
     * @return lista de frutas comidas como {@code List<String>}.
     */
    public List<String> getFrutasComidas() {
        return frutasComidas;
    }
}
