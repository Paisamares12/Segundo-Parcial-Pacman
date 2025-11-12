package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.cliente.control.ControlCliente;

import javax.swing.*;
import java.awt.*;

/**
 * Panel lateral con controles de movimiento únicamente.
 *
 * <p>Emite acciones al {@link ControlCliente}. Solo muestra los botones
 * de movimiento direccional.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @author Paula Martinez
 * @author Juan Sebastián Bravo Rojas
 * @version 2.0
 * @since 2025-11-11
 */
public class PanelControles extends JPanel {

    // Botones de movimiento
    private final JButton btnArriba = new JButton("↑");
    private final JButton btnAbajo = new JButton("↓");
    private final JButton btnIzquierda = new JButton("←");
    private final JButton btnDerecha = new JButton("→");

    /** Referencia al controlador (inyectada por la ventana) */
    private ControlCliente control;

    /**
     * Crea el panel y organiza los controles.
     */
    public PanelControles() {
        super(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de movimiento
        JPanel pnlMovimiento = new JPanel(new GridBagLayout());
        pnlMovimiento.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Control de Movimiento"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Configurar estilo de botones
        configurarBoton(btnArriba);
        configurarBoton(btnAbajo);
        configurarBoton(btnIzquierda);
        configurarBoton(btnDerecha);

        GridBagConstraints m = new GridBagConstraints();
        m.insets = new Insets(5, 5, 5, 5);
        m.fill = GridBagConstraints.BOTH;
        m.ipadx = 20;
        m.ipady = 20;
        
        m.gridx = 1;
        m.gridy = 0;
        pnlMovimiento.add(btnArriba, m);
        
        m.gridx = 0;
        m.gridy = 1;
        pnlMovimiento.add(btnIzquierda, m);
        
        m.gridx = 2;
        m.gridy = 1;
        pnlMovimiento.add(btnDerecha, m);
        
        m.gridx = 1;
        m.gridy = 2;
        pnlMovimiento.add(btnAbajo, m);

        add(pnlMovimiento, BorderLayout.CENTER);

        // Instrucciones
        JPanel pnlInstrucciones = new JPanel(new BorderLayout());
        pnlInstrucciones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel lblInstrucciones = new JLabel("<html><center><b>Atajos:</b><br>" +
                "Flechas: ↑↓←→<br>" +
                "WASD</center></html>");
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInstrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInstrucciones.add(lblInstrucciones, BorderLayout.CENTER);
        
        add(pnlInstrucciones, BorderLayout.SOUTH);

        // Estado inicial
        habilitarMovimiento(false);

        // Listeners
        btnArriba.addActionListener(e -> {
            if (control != null) control.moverArriba();
        });
        btnAbajo.addActionListener(e -> {
            if (control != null) control.moverAbajo();
        });
        btnIzquierda.addActionListener(e -> {
            if (control != null) control.moverIzquierda();
        });
        btnDerecha.addActionListener(e -> {
            if (control != null) control.moverDerecha();
        });
    }

    /**
     * Configura el estilo visual de un botón.
     *
     * @param boton botón a configurar
     */
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 24));
        boton.setBackground(new Color(70, 130, 180));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    /**
     * Inyecta el controlador que manejará los eventos de la UI.
     *
     * @param control instancia del controlador (no nula)
     */
    public void setControl(ControlCliente control) {
        this.control = control;
    }

    /**
     * Habilita o deshabilita los botones de movimiento.
     *
     * @param habilitar true para habilitar; false para deshabilitar
     */
    public void habilitarMovimiento(boolean habilitar) {
        btnArriba.setEnabled(habilitar);
        btnAbajo.setEnabled(habilitar);
        btnIzquierda.setEnabled(habilitar);
        btnDerecha.setEnabled(habilitar);
        
        // Cambiar color según estado
        Color color = habilitar ? new Color(70, 130, 180) : Color.GRAY;
        btnArriba.setBackground(color);
        btnAbajo.setBackground(color);
        btnIzquierda.setBackground(color);
        btnDerecha.setBackground(color);
    }
}