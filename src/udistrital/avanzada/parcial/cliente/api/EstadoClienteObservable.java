package udistrital.avanzada.parcial.cliente.api;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;
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
 * snapshot, habilitación de movimiento y mensajes de log.</li>
 * </ul>
 *
 * <h3>Implementación recomendada</h3>
 * <pre>{@code
 * public class ClienteEstado implements EstadoClienteObservable {
 *     private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
 *     // ...
 * }
 * }</pre>
 *
 * @author Paula Martinez
 * @version 3.0
 * @since 2025-11-10
 */
public interface EstadoClienteObservable {

    // ───── Observabilidad ──────────────────────────────────────────────
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

    // ───── Publicación de cambios ─────────────────────────────────────
    /**
     * Publica un nuevo snapshot del tablero recibido desde el servidor.
     *
     * @param s objeto {@link SnapshotTablero} que representa el estado del
     * juego
     */
    void setSnapshot(SnapshotTablero s);

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
}
