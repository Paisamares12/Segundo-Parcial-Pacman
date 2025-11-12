/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.cliente.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Representa el resultado de una partida para almacenamiento en archivo.
 *
 * <p>Esta clase encapsula todos los datos relevantes de una partida completada
 * y puede ser serializada para guardarse en un archivo de acceso aleatorio.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-11
 */
public class ResultadoPartida implements Serializable, Comparable<ResultadoPartida> {

    private static final long serialVersionUID = 1L;

    /** Tamaño fijo del registro en bytes (para acceso aleatorio) */
    public static final int TAMANIO_REGISTRO = 200;

    /** Nombre del jugador (máximo 50 caracteres) */
    private String nombreJugador;

    /** Puntaje total obtenido */
    private int puntajeTotal;

    /** Tiempo en milisegundos */
    private long tiempoMs;

    /** Frutas comidas (como texto separado por comas) */
    private String frutasComidas;

    /** Fecha y hora de la partida */
    private LocalDateTime fechaHora;

    /** Ranking calculado (puntaje / tiempo en segundos) */
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
        if (texto == null) return "";
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

    @Override
    public int compareTo(ResultadoPartida otro) {
        // Ordenar por ranking descendente (mayor es mejor)
        return Double.compare(otro.ranking, this.ranking);
    }

    // Getters y Setters

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = limitarTexto(nombreJugador, 50);
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
        this.ranking = calcularRanking();
    }

    public long getTiempoMs() {
        return tiempoMs;
    }

    public void setTiempoMs(long tiempoMs) {
        this.tiempoMs = tiempoMs;
        this.ranking = calcularRanking();
    }

    public String getFrutasComidas() {
        return frutasComidas;
    }

    public void setFrutasComidas(String frutasComidas) {
        this.frutasComidas = frutasComidas;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getRanking() {
        return ranking;
    }

    @Override
    public String toString() {
        return "ResultadoPartida{" +
                "nombreJugador='" + nombreJugador + '\'' +
                ", puntajeTotal=" + puntajeTotal +
                ", tiempo=" + getTiempoFormateado() +
                ", ranking=" + getRankingFormateado() +
                ", fecha=" + getFechaFormateada() +
                '}';
    }
}
