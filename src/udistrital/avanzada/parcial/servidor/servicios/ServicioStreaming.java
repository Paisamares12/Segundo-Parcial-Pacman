/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.servicios;

import udistrital.avanzada.parcial.servidor.vista.PanelJuegoServidor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Servicio encargado de capturar frames del panel de juego para streaming.
 *
 * <p>Convierte el contenido visual del panel en bytes que pueden ser
 * transmitidos por red al cliente.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 1.0
 * @since 2025-11-11
 */
public class ServicioStreaming {

    /** Calidad de compresión JPEG (0.0 a 1.0) */
    private static final float CALIDAD_JPEG = 0.75f;

    /**
     * Captura el frame actual del panel de juego como imagen.
     *
     * @param panel panel del que capturar el frame
     * @return imagen capturada
     */
    public BufferedImage capturarFrame(PanelJuegoServidor panel) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        
        if (width <= 0 || height <= 0) {
            width = 700;
            height = 500;
        }
        
        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        panel.paint(frame.getGraphics());
        return frame;
    }

    /**
     * Codifica un frame como bytes JPEG.
     *
     * @param frame imagen a codificar
     * @return bytes de la imagen codificada
     * @throws IOException si ocurre un error de codificación
     */
    public byte[] codificarFrame(BufferedImage frame) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(frame, "jpg", baos);
        return baos.toByteArray();
    }

    /**
     * Captura y codifica un frame en un solo paso.
     *
     * @param panel panel del que capturar
     * @return bytes del frame codificado
     * @throws IOException si ocurre un error
     */
    public byte[] capturarYCodificar(PanelJuegoServidor panel) throws IOException {
        BufferedImage frame = capturarFrame(panel);
        return codificarFrame(frame);
    }
}