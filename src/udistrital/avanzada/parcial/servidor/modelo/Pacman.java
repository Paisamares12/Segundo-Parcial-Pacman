package udistrital.avanzada.parcial.servidor.modelo;

/**
 * Representa a Pac-Man dentro del modelo del juego del servidor.
 *
 * <p>Encapsula su posición actual en el tablero y la dirección
 * hacia la cual se está moviendo. La clase no se encarga del
 * movimiento en sí, solo de mantener el estado actual de Pac-Man,
 * que será actualizado por el {@link udistrital.avanzada.parcial.servidor.control.ControlJuego}.</p>
 *
 * <p>Esta clase cumple con el principio de responsabilidad única (SRP):
 * solo gestiona la posición y la dirección del personaje.</p>
 *<p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * 
 * @author Paula Martinez
 * @version 2.0
 * @since 2025-11-11
 */
public class Pacman {

    /** Posición actual de Pac-Man en el tablero */
    private Posicion posicion;

    /** Dirección actual de movimiento de Pac-Man */
    private Direccion direccion;

    /**
     * Crea un Pac-Man en la posición inicial por defecto (100, 100)
     * y sin dirección activa.
     */
    public Pacman() {
        this.posicion = new Posicion(100, 100);
        this.direccion = Direccion.NINGUNA;
    }

    /**
     * Retorna la posición actual de Pac-Man.
     *
     * @return objeto {@link Posicion} que indica coordenadas actuales
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /**
     * Actualiza la posición de Pac-Man en el tablero.
     *
     * @param posicion nueva posición (no nula)
     */
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    /**
     * Retorna la dirección actual del movimiento de Pac-Man.
     *
     * @return dirección actual, o {@link Direccion#NINGUNA} si está detenido
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de movimiento de Pac-Man.
     *
     * @param direccion nueva dirección, o {@link Direccion#NINGUNA} para detenerlo
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     * Mueve a Pac-Man una unidad en la dirección actual, aplicando un paso dado.
     * Este método no valida colisiones ni límites (eso lo hace el controlador).
     *
     * @param paso cantidad de unidades a desplazar
     */
    public void mover(int paso) {
        if (direccion == null || direccion == Direccion.NINGUNA) return;

        int nuevaX = posicion.getX() + direccion.dx() * paso;
        int nuevaY = posicion.getY() + direccion.dy() * paso;

        posicion.setX(nuevaX);
        posicion.setY(nuevaY);
    }

    @Override
    public String toString() {
        return "Pacman{" +
                "posicion=" + posicion +
                ", direccion=" + direccion +
                '}';
    }
}
