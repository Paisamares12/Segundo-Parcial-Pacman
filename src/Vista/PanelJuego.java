package Vista;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
/**
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-10-31
 */
public class PanelJuego extends JPanel {
    
    private int numFilas = 21;
    private int numColumnas = 19;
    private int tamCasilla = 32; //Tamaño de la Casilla
    
    // Se calcula el ancho y alto del tablero en píxeles
    private int anchoTablero = numColumnas * tamCasilla;
    private int altoTablero = numFilas * tamCasilla;
    
    //Imagen de la pared
    private Image paredImagen;
    //Imagenes de los Pacman
    private Image pacmanArribaImagen; //Pacman Arriba
    private Image pacmanIzquierdaImagen; //Pacman Izquierda
    private Image pacmanAbajoImagen; //Pacman Abajo
    private Image pacmanDerechaImagen; //Pacman Derecha
    
    public PanelJuego(){
        initComponents();
    }
    
    public void initComponents(){
        //Darle la dimensión correspondiente al panel con los datos previamente calculados
        setPreferredSize(new Dimension(anchoTablero,altoTablero));
        //Hacer el panel de color negro
        setBackground(Color.BLACK);
        //Cargar las Imagenes
        paredImagen = new ImageIcon(getClass().getResource("./Imagenes/Pared.png")).getImage();
        pacmanAbajoImagen = new ImageIcon(getClass().getResource("./Imagenes/PacmanAbajo.png")).getImage();
        pacmanArribaImagen = new ImageIcon(getClass().getResource("./Imagenes/PacmanArriba.png")).getImage();
        pacmanDerechaImagen = new ImageIcon(getClass().getResource("./Imagenes/PacmanDerecha.png")).getImage();
        pacmanIzquierdaImagen = new ImageIcon(getClass().getResource("./Imagenes/PacmanIzquierda.png")).getImage();
    }
}
