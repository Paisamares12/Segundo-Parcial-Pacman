package udistrital.avanzada.parcial.servidor.vista;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

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
 * <li>Llamar a {@link #cargarSnapshot(SnapshotTablero)} al actualizar el
 * estado.</li>
 * <li>Llamar a {@link #actualizarHUD(int, long)} y {@link #refrescar()} tras
 * cada cambio.</li>
 * </ol>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-10
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
     * Carga el snapshot más reciente para que el panel de juego lo pinte.
     *
     * @param snapshot estado compacto del tablero
     */
    public void cargarSnapshot(SnapshotTablero snapshot) {
        panelJuego.setSnapshot(snapshot);
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
