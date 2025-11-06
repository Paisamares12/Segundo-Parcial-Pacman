package Vista;

import javax.swing.JFrame;

/**
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-10-31
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    int numFilas = 21;
    int numColumnas = 19;
    int tamCasilla = 32; //Tamaño de la Casilla
    
    // Se calcula el ancho y alto del tablero en píxeles
    int anchoTablero = numColumnas * tamCasilla;
    int altoTablero = numFilas * tamCasilla;
    
    PanelJuego pJuego;
    
    public VentanaPrincipal() {
        initComponents();
    }
    private void initComponents() {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(anchoTablero, altoTablero);
        setResizable(false);
        setLocationRelativeTo(null);
        
        this.pJuego = new PanelJuego();
        add(this.pJuego);
        pack();
    }
    
    /**
     * Devuelve el panel principal del juego
     *
     * @return el componente {@link PanelJuego} 
     */
    public PanelJuego getPanelJuego(){
        return this.pJuego;
    }
}
