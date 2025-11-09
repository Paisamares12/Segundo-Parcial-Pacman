package udistrital.avanzada.parcial.servidor.vista;

import udistrital.avanzada.parcial.servidor.modelo.*;

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
     * Estado del juego a renderizar (inyectado desde la ventana/controlador).
     */
    private EstadoJuego estado;

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
     * Establece el estado del juego a dibujar.
     *
     * @param estado estado actual (no nulo)
     */
    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (estado == null) {
            dibujarMensaje((Graphics2D) g, "Sin estado vinculado…");
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1) Límites
            LimitesTablero lim = estado.getLimites();
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(STROKE_BORDE);
            int minX = lim.getMinX(), minY = lim.getMinY();
            int w = Math.max(0, lim.getMaxX() - lim.getMinX());
            int h = Math.max(0, lim.getMaxY() - lim.getMinY());
            g2.drawRect(minX, minY, w, h);

            // 2) Pac-Man
            g2.setColor(COLOR_PACMAN);
            int px = estado.getPacman().getPosicion().getX();
            int py = estado.getPacman().getPosicion().getY();
            int diam = 10;
            g2.fillOval(px - diam / 2, py - diam / 2, diam, diam);

            // 3) Frutas
            g2.setColor(COLOR_FRUTA);
            int sz = 8;
            for (Fruta f : estado.getFrutas()) {
                if (f.isComida()) {
                    continue;
                }
                int fx = f.getPosicion().getX();
                int fy = f.getPosicion().getY();
                g2.fillRect(fx - sz / 2, fy - sz / 2, sz, sz);
            }
        } finally {
            g2.dispose();
        }
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
