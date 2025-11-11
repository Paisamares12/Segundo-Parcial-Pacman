package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;
import udistrital.avanzada.parcial.servidor.modelo.Direccion;
import udistrital.avanzada.parcial.servidor.modelo.EstadoJuego;
import udistrital.avanzada.parcial.servidor.modelo.Fruta;

/**
 * Objeto de transferencia enviado por el servidor al cliente para actualizar la
 * vista del tablero (modo espectador).
 *
 * Contiene solo datos simples, sin referencias a clases del modelo.
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-09
 */
public class SnapshotTablero implements Serializable {

    // Coordenadas de Pac-Man
    private int pacmanX;
    private int pacmanY;

    // Límites del tablero
    private int limiteMinX;
    private int limiteMinY;
    private int limiteMaxX;
    private int limiteMaxY;

    // Información de frutas (arrays paralelos)
    private int[] frutaX;
    private int[] frutaY;
    private boolean[] frutaComida;

    // Puntaje actual
    private int puntaje;

    // campos nuevos en SnapshotTablero:
    private String direccionPacman; // "ARRIBA","ABAJO","IZQUIERDA","DERECHA"
// getters/setters: getDireccionPacman(), setDireccionPacman(String)

    // --- Métodos de acceso (getters y setters) ---
    public int getPacmanX() {
        return pacmanX;
    }

    public int getPacmanY() {
        return pacmanY;
    }

    public void setPacmanX(int pacmanX) {
        this.pacmanX = pacmanX;
    }

    public void setPacmanY(int pacmanY) {
        this.pacmanY = pacmanY;
    }

    public int getLimiteMinX() {
        return limiteMinX;
    }

    public int getLimiteMinY() {
        return limiteMinY;
    }

    public int getLimiteMaxX() {
        return limiteMaxX;
    }

    public int getLimiteMaxY() {
        return limiteMaxY;
    }

    public void setLimites(int minX, int minY, int maxX, int maxY) {
        this.limiteMinX = minX;
        this.limiteMinY = minY;
        this.limiteMaxX = maxX;
        this.limiteMaxY = maxY;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void setFrutas(int[] x, int[] y, boolean[] comida) {
        this.frutaX = x;
        this.frutaY = y;
        this.frutaComida = comida;
    }

    public int getNumFrutas() {
        return frutaX == null ? 0 : frutaX.length;
    }

    public int getFrutaX(int i) {
        return frutaX[i];
    }

    public int getFrutaY(int i) {
        return frutaY[i];
    }

    public boolean isFrutaComida(int i) {
        return frutaComida[i];
    }
    
    public String setDireccionPacman(){
        return direccionPacman;
    }
    

}
