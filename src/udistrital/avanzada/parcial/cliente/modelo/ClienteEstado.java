package udistrital.avanzada.parcial.cliente.modelo;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import udistrital.avanzada.parcial.cliente.api.EstadoClienteObservable;

/**
 * Estado observable del cliente (lado UI).
 *
 * <p>
 * El controlador de red actualiza este modelo y la vista se suscribe para
 * reaccionar a los cambios, evitando que el control conozca la vista.</p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class ClienteEstado implements EstadoClienteObservable {

    public static final String PROP_SNAPSHOT = "snapshot";
    public static final String PROP_MOV_HABILITADO = "movHabilitado";
    public static final String PROP_LOG = "log";

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private SnapshotTablero snapshot;
    private boolean movHabilitado;

    /**
     * Suscribe un oyente a los cambios del estado.
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Quita un oyente.
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * Publica un nuevo snapshot (vista repinta).
     */
    public void setSnapshot(SnapshotTablero nuevo) {
        SnapshotTablero anterior = this.snapshot;
        this.snapshot = nuevo;
        pcs.firePropertyChange(PROP_SNAPSHOT, anterior, nuevo);
    }

    /**
     * @return último snapshot recibido.
     */
    public SnapshotTablero getSnapshot() {
        return snapshot;
    }

    /**
     * Habilita/deshabilita movimiento (botones/teclas).
     */
    public void setMovHabilitado(boolean valor) {
        boolean prev = this.movHabilitado;
        this.movHabilitado = valor;
        pcs.firePropertyChange(PROP_MOV_HABILITADO, prev, valor);
    }

    public boolean isMovHabilitado() {
        return movHabilitado;
    }

    /**
     * Publica una línea para el log de la UI.
     */
    public void log(String mensaje) {
        pcs.firePropertyChange(PROP_LOG, null, mensaje);
    }
}
