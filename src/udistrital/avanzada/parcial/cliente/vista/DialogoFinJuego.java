package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.mensajes.RespuestaFinal;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo que se muestra al finalizar el juego con los resultados.
 *
 * <p>
 * Muestra el puntaje final, tiempo y nombre del jugador, con opciones para ver
 * el ranking o salir.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-11
 */
public class DialogoFinJuego extends JDialog {

    private final RespuestaFinal respuestaFinal;
    private boolean verRanking = false;

    /**
     * Constructor del diálogo.
     *
     * @param parent ventana padre
     * @param respuestaFinal respuesta con los datos finales del juego
     */
    public DialogoFinJuego(JFrame parent, RespuestaFinal respuestaFinal) {
        super(parent, "¡Juego Terminado!", true);
        this.respuestaFinal = respuestaFinal;

        inicializarComponentes();

        setSize(450, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Inicializa los componentes del diálogo.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 248, 255));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("🎉 ¡FELICITACIONES! 🎉", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 100, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de información
        JPanel panelInfo = new JPanel(new GridLayout(5, 1, 5, 10));
        panelInfo.setBackground(new Color(240, 248, 255));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Jugador
        JLabel lblJugador = crearEtiquetaInfo("👤 Jugador: " + respuestaFinal.getNombreJugador());
        panelInfo.add(lblJugador);

        // Puntaje
        JLabel lblPuntaje = crearEtiquetaInfo("🏆 Puntaje: " + respuestaFinal.getPuntajeTotal());
        lblPuntaje.setForeground(new Color(184, 134, 11));
        panelInfo.add(lblPuntaje);

        // Tiempo
        String tiempoFormateado = formatearTiempo(respuestaFinal.getTiempoMs());
        JLabel lblTiempo = crearEtiquetaInfo("⏱️ Tiempo: " + tiempoFormateado);
        panelInfo.add(lblTiempo);

        // Frutas
        String frutas = String.join(", ", respuestaFinal.getFrutasComidas());
        JLabel lblFrutas = new JLabel("<html><b>🍒 Frutas:</b> " + frutas + "</html>");
        lblFrutas.setFont(new Font("Arial", Font.PLAIN, 14));
        panelInfo.add(lblFrutas);

        // Ranking
        double ranking = calcularRanking(respuestaFinal.getPuntajeTotal(), respuestaFinal.getTiempoMs());
        JLabel lblRanking = crearEtiquetaInfo(String.format("⭐ Ranking: %.2f pts/seg", ranking));
        lblRanking.setForeground(new Color(255, 140, 0));
        panelInfo.add(lblRanking);

        panelPrincipal.add(panelInfo, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnRanking = new JButton("📊 Ver Ranking");
        btnRanking.setFont(new Font("Arial", Font.BOLD, 14));
        btnRanking.setBackground(new Color(70, 130, 180));
        btnRanking.setForeground(Color.WHITE);
        btnRanking.setFocusPainted(false);
        btnRanking.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRanking.addActionListener(e -> {
            verRanking = true;
            dispose();
        });

        JButton btnSalir = new JButton("❌ Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(220, 20, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            verRanking = false;
            dispose();
        });

        panelBotones.add(btnRanking);
        panelBotones.add(btnSalir);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    /**
     * Crea una etiqueta con formato estándar para información.
     *
     * @param texto texto de la etiqueta
     * @return etiqueta formateada
     */
    private JLabel crearEtiquetaInfo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(47, 79, 79));
        return label;
    }

    /**
     * Formatea tiempo en formato mm:ss.mmm
     *
     * @param ms milisegundos
     * @return tiempo formateado
     */
    private String formatearTiempo(long ms) {
        long min = ms / 60000;
        long sec = (ms % 60000) / 1000;
        long mil = ms % 1000;
        return String.format("%02d:%02d.%03d", min, sec, mil);
    }

    /**
     * Calcula el ranking (puntaje / tiempo en segundos).
     *
     * @param puntaje puntaje total
     * @param tiempoMs tiempo en milisegundos
     * @return ranking calculado
     */
    private double calcularRanking(int puntaje, long tiempoMs) {
        double tiempoSegundos = tiempoMs / 1000.0;
        return tiempoSegundos > 0 ? puntaje / tiempoSegundos : 0;
    }

    /**
     * Verifica si el usuario quiere ver el ranking.
     *
     * @return true si quiere ver el ranking
     */
    public boolean deseaVerRanking() {
        return verRanking;
    }
}
