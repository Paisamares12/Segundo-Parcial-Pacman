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

    /**
     * Panel lateral con botones/controles de movimiento.
     */
    private final PanelControles panelControles = new PanelControles();

    /**
     * Área central donde se muestran logs y actualizaciones del juego.
     */
    private final JTextArea areaMensajes = new JTextArea(25, 70);

    /**
     * Acceso a persistencia para guardar resultados de partida.
     */
    private final RankingDAO rankingDAO = new RankingDAO();

    /**
     * Controlador de red/lógica del cliente.
     */
    private ControlCliente control;

    /**
     * Estado observable (modelo) desde el cual se reciben eventos.
     */
    private EstadoClienteObservable estado;

    /**
     * Crea e inicializa la ventana principal del cliente, configurando layout,
     * estilos, componentes y texto de bienvenida.
     */
    public MarcoCliente() {
        super("🎮 Cliente Pac-Man - Control Remoto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Configuración visual del área de mensajes (estilo consola retro).
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMensajes.setBackground(new Color(30, 30, 30));
        areaMensajes.setForeground(new Color(0, 255, 0));
        areaMensajes.setCaretColor(Color.GREEN);

        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        scrollMensajes.setBorder(BorderFactory.createTitledBorder("Información del Juego"));

        // Layout: controles a la izquierda, mensajes al centro.
        add(panelControles, BorderLayout.WEST);
        add(scrollMensajes, BorderLayout.CENTER);

        // Mensaje inicial.
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

    /**
     * Asocia el controlador a la vista y propaga la referencia al panel de
     * controles.
     *
     * @param control instancia de {@link ControlCliente} que gestionará los
     * comandos.
     */
    public void setControl(ControlCliente control) {
        this.control = control;
        this.panelControles.setControl(control);
    }

    /**
     * Suscribe la vista al estado observable, removiendo previamente la
     * suscripción anterior si existía.
     *
     * @param estado instancia de {@link EstadoClienteObservable} desde donde se
     * reciben eventos.
     */
    public void setEstado(EstadoClienteObservable estado) {
        if (this.estado != null) {
            this.estado.removePropertyChangeListener(this);
        }
        this.estado = estado;
        this.estado.addPropertyChangeListener(this);
    }

    /**
     * Devuelve el componente que la capa de control usa para instalar key
     * bindings (controles de teclado).
     *
     * @return componente Swing principal del "juego" en esta vista.
     */
    public JComponent getComponenteJuego() {
        return areaMensajes;
    }

    /**
     * Muestra la ventana en pantalla.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Escucha y procesa notificaciones de cambio de propiedad provenientes del
     * estado.
     *
     * <p>
     * Maneja los siguientes eventos:
     * </p>
     *
     * <ul>
     * <li>{@link EventosCliente#RESPUESTA_MOVIMIENTO}: muestra datos del
     * movimiento.</li>
     * <li>{@link EventosCliente#MOV_HABILITADO}: habilita/deshabilita
     * botones.</li>
     * <li>{@link EventosCliente#LOG}: agrega líneas al área de mensajes.</li>
     * <li>{@link EventosCliente#JUEGO_TERMINADO}: avisa y espera datos
     * finales.</li>
     * <li>{@link ClienteEstado#PROP_RESPUESTA_FINAL}: procesa fin de juego y
     * ranking.</li>
     * </ul>
     *
     * @param evt el evento de cambio de propiedad.
     */
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
                    // Llevar el caret al final para "auto-scroll".
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

    /**
     * Muestra en el área de mensajes los datos de la última
     * {@link RespuestaMovimiento}.
     *
     * @param respuesta respuesta de movimiento recibida desde el servidor.
     */
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
     * Procesa el fin del juego: guarda el resultado y muestra el diálogo final.
     * <p>
     * Debe ejecutarse en el hilo de eventos de Swing (EDT) para la parte
     * visual. El guardado en ranking se realiza antes y puede ejecutarse fuera
     * del EDT.
     * </p>
     *
     * @param respuestaFinal información final de la partida enviada por el
     * servidor.
     */
    private void procesarFinJuego(RespuestaFinal respuestaFinal) {
        System.out.println("📊 procesarFinJuego() iniciado");

        // 1) Guardar resultado en almacenamiento (puede no ser EDT).
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

        // 2) Mostrar diálogo en el EDT.
        SwingUtilities.invokeLater(() -> {
            System.out.println("🎬 Mostrando DialogoFinJuego");

            DialogoFinJuego dialogo = new DialogoFinJuego(this, respuestaFinal);
            dialogo.setVisible(true);

            System.out.println("📊 Usuario desea ver ranking: " + dialogo.deseaVerRanking());

            // Si el usuario quiere ver el ranking, abrir ventana y cerrar app al cerrar ranking.
            if (dialogo.deseaVerRanking()) {
                VentanaRanking ventanaRanking = new VentanaRanking();
                ventanaRanking.mostrar();

                // Cerrar aplicación cuando se cierre el ranking.
                ventanaRanking.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
            } else {
                // Si no quiere ver ranking, cerrar la aplicación.
                System.exit(0);
            }
        });
    }
}
