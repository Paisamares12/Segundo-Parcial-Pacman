package udistrital.avanzada.parcial.cliente.api;

/**
 * Conjunto de constantes que representan los nombres de los eventos observables
 * del cliente en el patrón Observer / Observable.
 *
 * <p>
 * Esta clase define las claves que se utilizan al disparar y escuchar cambios
 * de propiedades en el modelo observable del cliente ({@code ClienteEstado}).
 * De esta manera, tanto el controlador como la vista pueden comunicarse a
 * través de identificadores comunes sin depender directamente de la
 * implementación del modelo.
 * </p>
 *
 * <h3>Eventos definidos</h3>
 * <ul>
 * <li>{@link #SNAPSHOT}: se emite cuando se recibe un nuevo estado del tablero
 * desde el servidor.</li>
 * <li>{@link #MOV_HABILITADO}: indica que los controles de movimiento deben
 * habilitarse o deshabilitarse.</li>
 * <li>{@link #LOG}: representa un mensaje informativo o de error para mostrar
 * en la interfaz.</li>
 * </ul>
 *
 * <p>
 * La clase es {@code final} y tiene un constructor privado para impedir su
 * instanciación o herencia.
 * </p>
 *
 * <h3>Ejemplo de uso</h3>
 * <pre>{@code
 * // Desde el modelo (ClienteEstado)
 * pcs.firePropertyChange(EventosCliente.SNAPSHOT, anterior, nuevo);
 *
 * // Desde la vista (MarcoCliente)
 * if (evt.getPropertyName().equals(EventosCliente.LOG)) {
 *     areaMensajes.append((String) evt.getNewValue());
 * }
 * }</pre>
 *
 * @author Paula Martinez
 * @version 3.0
 * @since 2025-11-10
 */
public final class EventosCliente {

    /**
     * Constructor privado para evitar instanciación.
     */
    private EventosCliente() {
    }

    /**
     * Evento emitido al recibir un nuevo snapshot del tablero.
     */
    public static final String SNAPSHOT = "snapshot";

    /**
     * Evento emitido al habilitar o deshabilitar los controles de movimiento.
     */
    public static final String MOV_HABILITADO = "movHabilitado";

    /**
     * Evento emitido al registrar un mensaje de log o notificación.
     */
    public static final String LOG = "log";
}
