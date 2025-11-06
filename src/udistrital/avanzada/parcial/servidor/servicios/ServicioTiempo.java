package udistrital.avanzada.parcial.servidor.servicios;

/**
 * Servicio de cronometraje simple para medir la duración de la partida.
 *
 * <p>
 * Maneja un reloj de pared basado en {@link System#currentTimeMillis()}. No
 * persiste ni formatea resultados, y es agnóstico de UI/red.</p>
 *
 * <p>
 * Protocolo básico:
 * <ol>
 * <li>{@link #iniciar()} al comenzar la partida.</li>
 * <li>{@link #detener()} al comer todas las frutas.</li>
 * <li>{@link #milisegundosTranscurridos()} para consultar el tiempo.</li>
 * </ol>
 * </p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public class ServicioTiempo {

    private long inicio = -1L;
    private long fin = -1L;

    /**
     * Inicia (o reinicia) el cronómetro.
     */
    public void iniciar() {
        inicio = System.currentTimeMillis();
        fin = -1L;
    }

    /**
     * Detiene el cronómetro si estaba iniciado.
     */
    public void detener() {
        if (inicio >= 0) {
            fin = System.currentTimeMillis();
        }
    }

    /**
     * @return {@code true} si el cronómetro está corriendo.
     */
    public boolean activo() {
        return inicio >= 0 && fin < 0;
    }

    /**
     * Retorna el tiempo transcurrido en milisegundos.
     * <ul>
     * <li>Si está activo, mide desde {@link #iniciar()} hasta ahora.</li>
     * <li>Si está detenido, entrega la duración entre iniciar/detener.</li>
     * <li>Si nunca se inició, retorna 0.</li>
     * </ul>
     *
     * @return milisegundos transcurridos
     */
    public long milisegundosTranscurridos() {
        if (inicio < 0) {
            return 0L;
        }
        return (fin >= 0 ? fin : System.currentTimeMillis()) - inicio;
    }

    /**
     * Restablece el cronómetro a su estado inicial (no iniciado).
     */
    public void reiniciar() {
        inicio = -1L;
        fin = -1L;
    }
}
