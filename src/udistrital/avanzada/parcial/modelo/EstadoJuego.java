package udistrital.avanzada.parcial.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el estado general del juego, incluyendo a Pac-Man, las frutas, los
 * límites del tablero y el puntaje acumulado.
 *
 * <p>
 * Esta clase no gestiona entrada ni red; solo modela el estado del juego. Los
 * controladores aplican sobre ella la lógica de movimiento y colisiones.
 * </p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public class EstadoJuego {

    private final Pacman pacman;
    private final List<Fruta> frutas;
    private final LimitesTablero limites;
    private int puntaje;

    /**
     * Crea un nuevo estado de juego con los límites especificados.
     *
     * @param limites límites del tablero
     */
    public EstadoJuego(LimitesTablero limites) {
        this.limites = limites;
        this.pacman = new Pacman();
        this.frutas = new ArrayList<>();
        this.puntaje = 0;
    }

    /**
     * @return instancia de Pac-Man.
     */
    public Pacman getPacman() {
        return pacman;
    }

    /**
     * @return lista de frutas actuales.
     */
    public List<Fruta> getFrutas() {
        return frutas;
    }

    /**
     * @return límites del tablero.
     */
    public LimitesTablero getLimites() {
        return limites;
    }

    /**
     * @return puntaje acumulado.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * @param f fruta a agregar al tablero.
     */
    public void agregarFruta(Fruta f) {
        frutas.add(f);
    }

    /**
     * @param puntos cantidad de puntos a sumar.
     */
    public void sumarPuntos(int puntos) {
        puntaje += puntos;
    }

    /**
     * Verifica si todas las frutas del tablero han sido comidas.
     *
     * @return {@code true} si todas las frutas fueron comidas
     */
    public boolean todasLasFrutasComidas() {
        return frutas.stream().allMatch(Fruta::isComida);
    }
}
