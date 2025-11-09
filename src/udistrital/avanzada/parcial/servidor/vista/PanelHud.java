package udistrital.avanzada.parcial.servidor.vista;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Panel de encabezado inferior (HUD) que muestra puntaje y tiempo.
 *
 * <p>
 * No calcula el tiempo ni el puntaje: únicamente formatea y presenta los
 * valores que le provee el controlador/ventana.
 * </p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class PanelHUD extends JPanel {

    private final JLabel lblPuntaje = new JLabel("Puntaje: 0");
    private final JLabel lblTiempo = new JLabel("Tiempo: 00:00.000");

    /**
     * Crea el panel HUD con estilo simple.
     */
    public PanelHUD() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 16, 6));
        add(lblPuntaje);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(lblTiempo);
    }

    /**
     * Actualiza los valores mostrados en el HUD.
     *
     * @param puntaje puntaje acumulado
     * @param tiempoMs tiempo transcurrido en milisegundos
     */
    public void setValores(int puntaje, long tiempoMs) {
        lblPuntaje.setText("Puntaje: " + puntaje);
        lblTiempo.setText("Tiempo: " + formatear(tiempoMs));
    }

    /**
     * Formatea milisegundos en mm:ss.mmm
     *
     * @param ms milisegundos
     * @return cadena formateada
     */
    private String formatear(long ms) {
        long min = TimeUnit.MILLISECONDS.toMinutes(ms);
        long sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;
        long mil = ms % 1000;
        return String.format("%02d:%02d.%03d", min, sec, mil);
    }
}
