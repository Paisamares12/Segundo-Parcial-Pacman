package udistrital.avanzada.parcial.servidor.vista;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de dibujo del tablero del juego en el lado servidor.
 *
 * <p>
 * Renderiza los límites del tablero, la posición de Pac-Man y las frutas. Este
 * panel no toma decisiones de movimiento ni de colisión; únicamente refleja el
 * estado entregado por {@link EstadoJuego}.
 * </p>
 *
 * <h3>Convenciones de dibujo</h3>
 * <ul>
 * <li>Límites: rectángulo.</li>
 * <li>Pac-Man: círculo relleno.</li>
 * <li>Frutas: cuadrados (no se dibujan si están comidas).</li>
 * </ul>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class PanelJuegoServidor extends JPanel {

    /**
     * Último snapshot enviado por el servidor (datos compactos para pintar).
     */
    private SnapshotTablero snapshot;

    /**
     * Color del Pac-Man.
     */
    private static final Color COLOR_PACMAN = new Color(255, 215, 0);

    /**
     * Color de frutas.
     */
    private static final Color COLOR_FRUTA = new Color(200, 0, 0);

    /**
     * Trazo para bordes.
     */
    private static final Stroke STROKE_BORDE = new BasicStroke(2f);

    /**
     * Crea el panel con un tamaño preferido.
     */
    public PanelJuegoServidor() {
        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }

    /**
     * Establece el snapshot actual a dibujar. Este método lo llama
     * {@link MarcoServidor#cargarSnapshot(SnapshotTablero)}.
     *
     * @param snapshot datos del tablero (puede ser null)
     */
    public void setSnapshot(SnapshotTablero snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (snapshot == null) {
            g.setColor(Color.GRAY);
            g.drawString("Esperando snapshot del juego...", getWidth() / 3, getHeight() / 2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar límites
        g2.setColor(Color.DARK_GRAY);
        int minX = snapshot.getLimiteMinX();
        int minY = snapshot.getLimiteMinY();
        int ancho = snapshot.getLimiteMaxX() - snapshot.getLimiteMinX();
        int alto = snapshot.getLimiteMaxY() - snapshot.getLimiteMinY();
        g2.drawRect(minX, minY, ancho, alto);

        // Dibujar Pac-Man
        g2.setColor(Color.YELLOW);
        int px = snapshot.getPacmanX();
        int py = snapshot.getPacmanY();
        g2.fillOval(px - 5, py - 5, 10, 10);

        // Dibujar frutas
        g2.setColor(Color.RED);
        for (int i = 0; i < snapshot.getNumFrutas(); i++) {
            if (snapshot.isFrutaComida(i)) {
                continue;
            }
            int fx = snapshot.getFrutaX(i);
            int fy = snapshot.getFrutaY(i);
            g2.fillRect(fx - 4, fy - 4, 8, 8);
        }

        // Dibujar puntaje opcional
        g2.setColor(Color.BLACK);
        g2.drawString("Puntaje: " + snapshot.getPuntaje(), minX + 8, minY + 16);
    }

    /**
     * Dibuja un texto centrado cuando no hay estado disponible.
     *
     * @param g2 contexto gráfico
     * @param msg mensaje a pintar
     */
    private void dibujarMensaje(Graphics2D g2, String msg) {
        g2.setColor(Color.GRAY);
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(msg);
        int h = fm.getAscent();
        int x = (getWidth() - w) / 2;
        int y = (getHeight() + h) / 2;
        g2.drawString(msg, x, y);
    }
}
