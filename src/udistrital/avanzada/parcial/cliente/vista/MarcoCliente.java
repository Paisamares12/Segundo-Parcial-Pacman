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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Ventana principal del Cliente (solo controles y mensajes de texto).
 *
 * @author Juan Estevan Ariza Ortiz
 * @author Paula Martinez
 * @author Juan Sebastián Bravo Rojas
 * @version 4.3
 * @since 2025-11-11
 */
public class MarcoCliente extends JFrame implements PropertyChangeListener {

    private final PanelControles panelControles = new PanelControles();
    private final JTextArea areaMensajes = new JTextArea(25, 70);
    private final RankingDAO rankingDAO = new RankingDAO();

    private ControlCliente control;
    private EstadoClienteObservable estado;

    public MarcoCliente() {
        super("🎮 Cliente Pac-Man - Control Remoto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMensajes.setBackground(new Color(30, 30, 30));
        areaMensajes.setForeground(new Color(0, 255, 0));
        areaMensajes.setCaretColor(Color.GREEN);
        
        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        scrollMensajes.setBorder(BorderFactory.createTitledBorder("Información del Juego"));

        add(panelControles, BorderLayout.WEST);
        add(scrollMensajes, BorderLayout.CENTER);

        areaMensajes.append("═══════════════════════════════════════════════════\n");
        areaMensajes.append("    CLIENTE PAC-MAN - CONTROL REMOTO\n");
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
        System.out.println("🔔 Evento recibido: " + evt.getPropertyName());
        
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
                System.out.println("✓ Evento PROP_RESPUESTA_FINAL recibido");
                RespuestaFinal respuestaFinal = (RespuestaFinal) evt.getNewValue();
                if (respuestaFinal != null) {
                    System.out.println("✓ RespuestaFinal válida, procesando fin de juego");
                    procesarFinJuego(respuestaFinal);
                } else {
                    System.err.println("✗ RespuestaFinal es null");
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
    
    /**
     * Procesa el fin del juego: guarda el resultado y muestra el diálogo.
     * DEBE ejecutarse en el hilo de Swing (EDT).
     */
    private void procesarFinJuego(RespuestaFinal respuestaFinal) {
        System.out.println("📊 procesarFinJuego() iniciado");
        
        // Guardar resultado en archivo (puede hacerse fuera del EDT)
        try {
            ResultadoPartida resultado = new ResultadoPartida(
                respuestaFinal.getNombreJugador(),
                respuestaFinal.getPuntajeTotal(),
                respuestaFinal.getTiempoMs(),
                respuestaFinal.getFrutasComidas()
            );
            
            rankingDAO.guardarResultado(resultado);
            areaMensajes.append("✓ Resultado guardado en ranking\n");
            System.out.println("✓ Resultado guardado: " + resultado);
            
        } catch (Exception e) {
            areaMensajes.append("✗ Error al guardar resultado: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        
        // Mostrar diálogo en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            System.out.println("🎬 Mostrando DialogoFinJuego");
            
            DialogoFinJuego dialogo = new DialogoFinJuego(this, respuestaFinal);
            dialogo.setVisible(true);
            
            System.out.println("📊 Usuario desea ver ranking: " + dialogo.deseaVerRanking());
            
            // Si el usuario quiere ver el ranking
            if (dialogo.deseaVerRanking()) {
                VentanaRanking ventanaRanking = new VentanaRanking();
                ventanaRanking.mostrar();
                
                // Cerrar aplicación cuando se cierre el ranking
                ventanaRanking.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
            } else {
                // Si no quiere ver ranking, cerrar la aplicación
                System.exit(0);
            }
        });
    }
}