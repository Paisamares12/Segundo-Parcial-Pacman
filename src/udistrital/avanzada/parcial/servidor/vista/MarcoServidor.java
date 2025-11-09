package udistrital.avanzada.parcial.servidor.vista;

import udistrital.avanzada.parcial.servidor.modelo.EstadoJuego;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del lado servidor para visualizar el juego Pac-Man.
 *
 * <p>
 * Contiene el panel de render del tablero ({@link PanelJuegoServidor}) y un HUD
 * inferior ({@link PanelHUD}) con puntaje y tiempo. No implementa lógica de
 * juego ni de red: la vista únicamente pinta y expone métodos para que el
 * controlador actualice la UI.
 * </p>
 *
 * <h3>Uso típico</h3>
 * <ol>
 * <li>Crear la ventana y mostrarla.</li>
 * <li>Vincular el {@link EstadoJuego} cuando el controlador lo tenga
 * listo.</li>
 * <li>Llamar a {@link #refrescar()} tras cada movimiento o cambio de
 * estado.</li>
 * </ol>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class MarcoServidor extends JFrame {

    /**
     * Panel responsable de dibujar el tablero.
     */
    private final PanelJuegoServidor panelJuego = new PanelJuegoServidor();

    /**
     * Panel HUD con puntaje y tiempo.
     */
    private final PanelHUD panelHUD = new PanelHUD();

    /**
     * Crea y arma la ventana del servidor.
     */
    public MarcoServidor() {
        super("Servidor Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(panelJuego, BorderLayout.CENTER);
        add(panelHUD, BorderLayout.SOUTH);

        setSize(720, 520);
        setLocationRelativeTo(null);
    }

    /**
     * Muestra la ventana.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Vincula el estado del juego para que el panel de render pueda dibujarlo.
     *
     * @param estado instancia de {@link EstadoJuego} (no nula)
     */
    public void vincularEstado(EstadoJuego estado) {
        panelJuego.setEstado(estado);
    }

    /**
     * Actualiza los textos del HUD (puntaje y tiempo).
     *
     * @param puntaje puntaje acumulado
     * @param tiempoMs tiempo transcurrido en milisegundos
     */
    public void actualizarHUD(int puntaje, long tiempoMs) {
        panelHUD.setValores(puntaje, tiempoMs);
    }

    /**
     * Solicita repintar el tablero y actualizar el HUD en pantalla.
     */
    public void refrescar() {
        panelJuego.repaint();
        panelHUD.repaint();
    }
}
