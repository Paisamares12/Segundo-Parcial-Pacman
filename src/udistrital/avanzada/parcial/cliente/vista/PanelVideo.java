/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.cliente.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel que reproduce el stream de video del servidor.
 *
 * <p>Muestra los frames recibidos del servidor sin ejecutar ninguna
 * lógica de juego. Actúa como un simple reproductor de video.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 1.0
 * @since 2025-11-11
 */
public class PanelVideo extends JPanel {

    /** Frame actual a mostrar */
    private BufferedImage frame;

    /** Mensaje cuando no hay frame */
    private static final String MENSAJE_ESPERA = "Esperando transmisión del servidor...";

    /**
     * Constructor del panel de video.
     */
    public PanelVideo() {
        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    /**
     * Actualiza el frame a mostrar.
     *
     * @param frame nueva imagen a mostrar
     */
    public synchronized void setFrame(BufferedImage frame) {
        this.frame = frame;
    }

    /**
     * Obtiene el frame actual.
     *
     * @return frame actual o null
     */
    public synchronized BufferedImage getFrame() {
        return frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        BufferedImage currentFrame = getFrame();
        
        if (currentFrame != null) {
            // Dibujar el frame escalado al tamaño del panel
            g.drawImage(currentFrame, 0, 0, getWidth(), getHeight(), null);
        } else {
            // Mostrar mensaje de espera
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(MENSAJE_ESPERA);
            int x = (getWidth() - w) / 2;
            int y = getHeight() / 2;
            g.drawString(MENSAJE_ESPERA, x, y);
        }
    }
}
