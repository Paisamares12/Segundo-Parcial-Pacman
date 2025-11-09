package udistrital.avanzada.parcial.servidor.control;

import udistrital.avanzada.parcial.servidor.modelo.EstadoJuego;
import udistrital.avanzada.parcial.servidor.vista.MarcoServidor;

import javax.swing.SwingUtilities;

/**
 * Controlador de la capa de interfaz del lado SERVIDOR.
 *
 * <p>
 * Coordina la vista del servidor ({@link MarcoServidor}) sin ejecutar lógica de
 * juego ni manejar sockets. Sus responsabilidades incluyen:
 * </p>
 *
 * <ul>
 * <li>Construir y mostrar la UI del servidor.</li>
 * <li>Vincular el {@link EstadoJuego} cuando esté inicializado por el
 * ControlJuego.</li>
 * <li>Refrescar el tablero y el HUD después de cada actualización del
 * juego.</li>
 * <li>Proveer utilidades para registrar mensajes en pantalla (si agregas un
 * log).</li>
 * </ul>
 *
 * <p>
 * El controlador de lógica (por ejemplo, {@code ControlJuego}) puede invocar a
 * {@link #vincularEstado(EstadoJuego)}, {@link #refrescarDesdeEstado(EstadoJuego, long)}
 * o {@link #refrescarTablero()} para mantener la vista sincronizada con el
 * modelo.
 * </p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class ControlInterfazServidor {

    /**
     * Ventana principal del servidor.
     */
    private final MarcoServidor vista;

    /**
     * Crea el controlador de interfaz del servidor.
     *
     * @param vista instancia de la ventana del servidor (no nula)
     */
    public ControlInterfazServidor(MarcoServidor vista) {
        this.vista = vista;
    }

    /**
     * Muestra la UI del servidor.
     */
    public void iniciar() {
        vista.mostrar();
    }

    /**
     * Vincula el estado del juego para que el panel de render pueda dibujarlo.
     *
     * @param estado instancia del estado del juego (no nula)
     */
    public void vincularEstado(EstadoJuego estado) {
        SwingUtilities.invokeLater(() -> vista.vincularEstado(estado));
    }

    /**
     * Actualiza el HUD y repinta el tablero a partir del estado más reciente.
     *
     * @param estado estado de juego con el puntaje actual
     * @param tiempoMs tiempo transcurrido (en milisegundos) provisto por un
     * servicio de tiempo
     */
    public void refrescarDesdeEstado(EstadoJuego estado, long tiempoMs) {
        SwingUtilities.invokeLater(() -> {
            vista.actualizarHUD(estado.getPuntaje(), tiempoMs);
            vista.refrescar();
        });
    }

    /**
     * Solicita repintar el tablero sin tocar el HUD. Útil cuando solo hubo
     * cambios de posición.
     */
    public void refrescarTablero() {
        SwingUtilities.invokeLater(vista::refrescar);
    }

    /**
     * Registra un mensaje informativo en la interfaz (si tu
     * {@link MarcoServidor} implementa un log visual). En la versión base, no
     * hace nada; mantenlo para futuro crecimiento sin romper firmas.
     *
     * @param mensaje texto a mostrar
     */
    public void log(String mensaje) {
        // Si agregas un área de log en MarcoServidor, llama aquí a vista.log(mensaje).
        // SwingUtilities.invokeLater(() -> vista.log(mensaje));
    }
}
