package udistrital.avanzada.parcial.cliente.modelo;

import java.awt.image.BufferedImage;
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

    public static final String PROP_RESPUESTA_MOVIMIENTO = EventosCliente.RESPUESTA_MOVIMIENTO;
    public static final String PROP_MOV_HABILITADO = EventosCliente.MOV_HABILITADO;
    public static final String PROP_LOG = EventosCliente.LOG;
    public static final String PROP_JUEGO_TERMINADO = EventosCliente.JUEGO_TERMINADO;
    public static final String PROP_RESPUESTA_FINAL = "respuestaFinal";
    public static final String PROP_FRAME = "frame"; // NUEVO

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private RespuestaMovimiento ultimaRespuesta;
    private RespuestaFinal respuestaFinal;
    private BufferedImage frame; // NUEVO
    private boolean movHabilitado;
    private boolean juegoTerminado;

    public ClienteEstado() {
        this.movHabilitado = false;
        this.juegoTerminado = false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    @Override
    public void setRespuestaMovimiento(RespuestaMovimiento respuesta) {
        RespuestaMovimiento anterior = this.ultimaRespuesta;
        this.ultimaRespuesta = respuesta;
        pcs.firePropertyChange(PROP_RESPUESTA_MOVIMIENTO, anterior, respuesta);
        
        if (respuesta != null && respuesta.isJuegoTerminado()) {
            setJuegoTerminado(true);
        }
    }

    @Override
    public void setMovHabilitado(boolean valor) {
        boolean prev = this.movHabilitado;
        this.movHabilitado = valor;
        pcs.firePropertyChange(PROP_MOV_HABILITADO, prev, valor);
    }

    @Override
    public void log(String mensaje) {
        pcs.firePropertyChange(PROP_LOG, null, mensaje);
    }

    @Override
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    @Override
    public boolean isMovHabilitado() {
        return movHabilitado;
    }

    public RespuestaMovimiento getUltimaRespuesta() {
        return ultimaRespuesta;
    }

    public void setJuegoTerminado(boolean terminado) {
        boolean prev = this.juegoTerminado;
        this.juegoTerminado = terminado;
        pcs.firePropertyChange(PROP_JUEGO_TERMINADO, prev, terminado);
    }
    
    public void setRespuestaFinal(RespuestaFinal respuesta) {
        System.out.println("✓ ClienteEstado.setRespuestaFinal() llamado: " + respuesta);
        this.respuestaFinal = respuesta;
        pcs.firePropertyChange(PROP_RESPUESTA_FINAL, null, respuesta);
    }
    
    public RespuestaFinal getRespuestaFinal() {
        return respuestaFinal;
    }
    
    /**
     * NUEVO: Establece el frame recibido del servidor.
     *
     * @param frame imagen del frame
     */
    public void setFrame(BufferedImage frame) {
        BufferedImage prev = this.frame;
        this.frame = frame;
        pcs.firePropertyChange(PROP_FRAME, prev, frame);
    }
    
    public BufferedImage getFrame() {
        return frame;
    }
}
