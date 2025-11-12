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
 * Modificado: Juan Ariza y Juan Sebastián Bravo Rojas
 *
 * @author Paula Martinez
 * @version 4.0
 * @since 2025-11-11
 */
public class MarcoServidor extends JFrame {

    private final PanelJuegoServidor panelJuego = new PanelJuegoServidor();
    private final PanelHUD panelHUD = new PanelHUD();

    public MarcoServidor() {
        super("🎮 Servidor Pac-Man - Tablero de Juego");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(panelJuego, BorderLayout.CENTER);
        add(panelHUD, BorderLayout.SOUTH);

        setSize(750, 570);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void mostrar() {
        setVisible(true);
    }

    public void cargarSnapshot(SnapshotTablero snapshot) {
        panelJuego.setSnapshot(snapshot);
    }

    public void actualizarHUD(int puntaje, long tiempoMs) {
        panelHUD.setValores(puntaje, tiempoMs);
    }

    public void refrescar() {
        panelJuego.repaint();
        panelHUD.repaint();
    }
    
    /**
     * NUEVO: Obtiene el panel de juego para capturar frames.
     *
     * @return panel de juego del servidor
     */
    public PanelJuegoServidor getPanelJuego() {
        return panelJuego;
    }
}