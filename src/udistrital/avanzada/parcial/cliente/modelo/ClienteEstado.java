package udistrital.avanzada.parcial.cliente.modelo;

import udistrital.avanzada.parcial.mensajes.RespuestaMovimiento;
import udistrital.avanzada.parcial.mensajes.RespuestaFinal;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import udistrital.avanzada.parcial.cliente.api.EstadoClienteObservable;
import udistrital.avanzada.parcial.cliente.api.EventosCliente;

/**
 * Estado observable del cliente (lado UI).
 *
 * <p>
 * Originalmente hecho por Paula Martínez, pero modificado por Juan Sebastián
 * Bravo Rojas y Juan Estevan Ariza Ortiz.
 * </p>
 *
 * <p>
 * Esta clase implementa la interfaz {@link EstadoClienteObservable} y funciona
 * como modelo dentro del patrón <b>MVC</b>, notificando a las vistas (interfaz
 * gráfica) cada vez que ocurre un cambio relevante en el estado del juego.
 * </p>
 *
 * <p>
 * Utiliza el mecanismo de observadores de Java ({@link PropertyChangeSupport})
 * para emitir eventos de actualización. De este modo, las vistas pueden
 * escuchar cambios en propiedades como el movimiento, el registro de logs, el
 * fin del juego o la recepción de la respuesta final del servidor.
 * </p>
 *
 * @author Paula Martinez
 * @version 4.0
 * @since 2025-11-11
 */
public class ClienteEstado implements EstadoClienteObservable {

    /**
     * Propiedad asociada a los cambios de movimiento recibidos.
     */
    public static final String PROP_RESPUESTA_MOVIMIENTO = EventosCliente.RESPUESTA_MOVIMIENTO;

    /**
     * Propiedad asociada a la habilitación o deshabilitación del movimiento.
     */
    public static final String PROP_MOV_HABILITADO = EventosCliente.MOV_HABILITADO;

    /**
     * Propiedad asociada al registro de logs (mensajes del sistema).
     */
    public static final String PROP_LOG = EventosCliente.LOG;

    /**
     * Propiedad asociada al fin del juego.
     */
    public static final String PROP_JUEGO_TERMINADO = EventosCliente.JUEGO_TERMINADO;

    /**
     * Propiedad asociada a la recepción de la respuesta final.
     */
    public static final String PROP_RESPUESTA_FINAL = "respuestaFinal";

    /**
     * Soporte interno para el manejo de observadores (patrón Observer de
     * JavaBeans).
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Última respuesta de movimiento recibida desde el servidor.
     */
    private RespuestaMovimiento ultimaRespuesta;

    /**
     * Información final del juego enviada por el servidor.
     */
    private RespuestaFinal respuestaFinal;

    /**
     * Indica si los movimientos están actualmente habilitados para el jugador.
     */
    private boolean movHabilitado;

    /**
     * Indica si el juego ha terminado.
     */
    private boolean juegoTerminado;

    /**
     * Constructor que inicializa el estado del cliente con valores por defecto.
     * Los movimientos y el estado del juego comienzan deshabilitados.
     */
    public ClienteEstado() {
        this.movHabilitado = false;
        this.juegoTerminado = false;
    }

    // ───────────────────────────────
    // MÉTODOS DE OBSERVADORES
    // ───────────────────────────────
    /**
     * {@inheritDoc}
     *
     * <p>
     * Registra un nuevo observador para escuchar los cambios de propiedades del
     * estado del cliente. Es utilizado principalmente por la vista.
     * </p>
     *
     * @param l el {@link PropertyChangeListener} que escuchará los eventos.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Elimina un observador previamente registrado.
     * </p>
     *
     * @param l el {@link PropertyChangeListener} a eliminar.
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    // ───────────────────────────────
    // ACTUALIZACIÓN DE ESTADO
    // ───────────────────────────────
    /**
     * {@inheritDoc}
     *
     * <p>
     * Actualiza la última respuesta de movimiento recibida y notifica a los
     * observadores. Si la respuesta indica que el juego terminó, también cambia
     * el estado de fin de juego.
     * </p>
     *
     * @param respuesta objeto con la información del último movimiento
     * recibido.
     */
    @Override
    public void setRespuestaMovimiento(RespuestaMovimiento respuesta) {
        RespuestaMovimiento anterior = this.ultimaRespuesta;
        this.ultimaRespuesta = respuesta;
        pcs.firePropertyChange(PROP_RESPUESTA_MOVIMIENTO, anterior, respuesta);

        if (respuesta != null && respuesta.isJuegoTerminado()) {
            setJuegoTerminado(true);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Habilita o deshabilita la capacidad de movimiento del cliente y notifica
     * el cambio a los observadores.
     * </p>
     *
     * @param valor true si el movimiento debe habilitarse; false en caso
     * contrario.
     */
    @Override
    public void setMovHabilitado(boolean valor) {
        boolean prev = this.movHabilitado;
        this.movHabilitado = valor;
        pcs.firePropertyChange(PROP_MOV_HABILITADO, prev, valor);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Envía un mensaje al registro de logs del cliente, generando un evento que
     * puede ser mostrado por la vista.
     * </p>
     *
     * @param mensaje texto a registrar en el log.
     */
    @Override
    public void log(String mensaje) {
        pcs.firePropertyChange(PROP_LOG, null, mensaje);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Indica si el juego ha terminado, según el último estado recibido.
     * </p>
     *
     * @return true si el juego ya terminó; false en caso contrario.
     */
    @Override
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Indica si los movimientos del jugador están habilitados.
     * </p>
     *
     * @return true si el jugador puede moverse; false si no.
     */
    @Override
    public boolean isMovHabilitado() {
        return movHabilitado;
    }

    // ───────────────────────────────
    // MÉTODOS ADICIONALES DEL ESTADO
    // ───────────────────────────────
    /**
     * Retorna la última respuesta de movimiento recibida.
     *
     * @return la última {@link RespuestaMovimiento}.
     */
    public RespuestaMovimiento getUltimaRespuesta() {
        return ultimaRespuesta;
    }

    /**
     * Actualiza el estado de fin de juego y notifica a los observadores.
     *
     * @param terminado true si el juego ha finalizado; false en caso contrario.
     */
    public void setJuegoTerminado(boolean terminado) {
        boolean prev = this.juegoTerminado;
        this.juegoTerminado = terminado;
        pcs.firePropertyChange(PROP_JUEGO_TERMINADO, prev, terminado);
    }

    /**
     * Establece la respuesta final y dispara el evento correspondiente.
     *
     * <p>
     * Este método es llamado por {@code ControlCliente} cuando el servidor
     * envía la {@link RespuestaFinal} al terminar el juego.
     * </p>
     *
     * @param respuesta la respuesta final del servidor.
     */
    public void setRespuestaFinal(RespuestaFinal respuesta) {
        System.out.println("✓ ClienteEstado.setRespuestaFinal() llamado: " + respuesta);
        this.respuestaFinal = respuesta;
        pcs.firePropertyChange(PROP_RESPUESTA_FINAL, null, respuesta);
    }

    /**
     * Retorna la última {@link RespuestaFinal} almacenada en el estado del
     * cliente.
     *
     * @return la respuesta final del servidor.
     */
    public RespuestaFinal getRespuestaFinal() {
        return respuestaFinal;
    }
}
