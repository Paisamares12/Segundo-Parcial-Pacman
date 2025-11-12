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
 * Modificado: Juan Ariza y Juan Sebastián Bravo Rojas
 *
 * @author Paula Martinez
 * @version 4.0
 * @since 2025-11-11
 */
public class PanelHUD extends JPanel {

    private final JLabel lblPuntaje = new JLabel("🏆 Puntaje: 0");
    private final JLabel lblTiempo = new JLabel("⏱️ Tiempo: 00:00.000");

    /**
     * Crea el panel HUD con estilo mejorado.
     */
    public PanelHUD() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
        setBackground(new Color(30, 30, 30));

        // Estilo de las etiquetas
        Font fuente = new Font("Arial", Font.BOLD, 14);
        Color colorTexto = new Color(255, 215, 0);

        lblPuntaje.setFont(fuente);
        lblPuntaje.setForeground(colorTexto);

        lblTiempo.setFont(fuente);
        lblTiempo.setForeground(colorTexto);

        add(lblPuntaje);
        add(crearSeparador());
        add(lblTiempo);
    }

    /**
     * Crea un separador vertical para el HUD.
     *
     * @return separador visual
     */
    private JLabel crearSeparador() {
        JLabel separador = new JLabel(" | ");
        separador.setForeground(Color.GRAY);
        separador.setFont(new Font("Arial", Font.BOLD, 14));
        return separador;
    }

    /**
     * Actualiza los valores mostrados en el HUD.
     *
     * @param puntaje puntaje acumulado
     * @param tiempoMs tiempo transcurrido en milisegundos
     */
    public void setValores(int puntaje, long tiempoMs) {
        lblPuntaje.setText("🏆 Puntaje: " + puntaje);
        lblTiempo.setText("⏱️ Tiempo: " + formatear(tiempoMs));
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
