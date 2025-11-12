package udistrital.avanzada.parcial.servidor.vista;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel de dibujo del tablero del juego en el lado servidor.
 *
 * <p>
 * Renderiza los límites del tablero, la posición de Pac-Man y las frutas
 * utilizando imágenes cargadas desde los recursos del proyecto. Este panel no
 * toma decisiones de movimiento ni de colisión; únicamente refleja el estado
 * entregado por
 * {@link udistrital.avanzada.parcial.servidor.modelo.EstadoJuego}.
 * </p>
 *
 * <h3>Convenciones de dibujo</h3>
 * <ul>
 * <li>Límites: imágenes de pared repetidas formando el borde</li>
 * <li>Pac-Man: imagen correspondiente a su dirección actual</li>
 * <li>Frutas: imagen de comida (no se dibujan si están comidas)</li>
 * </ul>
 *
 * <p>
 * Las imágenes se cargan mediante {@link CargadorRecursos} y se escalan
 * apropiadamente para el tamaño del panel.</p>
 *
 * Modificado: Juan Ariza y Juan Sebastián Bravo Rojas
 *
 * @author Paula Martinez
 * @version 2.0
 * @since 2025-11-11
 */
public class PanelJuegoServidor extends JPanel {

    /**
     * Último snapshot enviado por el servidor (datos compactos para pintar)
     */
    private SnapshotTablero snapshot;

    /**
     * Cargador de recursos gráficos
     */
    private final CargadorRecursos recursos;

    /**
     * Tamaño para renderizar Pac-Man (en píxeles)
     */
    private static final int TAMANIO_PACMAN = 20;

    /**
     * Tamaño para renderizar frutas (en píxeles)
     */
    private static final int TAMANIO_FRUTA = 16;

    /**
     * Tamaño de los bloques de pared (en píxeles)
     */
    private static final int TAMANIO_PARED = 20;

    /**
     * Color de fondo del tablero
     */
    private static final Color COLOR_FONDO = new Color(0, 0, 0);

    /**
     * Color del texto
     */
    private static final Color COLOR_TEXTO = new Color(255, 255, 0);

