package udistrital.avanzada.parcial.cliente.api;

import udistrital.avanzada.parcial.mensajes.RespuestaMovimiento;
import java.beans.PropertyChangeListener;

/**
 * Interfaz que define el contrato para el estado observable del cliente (patrón
 * Observer / Observable).
 *
 * <p>
 * Esta interfaz permite desacoplar completamente el {@code ControlCliente} y la
 * {@code Vista} (por ejemplo, {@code MarcoCliente}), de modo que ninguno de
 * ellos conozca la implementación concreta del estado interno.
 * </p>
 *
 * <p>
 * El modelo concreto —por ejemplo {@code ClienteEstado}— será el encargado de
 * implementar esta interfaz, manteniendo un
 * {@link java.beans.PropertyChangeSupport} para notificar cambios a los
 * observadores registrados.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 * <li>Permitir la suscripción y eliminación de observadores (vistas).</li>
 * <li>Exponer métodos para que el controlador publique cambios de estado:
 * respuesta de movimiento, habilitación de movimiento y mensajes de log.</li>
 * </ul>
 *
 * <p>
 * Originalmente hecho por Paula Martínez, pero modificado por Juan Sebastián
 * Bravo Rojas
 * </p>
 *
 * @author Paula Martinez
 * @version 4.0
 * @since 2025-11-11
 */
public interface EstadoClienteObservable {

    // ───── Observabilidad ──────────────────────────────────────────────────
    /**
     * Registra un oyente para recibir notificaciones de cambio de propiedad.
     *
     * @param l instancia del {@link PropertyChangeListener} que se desea
     * agregar
     */
    void addPropertyChangeListener(PropertyChangeListener l);

    /**
     * Elimina un oyente previamente registrado.
     *
     * @param l instancia del {@link PropertyChangeListener} que se desea
     * eliminar
     */
    void removePropertyChangeListener(PropertyChangeListener l);

    // ───── Publicación de cambios ──────────────────────────────────────────
    /**
     * Publica una nueva respuesta de movimiento recibida desde el servidor.
     *
     * @param respuesta objeto {@link RespuestaMovimiento} con el resultado del
     * movimiento
     */
    void setRespuestaMovimiento(RespuestaMovimiento respuesta);

    /**
     * Habilita o deshabilita los controles de movimiento en la UI.
     *
     * @param on {@code true} para habilitar, {@code false} para deshabilitar
     */
    void setMovHabilitado(boolean on);

    /**
     * Publica un mensaje de registro o notificación para mostrar en la UI.
     *
     * @param mensaje texto informativo o de error
     */
    void log(String mensaje);

    // ───── Consultas de estado ────────────────────────────────────────────
    /**
     * Verifica si el juego ha terminado.
     *
     * @return {@code true} si el juego terminó, {@code false} en caso contrario
     */
    boolean isJuegoTerminado();

    /**
     * Verifica si el movimiento está habilitado.
     *
     * @return {@code true} si el movimiento está habilitado, {@code false} en
     * caso contrario
     */
    boolean isMovHabilitado();
}
