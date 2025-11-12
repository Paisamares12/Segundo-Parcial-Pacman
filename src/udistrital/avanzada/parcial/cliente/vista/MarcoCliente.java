package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.cliente.control.ControlCliente;
import udistrital.avanzada.parcial.cliente.api.EstadoClienteObservable;
import udistrital.avanzada.parcial.cliente.api.EventosCliente;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.cliente.modelo.ResultadoPartida;
import udistrital.avanzada.parcial.cliente.persistencia.RankingDAO;
import udistrital.avanzada.parcial.mensajes.RespuestaMovimiento;
import udistrital.avanzada.parcial.mensajes.RespuestaFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Ventana principal del Cliente (solo controles y mensajes de texto).
 *
 * <p>
 * Originalmente hecho por Paula Martínez, pero modificado por Juan Sebastián
 * Bravo Rojas y Juan Estevan Ariza Ortiz.
 * </p>
 *
 * <p>
 * Esta vista implementa {@link PropertyChangeListener} para escuchar cambios en
 * el estado del cliente y actualizar la interfaz en consecuencia. Presenta:
 * </p>
 *
 * <ul>
 * <li>Un panel de controles para enviar movimientos.</li>
 * <li>Un área de mensajes/bitácora para mostrar información del juego.</li>
 * </ul>
 *
 * <p>
 * <b>Nota de concurrencia:</b> los cambios de UI deben ejecutarse en el hilo de
 * eventos de Swing (EDT). Cuando se recibe un evento desde otro hilo, se usa
 * {@link SwingUtilities#invokeLater(Runnable)}.
 * </p>
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-11
 */
public class MarcoCliente extends JFrame implements PropertyChangeListener {

    private final PanelControles panelControles = new PanelControles();
    private final JTextArea areaMensajes = new JTextArea(15, 50);
    private final PanelVideo panelVideo = new PanelVideo(); // NUEVO
    private final RankingDAO rankingDAO = new RankingDAO();

    private ControlCliente control;
    private EstadoClienteObservable estado;

    public MarcoCliente() {
        super("🎮 Cliente Pac-Man - Control Remoto + Video");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel izquierdo: controles
        add(panelControles, BorderLayout.WEST);
        
        // Panel central: video del juego
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder("📺 Transmisión en Vivo"));
        panelCentral.add(panelVideo, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: mensajes
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaMensajes.setBackground(new Color(30, 30, 30));
        areaMensajes.setForeground(new Color(0, 255, 0));
        areaMensajes.setCaretColor(Color.GREEN);
        
        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        scrollMensajes.setBorder(BorderFactory.createTitledBorder("Información del Juego"));
        add(scrollMensajes, BorderLayout.SOUTH);

        // Mensaje inicial
        areaMensajes.append("═══════════════════════════════════════════════════\n");
        areaMensajes.append("    CLIENTE PAC-MAN - STREAMING + CONTROL\n");
        areaMensajes.append("═══════════════════════════════════════════════════\n");
        areaMensajes.append("Conecta al servidor para comenzar a jugar.\n");
        areaMensajes.append("Controles: Flechas ↑↓←→ o WASD\n");
        areaMensajes.append("═══════════════════════════════════════════════════\n\n");

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void setControl(ControlCliente control) {
        this.control = control;
        this.panelControles.setControl(control);
    }

    public void setEstado(EstadoClienteObservable estado) {
        if (this.estado != null) {
            this.estado.removePropertyChangeListener(this);
        }
        this.estado = estado;
        this.estado.addPropertyChangeListener(this);
    }

    public JComponent getComponenteJuego() {
        return areaMensajes;
    }

    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case EventosCliente.RESPUESTA_MOVIMIENTO -> {
                RespuestaMovimiento respuesta = (RespuestaMovimiento) evt.getNewValue();
                if (respuesta != null) {
                    mostrarRespuestaMovimiento(respuesta);
                }
            }
            case EventosCliente.MOV_HABILITADO -> {
                boolean on = (boolean) evt.getNewValue();
                panelControles.habilitarMovimiento(on);
            }
            case EventosCliente.LOG -> {
                String msg = (String) evt.getNewValue();
                if (msg != null && !msg.isEmpty()) {
                    areaMensajes.append(msg);
                    if (!msg.endsWith("\n")) {
                        areaMensajes.append("\n");
                    }
                    areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
                }
            }
            case EventosCliente.JUEGO_TERMINADO -> {
                boolean terminado = (boolean) evt.getNewValue();
                if (terminado) {
                    panelControles.habilitarMovimiento(false);
                    areaMensajes.append("\n⏳ Esperando información final del servidor...\n");
                }
            }
            case ClienteEstado.PROP_RESPUESTA_FINAL -> {
                RespuestaFinal respuestaFinal = (RespuestaFinal) evt.getNewValue();
                if (respuestaFinal != null) {
                    procesarFinJuego(respuestaFinal);
                }
            }
            case ClienteEstado.PROP_FRAME -> { // NUEVO
                BufferedImage frame = (BufferedImage) evt.getNewValue();
                if (frame != null) {
                    panelVideo.setFrame(frame);
                    panelVideo.repaint();
                }
            }
        }
    }
    
    private void mostrarRespuestaMovimiento(RespuestaMovimiento respuesta) {
        StringBuilder sb = new StringBuilder();
        sb.append("─────────────────────────────────────────────────\n");
        sb.append("📍 Posición: (").append(respuesta.getPacmanX())
          .append(", ").append(respuesta.getPacmanY()).append(")\n");
        sb.append("🏆 Puntaje: ").append(respuesta.getPuntaje());
        
        if (respuesta.isComioFruta()) {
            sb.append(" (+").append(respuesta.getPuntosGanados()).append(" pts) 🍒");
        }
        sb.append("\n");
        
        if (respuesta.isChocoConPared()) {
            sb.append("💥 ¡CHOCASTE CONTRA LA PARED!\n");
        }
        
        if (respuesta.isComioFruta()) {
            sb.append("😋 ¡COMISTE UNA FRUTA! Quedan: ")
              .append(respuesta.getFrutasRestantes()).append("\n");
        } else {
            sb.append("🍎 Frutas restantes: ").append(respuesta.getFrutasRestantes()).append("\n");
        }
        
        areaMensajes.append(sb.toString());
        areaMensajes.append("\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }
    
    private void procesarFinJuego(RespuestaFinal respuestaFinal) {
        try {
            ResultadoPartida resultado = new ResultadoPartida(
                respuestaFinal.getNombreJugador(),
                respuestaFinal.getPuntajeTotal(),
                respuestaFinal.getTiempoMs(),
                respuestaFinal.getFrutasComidas()
            );
            
            rankingDAO.guardarResultado(resultado);
            areaMensajes.append("✓ Resultado guardado en ranking\n");
            
        } catch (Exception e) {
            areaMensajes.append("✗ Error al guardar resultado: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            DialogoFinJuego dialogo = new DialogoFinJuego(this, respuestaFinal);
            dialogo.setVisible(true);
            
            if (dialogo.deseaVerRanking()) {
                VentanaRanking ventanaRanking = new VentanaRanking();
                ventanaRanking.mostrar();
                
                ventanaRanking.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
            } else {
                System.exit(0);
            }
        });
    }
}