    /**
     * Crea el panel con un tamaño preferido y carga los recursos gráficos.
     */
    public PanelJuegoServidor() {
        setPreferredSize(new Dimension(700, 500));
        setBackground(COLOR_FONDO);
        setDoubleBuffered(true);

        // Obtener el cargador de recursos
        recursos = CargadorRecursos.getInstancia();

        // Intentar cargar recursos
        try {
            if (!recursos.recursosDisponibles()) {
                recursos.cargarRecursos();
            }
        } catch (Exception e) {
            System.err.println("⚠ Advertencia: No se pudieron cargar los recursos gráficos");
            System.err.println("  Se usarán formas geométricas simples como respaldo");
            e.printStackTrace();
        }
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
            dibujarMensajeEspera(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 1. Dibujar bordes del tablero con imágenes de pared
        dibujarBordes(g2);

        // 2. Dibujar frutas
        dibujarFrutas(g2);

        // 3. Dibujar Pac-Man (encima de todo)
        dibujarPacman(g2);

        // 4. Dibujar información adicional
        dibujarInfo(g2);
    }

    /**
     * Dibuja los bordes del tablero usando la imagen de pared.
     *
     * @param g2 contexto gráfico 2D
     */
    private void dibujarBordes(Graphics2D g2) {
        int minX = snapshot.getLimiteMinX();
        int minY = snapshot.getLimiteMinY();
        int maxX = snapshot.getLimiteMaxX();
        int maxY = snapshot.getLimiteMaxY();

        BufferedImage imgPared = recursos.getImagenPared();

        if (imgPared != null) {
            // Borde superior
            for (int x = minX; x <= maxX; x += TAMANIO_PARED) {
                g2.drawImage(imgPared, x, minY - TAMANIO_PARED, TAMANIO_PARED, TAMANIO_PARED, null);
            }

            // Borde inferior
            for (int x = minX; x <= maxX; x += TAMANIO_PARED) {
                g2.drawImage(imgPared, x, maxY, TAMANIO_PARED, TAMANIO_PARED, null);
            }

            // Borde izquierdo
            for (int y = minY; y <= maxY; y += TAMANIO_PARED) {
                g2.drawImage(imgPared, minX - TAMANIO_PARED, y, TAMANIO_PARED, TAMANIO_PARED, null);
            }

            // Borde derecho
            for (int y = minY; y <= maxY; y += TAMANIO_PARED) {
                g2.drawImage(imgPared, maxX, y, TAMANIO_PARED, TAMANIO_PARED, null);
            }

            // Esquinas
            g2.drawImage(imgPared, minX - TAMANIO_PARED, minY - TAMANIO_PARED, TAMANIO_PARED, TAMANIO_PARED, null);
            g2.drawImage(imgPared, maxX, minY - TAMANIO_PARED, TAMANIO_PARED, TAMANIO_PARED, null);
            g2.drawImage(imgPared, minX - TAMANIO_PARED, maxY, TAMANIO_PARED, TAMANIO_PARED, null);
            g2.drawImage(imgPared, maxX, maxY, TAMANIO_PARED, TAMANIO_PARED, null);

        } else {
            // Respaldo: dibujar rectángulo simple
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3f));
            int ancho = maxX - minX;
            int alto = maxY - minY;
            g2.drawRect(minX, minY, ancho, alto);
        }
    }

    /**
     * Dibuja todas las frutas no comidas usando la imagen de comida.
     *
     * @param g2 contexto gráfico 2D
     */
    private void dibujarFrutas(Graphics2D g2) {
        BufferedImage imgComida = recursos.getImagenComida();

        for (int i = 0; i < snapshot.getNumFrutas(); i++) {
            if (snapshot.isFrutaComida(i)) {
                continue; // No dibujar frutas comidas
            }

            int fx = snapshot.getFrutaX(i);
            int fy = snapshot.getFrutaY(i);

            if (imgComida != null) {
                // Dibujar imagen centrada en la posición
                int x = fx - (TAMANIO_FRUTA / 2);
                int y = fy - (TAMANIO_FRUTA / 2);
                g2.drawImage(imgComida, x, y, TAMANIO_FRUTA, TAMANIO_FRUTA, null);
            } else {
                // Respaldo: dibujar cuadrado rojo
                g2.setColor(Color.RED);
                g2.fillRect(fx - 4, fy - 4, 8, 8);
            }
        }
    }

    /**
     * Dibuja a Pac-Man usando la imagen correspondiente a su dirección actual.
     *
     * @param g2 contexto gráfico 2D
     */
    private void dibujarPacman(Graphics2D g2) {
        int px = snapshot.getPacmanX();
        int py = snapshot.getPacmanY();
        String direccion = snapshot.getDireccionPacman();

        BufferedImage imgPacman = recursos.getImagenPacman(direccion);

        if (imgPacman != null) {
            // Dibujar imagen centrada en la posición
            int x = px - (TAMANIO_PACMAN / 2);
            int y = py - (TAMANIO_PACMAN / 2);
            g2.drawImage(imgPacman, x, y, TAMANIO_PACMAN, TAMANIO_PACMAN, null);
        } else {
            // Respaldo: dibujar círculo amarillo
            g2.setColor(Color.YELLOW);
            g2.fillOval(px - 10, py - 10, 20, 20);
        }
    }

    /**
     * Dibuja información adicional en pantalla (puntaje y coordenadas).
     *
     * @param g2 contexto gráfico 2D
     */
    private void dibujarInfo(Graphics2D g2) {
        g2.setColor(COLOR_TEXTO);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        int minX = snapshot.getLimiteMinX();
        int minY = snapshot.getLimiteMinY();

        // Información del juego
        g2.drawString("Puntaje: " + snapshot.getPuntaje(), minX + 8, minY - 5);

        // Información de debug (opcional)
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(150, 150, 150));
        String coordenadas = String.format("Pos: (%d, %d)", snapshot.getPacmanX(), snapshot.getPacmanY());
        g2.drawString(coordenadas, minX + 8, minY + 15);
    }

    /**
     * Dibuja un mensaje de espera cuando no hay snapshot disponible.
     *
     * @param g contexto gráfico
     */
    private void dibujarMensajeEspera(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String msg = "Esperando inicio del juego...";
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(msg);
        int x = (getWidth() - w) / 2;
        int y = getHeight() / 2;
        g.drawString(msg, x, y);
    }
}
