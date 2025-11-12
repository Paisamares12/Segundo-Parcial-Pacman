package udistrital.avanzada.parcial.cliente.api;

/**
 * Constantes que definen los nombres de los eventos del cliente observable.
 *
 * <p>Estas constantes se usan para identificar los cambios de propiedad
 * en el patrón Observer implementado por {@link EstadoClienteObservable}.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0
 * @since 2025-11-11
 */
public final class EventosCliente {

    private EventosCliente() {
        // Clase de constantes, no instanciable
    }

    /** Evento disparado cuando se recibe una respuesta de movimiento del servidor */
    public static final String RESPUESTA_MOVIMIENTO = "respuestaMovimiento";

    /** Evento disparado cuando se habilita o deshabilita el movimiento */
    public static final String MOV_HABILITADO = "movHabilitado";

    /** Evento disparado cuando hay un nuevo mensaje de log */
    public static final String LOG = "log";
    
    /** Evento para cuando el juego termina */
    public static final String JUEGO_TERMINADO = "juegoTerminado";
    
    // Mantener compatibilidad (deprecated)
    /** @deprecated Usar RESPUESTA_MOVIMIENTO en su lugar */
    @Deprecated
    public static final String SNAPSHOT = "snapshot";
}