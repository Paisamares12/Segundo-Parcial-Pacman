package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.cliente.control.ControlCliente;

import javax.swing.*;
import java.awt.*;

/**
 * Panel lateral con controles de conexión y movimiento.
 *
 * <p>
 * Emite acciones al {@link ControlCliente}. No gestiona sockets ni lógica del
 * juego: se limita a construir la UI y a despachar eventos.
 * </p>
 *
 * <h3>Componentes</h3>
 * <ul>
 * <li>Campos: host, puerto, usuario, contraseña.</li>
 * <li>Botón Conectar.</li>
 * <li>Botones de movimiento: ↑, ↓, ←, →.</li>
 * </ul>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class PanelControles extends JPanel {

    // --- Campos de conexión ---
    private final JTextField txtHost = new JTextField("127.0.0.1", 10);
    private final JTextField txtPuerto = new JTextField("5050", 5);
    private final JTextField txtUsuario = new JTextField("paciente", 10);
    private final JPasswordField txtContrasena = new JPasswordField("1234", 10);

    private final JButton btnConectar = new JButton("Conectar");

    // --- Botones de movimiento ---
    private final JButton btnArriba = new JButton("↑");
    private final JButton btnAbajo = new JButton("↓");
    private final JButton btnIzquierda = new JButton("←");
    private final JButton btnDerecha = new JButton("→");

    /**
     * Referencia al controlador (inyectada por la ventana).
     */
    private ControlCliente control;

    /**
     * Crea el panel y organiza los controles.
     */
    public PanelControles() {
        super(new BorderLayout(6, 6));

        // Panel de conexión
        JPanel pnlConexion = new JPanel(new GridBagLayout());
        pnlConexion.setBorder(BorderFactory.createTitledBorder("Conexión"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.anchor = GridBagConstraints.WEST;

        int row = 0;
        c.gridx = 0;
        c.gridy = row;
        pnlConexion.add(new JLabel("Host:"), c);
        c.gridx = 1;
        pnlConexion.add(txtHost, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        pnlConexion.add(new JLabel("Puerto:"), c);
        c.gridx = 1;
        pnlConexion.add(txtPuerto, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        pnlConexion.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        pnlConexion.add(txtUsuario, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        pnlConexion.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        pnlConexion.add(txtContrasena, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnlConexion.add(btnConectar, c);

        // Panel de movimiento
        JPanel pnlMovimiento = new JPanel(new GridBagLayout());
        pnlMovimiento.setBorder(BorderFactory.createTitledBorder("Movimiento"));

        GridBagConstraints m = new GridBagConstraints();
        m.insets = new Insets(2, 2, 2, 2);
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

        // Armar panel
        JPanel cont = new JPanel(new BorderLayout(6, 6));
        cont.add(pnlConexion, BorderLayout.NORTH);
        cont.add(pnlMovimiento, BorderLayout.CENTER);

        add(cont, BorderLayout.NORTH);

        // Estado inicial
        habilitarMovimiento(false);

        // Listeners → delegan en el controlador
        btnConectar.addActionListener(e -> {
            if (control == null) {
                return;
            }
            String host = txtHost.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String puertoStr = txtPuerto.getText().trim();
            String pass = new String(txtContrasena.getPassword());

            int puerto = 5050;
            try {
                puerto = Integer.parseInt(puertoStr);
            } catch (NumberFormatException ignored) {
            }

            control.conectar(host, puerto, usuario, pass);
        });

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
    }
}
