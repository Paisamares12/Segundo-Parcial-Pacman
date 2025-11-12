package udistrital.avanzada.parcial.cliente.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Representa el resultado de una partida para almacenamiento en archivo.
 *
 * <p>
 * Esta clase encapsula todos los datos relevantes de una partida completada y
 * puede ser serializada para guardarse en un archivo de acceso aleatorio.</p>
 * 
 * <p>
 * Modificado por Paula Martínez
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-11
 */
public class ResultadoPartida implements Serializable, Comparable<ResultadoPartida> {

    private static final long serialVersionUID = 1L;

    /**
     * Tamaño fijo del registro en bytes (para acceso aleatorio)
     */
    public static final int TAMANIO_REGISTRO = 200;

    /**
     * Nombre del jugador (máximo 50 caracteres)
     */
    private String nombreJugador;

    /**
     * Puntaje total obtenido
     */
    private int puntajeTotal;

    /**
     * Tiempo en milisegundos
     */
    private long tiempoMs;

    /**
     * Frutas comidas (como texto separado por comas)
     */
    private String frutasComidas;

    /**
     * Fecha y hora de la partida
     */
    private LocalDateTime fechaHora;

    /**
     * Ranking calculado (puntaje / tiempo en segundos)
     */
    private double ranking;

    /**
     * Constructor vacío.
     */
    public ResultadoPartida() {
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Constructor completo.
     *
     * @param nombreJugador nombre del jugador
     * @param puntajeTotal puntaje total
     * @param tiempoMs tiempo en milisegundos
     * @param frutasComidas lista de frutas
     */
    public ResultadoPartida(String nombreJugador, int puntajeTotal, long tiempoMs, List<String> frutasComidas) {
        this.nombreJugador = limitarTexto(nombreJugador, 50);
        this.puntajeTotal = puntajeTotal;
        this.tiempoMs = tiempoMs;
        this.frutasComidas = String.join(", ", frutasComidas);
        this.fechaHora = LocalDateTime.now();
        this.ranking = calcularRanking();
    }

    /**
     * Calcula el ranking (puntaje / tiempo en segundos).
     *
     * @return valor del ranking
     */
    private double calcularRanking() {
        double tiempoSegundos = tiempoMs / 1000.0;
        return tiempoSegundos > 0 ? puntajeTotal / tiempoSegundos : 0;
    }

    /**
     * Limita un texto a un número máximo de caracteres.
     *
     * @param texto texto original
     * @param maxLength longitud máxima
     * @return texto limitado
     */
    private String limitarTexto(String texto, int maxLength) {
        if (texto == null) {
            return "";
        }
        return texto.length() > maxLength ? texto.substring(0, maxLength) : texto;
    }

    /**
     * Formatea el tiempo en formato mm:ss.mmm
     *
     * @return tiempo formateado
     */
    public String getTiempoFormateado() {
        long min = tiempoMs / 60000;
        long sec = (tiempoMs % 60000) / 1000;
        long mil = tiempoMs % 1000;
        return String.format("%02d:%02d.%03d", min, sec, mil);
    }

    /**
     * Formatea la fecha en formato legible.
     *
     * @return fecha formateada
     */
    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
    }

    /**
     * Obtiene el ranking formateado con 2 decimales.
     *
     * @return ranking formateado
     */
    public String getRankingFormateado() {
        return String.format("%.2f", ranking);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Compara este resultado de partida con otro, utilizando el valor del
     * <b>ranking</b> como criterio principal de ordenación.
     * </p>
     *
     * <p>
     * El orden es <b>descendente</b>: los jugadores con un ranking más alto
     * aparecen primero.
     * </p>
     *
     * @param otro otro objeto {@link ResultadoPartida} con el que se compara.
     * @return un número negativo si este ranking es mayor, positivo si es
     * menor, o cero si son iguales.
     */
    @Override
    public int compareTo(ResultadoPartida otro) {
        // Ordenar por ranking descendente (mayor es mejor)
        return Double.compare(otro.ranking, this.ranking);
    }

    // ─────────────────────────────────────────────
    // Getters y Setters
    // ─────────────────────────────────────────────
    /**
     * Obtiene el nombre del jugador asociado a este resultado.
     *
     * @return nombre del jugador.
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     * Asigna el nombre del jugador.
     * <p>
     * Si el texto supera los 50 caracteres, se limita automáticamente mediante
     * el método {@code limitarTexto()}.
     * </p>
     *
     * @param nombreJugador nombre del jugador (máximo 50 caracteres).
     */
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = limitarTexto(nombreJugador, 50);
    }

    /**
     * Obtiene el puntaje total obtenido por el jugador.
     *
     * @return puntaje total acumulado.
     */
    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    /**
     * Establece el puntaje total del jugador.
     * <p>
     * Cada vez que se modifica, se recalcula automáticamente el ranking general
     * del jugador.
     * </p>
     *
     * @param puntajeTotal nuevo puntaje total.
     */
    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
        this.ranking = calcularRanking();
    }

    /**
     * Obtiene el tiempo total (en milisegundos) que el jugador tardó en
     * completar la partida.
     *
     * @return tiempo de juego en milisegundos.
     */
    public long getTiempoMs() {
        return tiempoMs;
    }

    /**
     * Asigna el tiempo total utilizado por el jugador para completar la
     * partida.
     * <p>
     * Al actualizar el tiempo, también se recalcula el ranking del jugador.
     * </p>
     *
     * @param tiempoMs duración total de la partida en milisegundos.
     */
    public void setTiempoMs(long tiempoMs) {
        this.tiempoMs = tiempoMs;
        this.ranking = calcularRanking();
    }

    /**
     * Obtiene una descripción de las frutas comidas por el jugador durante la
     * partida.
     *
     * @return lista o descripción de las frutas comidas.
     */
    public String getFrutasComidas() {
        return frutasComidas;
    }

    /**
     * Asigna el detalle de las frutas comidas por el jugador.
     *
     * @param frutasComidas cadena descriptiva de las frutas comidas.
     */
    public void setFrutasComidas(String frutasComidas) {
        this.frutasComidas = frutasComidas;
    }

    /**
     * Obtiene la fecha y hora en que se jugó la partida.
     *
     * @return objeto {@link LocalDateTime} que representa el momento de la
     * partida.
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora en que se registró el resultado de la partida.
     *
     * @param fechaHora fecha y hora de la partida.
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el ranking calculado del jugador.
     * <p>
     * Este valor combina factores como el puntaje total y el tiempo utilizado,
     * reflejando el desempeño general del jugador.
     * </p>
     *
     * @return valor numérico del ranking (mayor es mejor).
     */
    public double getRanking() {
        return ranking;
    }

}
