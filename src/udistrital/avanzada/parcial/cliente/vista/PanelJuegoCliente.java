package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de render del lado cliente (modo espectador).
 *
 * <p>
 * Dibuja un "espejo" del tablero a partir de un {@link SnapshotTablero} enviado
 * por el servidor. No ejecuta lógica de juego ni calcula colisiones.
 * </p>
 *
 * <h3>Convenciones de dibujo</h3>
 * <ul>
 * <li>Límites del tablero: rectángulo.</li>
 * <li>Pac-Man: círculo relleno de 10 px de diámetro.</li>
 * <li>Frutas: cuadrados de 8 px (no se dibujan si ya están comidas).</li>
 * </ul>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class PanelJuegoCliente extends JPanel {

    /**
     * Último snapshot recibido del servidor (puede ser null).
     */
    private SnapshotTablero snapshot;

    /**
     * Color usado para Pac-Man.
     */
    private static final Color COLOR_PACMAN = new Color(255, 215, 0); // dorado

    /**
     * Color usado para frutas.
     */
    private static final Color COLOR_FRUTA = new Color(200, 0, 0);

    /**
     * Trazo para el borde del tablero.
     */
    private static final Stroke STROKE_BORDE = new BasicStroke(2f);

    /**
     * Crea el panel con un tamaño preferido razonable.
     */
    public PanelJuegoCliente() {
        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }

    private final Image pacUp = Iconos.cargar("PacmanArriba.png");
    private final Image pacDown = Iconos.cargar("PacmanAbajo.png");
    private final Image pacLeft = Iconos.cargar("PacmanIzquierda.png");
    private final Image pacRight = Iconos.cargar("PacmanDerecha.png");
    private final Image comidaImg = Iconos.cargar("Comida.png"); // si la usas
    private final Image paredImg = Iconos.cargar("Pared.png");  // si la usas

    /**
     * Establece el snapshot que será usado para el siguiente repintado.
     *
     * @param snapshot estado compacto enviado por el servidor (puede ser null)
     */
    public void setSnapshot(SnapshotTablero snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (snapshot == null) {
            dibujarMensaje((Graphics2D) g, "Esperando datos del servidor…");
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1) Dibujar límites
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(STROKE_BORDE);
            int minX = snapshot.getLimiteMinX();
            int minY = snapshot.getLimiteMinY();
            int ancho = snapshot.getLimiteMaxX() - snapshot.getLimiteMinX();
            int alto = snapshot.getLimiteMaxY() - snapshot.getLimiteMinY();
            g2.drawRect(minX, minY, Math.max(ancho, 0), Math.max(alto, 0));

            // 2) Dibujar Pac-Man
            g2.setColor(COLOR_PACMAN);
            int px = snapshot.getPacmanX();
            int py = snapshot.getPacmanY();
            int diam = 10;
            g2.fillOval(px - diam / 2, py - diam / 2, diam, diam);

            // 3) Dibujar frutas
            g2.setColor(COLOR_FRUTA);
            int sz = 8;
            int n = snapshot.getNumFrutas();
            for (int i = 0; i < n; i++) {
                if (snapshot.isFrutaComida(i)) {
                    continue;
                }
                int fx = snapshot.getFrutaX(i);
                int fy = snapshot.getFrutaY(i);
                g2.fillRect(fx - sz / 2, fy - sz / 2, sz, sz);
            }

            // 4) Puntaje (opcional, esquina superior)
            g2.setColor(Color.BLACK);
            g2.drawString("Puntaje: " + snapshot.getPuntaje(), minX + 8, minY + 16);
            
            

        } finally {
            g2.dispose();
        }
    }

    /**
     * Dibuja un mensaje centrado cuando no hay datos aún.
     *
     * @param g2 contexto gráfico
     * @param msg texto a mostrar
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
