package udistrital.avanzada.parcial.cliente.control;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Controlador de interfaz del cliente (sin dependencia de clases de vista
 * concretas).
 *
 * <p>
 * Su único rol es instalar atajos de teclado sobre un componente gráfico
 * (usualmente el panel de juego) para delegar acciones al
 * {@link ControlCliente}.
 * </p>
 *
 * <p>
 * Este controlador no conoce {@code MarcoCliente} ni ningún detalle de UI;
 * recibe únicamente el {@link ControlCliente} y el componente gráfico genérico.
 * </p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class ControlInterfazCliente {

    private final ControlCliente control;

    /**
     * Crea el controlador de interfaz, sin acoplarlo a una vista concreta.
     */
    public ControlInterfazCliente(ControlCliente control) {
        this.control = control;
    }

    /**
     * Instala atajos de teclado sobre el componente indicado.
     *
     * @param comp componente Swing sobre el que se aplicarán los key bindings
     */
    public void instalarKeyBindings(JComponent comp) {
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        // Flechas
        im.put(KeyStroke.getKeyStroke("UP"), "mvUp");
        im.put(KeyStroke.getKeyStroke("DOWN"), "mvDown");
        im.put(KeyStroke.getKeyStroke("LEFT"), "mvLeft");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "mvRight");

        // Opcional: WASD
        im.put(KeyStroke.getKeyStroke('W'), "mvUp");
        im.put(KeyStroke.getKeyStroke('S'), "mvDown");
        im.put(KeyStroke.getKeyStroke('A'), "mvLeft");
        im.put(KeyStroke.getKeyStroke('D'), "mvRight");

        am.put("mvUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.moverArriba();
            }
        });
        am.put("mvDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.moverAbajo();
            }
        });
        am.put("mvLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.moverIzquierda();
            }
        });
        am.put("mvRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.moverDerecha();
            }
        });

        // Preparar el componente para recibir teclas
        comp.setFocusable(true);
        comp.setFocusTraversalKeysEnabled(false);
        comp.requestFocusInWindow();
    }
}
