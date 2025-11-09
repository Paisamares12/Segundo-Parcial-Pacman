package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Ventana principal del Cliente (solo UI).
 *
 * <p>
 * Se suscribe a {@link ClienteEstado} para reaccionar a cambios (snapshot,
 * habilitar movimiento y log). No contiene listeners de teclado ni lógica de
 * red.</p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class MarcoCliente extends JFrame implements PropertyChangeListener {

    private final PanelControles panelControles = new PanelControles();
    private final PanelJuegoCliente panelJuego = new PanelJuegoCliente();
    private final JTextArea areaMensajes = new JTextArea(5, 60);

    /**
     * Referencias inyectadas desde el composition root.
     */
    private ControlCliente control;
    private ClienteEstado estado;

    public MarcoCliente() {
        super("Cliente Pac-Man – Espectador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        areaMensajes.setEditable(false);

        add(panelControles, BorderLayout.WEST);
        add(panelJuego, BorderLayout.CENTER);
        add(new JScrollPane(areaMensajes), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * La vista conoce al controlador SOLO para despachar acciones de botones.
     */
    public void setControl(ControlCliente control) {
        this.control = control;
        this.panelControles.setControl(control);
    }

    /**
     * La vista se suscribe al estado (modelo observable del cliente).
     */
    public void setEstado(ClienteEstado estado) {
        if (this.estado != null) {
            this.estado.removePropertyChangeListener(this);
        }
        this.estado = estado;
        this.estado.addPropertyChangeListener(this);
    }

    /**
     * Componente de juego para que ControlInterfazCliente instale atajos.
     */
    public JComponent getComponenteJuego() {
        return panelJuego;
    }

    public void mostrar() {
        setVisible(true);
    }

    // ─── Reacciones a cambios en ClienteEstado ────────────────────────────────
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ClienteEstado.PROP_SNAPSHOT -> {
                SnapshotTablero snap = (SnapshotTablero) evt.getNewValue();
                panelJuego.setSnapshot(snap);
                panelJuego.repaint();
            }
            case ClienteEstado.PROP_MOV_HABILITADO -> {
                boolean on = (boolean) evt.getNewValue();
                panelControles.habilitarMovimiento(on);
            }
            case ClienteEstado.PROP_LOG -> {
                String msg = (String) evt.getNewValue();
                if (msg != null && !msg.isEmpty()) {
                    areaMensajes.append(msg + "\n");
                    areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
                }
            }
        }
    }
}
