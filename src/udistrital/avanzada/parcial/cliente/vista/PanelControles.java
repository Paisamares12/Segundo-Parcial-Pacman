package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import javax.swing.*;
import java.awt.*;

/**
 * Panel lateral con controles de movimiento únicamente.
 *
 * <p>
 * Este panel contiene los botones para controlar los movimientos del jugador en
 * la interfaz del cliente. Emite las acciones correspondientes al
 * {@link ControlCliente}, sin realizar ninguna lógica adicional.
 * </p>
 *
 * <p>
 * Originalmente desarrollado por <b>Paula Martínez</b>, posteriormente
 * modificado por
 * <b>Juan Estevan Ariza Ortiz</b> y <b>Juan Sebastián Bravo Rojas</b> para
 * mejorar la organización visual, aplicar estilo uniforme y estructurar el
 * código según las buenas prácticas de Swing.
 * </p>
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-11
 */
public class PanelControles extends JPanel {

    // -------------------------------------------------------------
    // Atributos
    // -------------------------------------------------------------
    /**
     * Botón para mover hacia arriba.
     */
    private final JButton btnArriba = new JButton("↑");
    /**
     * Botón para mover hacia abajo.
     */
    private final JButton btnAbajo = new JButton("↓");
    /**
     * Botón para mover hacia la izquierda.
     */
    private final JButton btnIzquierda = new JButton("←");
    /**
     * Botón para mover hacia la derecha.
     */
    private final JButton btnDerecha = new JButton("→");

    /**
     * Referencia al controlador (inyectada desde la ventana principal).
     */
    private ControlCliente control;

    // -------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------
    /**
     * Crea el panel de controles de movimiento.
     *
     * <p>
     * El diseño se basa en un {@link BorderLayout} con un subpanel central para
     * los botones y otro inferior para mostrar las instrucciones de uso.
     * </p>
     */
    public PanelControles() {
        super(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------------------------------------------------------
        // Panel de botones de movimiento
        // ---------------------------------------------------------
        JPanel pnlMovimiento = new JPanel(new GridBagLayout());
        pnlMovimiento.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Control de Movimiento"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Configura el estilo de los botones de movimiento
        configurarBoton(btnArriba);
        configurarBoton(btnAbajo);
        configurarBoton(btnIzquierda);
        configurarBoton(btnDerecha);

        // Define la disposición de los botones usando GridBagConstraints
        GridBagConstraints m = new GridBagConstraints();
        m.insets = new Insets(5, 5, 5, 5); // Espaciado entre botones
        m.fill = GridBagConstraints.BOTH;
        m.ipadx = 20;
        m.ipady = 20;

        // Disposición visual en forma de cruz
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

        // Agrega el panel de movimiento al centro
        add(pnlMovimiento, BorderLayout.CENTER);

        // ---------------------------------------------------------
        // Panel de instrucciones
        // ---------------------------------------------------------
        JPanel pnlInstrucciones = new JPanel(new BorderLayout());
        pnlInstrucciones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblInstrucciones = new JLabel("<html><center><b>Atajos:</b><br>"
                + "Flechas: ↑↓←→<br>"
                + "WASD</center></html>");
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInstrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInstrucciones.add(lblInstrucciones, BorderLayout.CENTER);

        add(pnlInstrucciones, BorderLayout.SOUTH);

        // ---------------------------------------------------------
        // Estado inicial y eventos
        // ---------------------------------------------------------
        // Los botones inician deshabilitados hasta que el servidor esté listo
        habilitarMovimiento(false);

        // Listeners que llaman métodos del controlador
        btnArriba.addActionListener(e -> {
            if (control != null) {
                control.moverArriba();
            }
        });
        btnAbajo.addActionListener(e -> {
            if (control != null) {
                control.moverAbajo();
            }
        });
        btnIzquierda.addActionListener(e -> {
            if (control != null) {
                control.moverIzquierda();
            }
        });
        btnDerecha.addActionListener(e -> {
            if (control != null) {
                control.moverDerecha();
            }
        });
    }

    // -------------------------------------------------------------
    // Métodos auxiliares
    // -------------------------------------------------------------
    /**
     * Configura el estilo visual de un botón de movimiento.
     *
     * @param boton botón a configurar
     */
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 24));
        boton.setBackground(new Color(70, 130, 180)); // Azul acero
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    // -------------------------------------------------------------
    // Métodos públicos
    // -------------------------------------------------------------
    /**
     * Inyecta el controlador que manejará los eventos de la UI.
     *
     * @param control instancia del {@link ControlCliente}, no nula
     */
    public void setControl(ControlCliente control) {
        this.control = control;
    }

    /**
     * Habilita o deshabilita los botones de movimiento.
     *
     * <p>
     * Se utiliza, por ejemplo, cuando el cliente aún no se conecta al servidor
     * o la partida ha terminado.
     * </p>
     *
     * @param habilitar true para habilitar; false para deshabilitar
     */
    public void habilitarMovimiento(boolean habilitar) {
        btnArriba.setEnabled(habilitar);
        btnAbajo.setEnabled(habilitar);
        btnIzquierda.setEnabled(habilitar);
        btnDerecha.setEnabled(habilitar);

        // Cambia el color de los botones según el estado
        Color color = habilitar ? new Color(70, 130, 180) : Color.GRAY;
        btnArriba.setBackground(color);
        btnAbajo.setBackground(color);
        btnIzquierda.setBackground(color);
        btnDerecha.setBackground(color);
    }
}
