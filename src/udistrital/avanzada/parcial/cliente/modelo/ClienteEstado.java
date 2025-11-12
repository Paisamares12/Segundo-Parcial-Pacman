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
 * @author Juan Estevan Ariza Ortiz
 * @author Paula Martinez
 * @author Juan Sebastián Bravo Rojas
 * @version 2.2
 * @since 2025-11-11
 */
public class ClienteEstado implements EstadoClienteObservable {

    public static final String PROP_RESPUESTA_MOVIMIENTO = EventosCliente.RESPUESTA_MOVIMIENTO;
    public static final String PROP_MOV_HABILITADO = EventosCliente.MOV_HABILITADO;
    public static final String PROP_LOG = EventosCliente.LOG;
    public static final String PROP_JUEGO_TERMINADO = EventosCliente.JUEGO_TERMINADO;
    public static final String PROP_RESPUESTA_FINAL = "respuestaFinal";

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private RespuestaMovimiento ultimaRespuesta;
    private RespuestaFinal respuestaFinal;
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
    
    /**
     * Establece la respuesta final y dispara el evento.
     * Este método es llamado por ControlCliente cuando recibe RespuestaFinal del servidor.
     *
     * @param respuesta respuesta final del servidor
     */
    public void setRespuestaFinal(RespuestaFinal respuesta) {
        System.out.println("✓ ClienteEstado.setRespuestaFinal() llamado: " + respuesta);
        this.respuestaFinal = respuesta;
        pcs.firePropertyChange(PROP_RESPUESTA_FINAL, null, respuesta);
    }
    
    public RespuestaFinal getRespuestaFinal() {
        return respuestaFinal;
    }
}