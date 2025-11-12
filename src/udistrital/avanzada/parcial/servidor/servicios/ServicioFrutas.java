package udistrital.avanzada.parcial.servidor.servicios;

import udistrital.avanzada.parcial.servidor.modelo.TipoFruta;
import udistrital.avanzada.parcial.servidor.modelo.Posicion;
import udistrital.avanzada.parcial.servidor.modelo.LimitesTablero;
import udistrital.avanzada.parcial.servidor.modelo.Fruta;
import udistrital.avanzada.parcial.servidor.modelo.EstadoJuego;

import java.util.*;

/**
 * Servicio de dominio para la gestión de frutas en el tablero: selección
 * aleatoria de tipos, posicionamiento dentro de límites y verificación de
 * colisiones para marcarlas como comidas.
 *
 * <p>
 * No realiza operaciones de E/S, UI ni red. Está pensado para ser invocado
 * desde los controladores del servidor.</p>
 *
 * <p>
 * Estrategia por defecto:
 * <ul>
 * <li>Selecciona 4 tipos de fruta distintos (siempre que existan).</li>
 * <li>Ubica cada fruta en una posición aleatoria dentro de los límites.</li>
 * <li>Permite verificar colisiones contra Pac-Man con un umbral
 * configurable.</li>
 * </ul>
 * </p>
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-06
 */
public class ServicioFrutas {

    private final Random random = new Random();

    /**
     * Elimina cualquier fruta previa del estado y coloca 4 frutas de tipos
     * distintos en posiciones aleatorias dentro de los límites.
     *
     * @param estado estado del juego (no nulo)
     */
    public void colocarCuatroFrutasUnicas(EstadoJuego estado) {
        colocarFrutasUnicas(estado, 4);
    }

    /**
     * Elimina cualquier fruta previa del estado y coloca {@code cantidad}
     * frutas de tipos distintos (hasta donde el enum lo permita).
     *
     * @param estado estado del juego (no nulo)
     * @param cantidad número de frutas a colocar (>=1)
     */
    public void colocarFrutasUnicas(EstadoJuego estado, int cantidad) {
        estado.getFrutas().clear(); // Nota: si necesitas inmutabilidad, usa un método del Estado

        List<TipoFruta> tipos = new ArrayList<>(Arrays.asList(TipoFruta.values()));
        Collections.shuffle(tipos, random);

        int n = Math.min(cantidad, tipos.size());
        for (int i = 0; i < n; i++) {
            Posicion pos = posicionAleatoria(estado.getLimites());
            estado.getFrutas().add(new Fruta(tipos.get(i), pos));
        }
    }

    /**
     * Verifica colisiones de Pac-Man contra frutas no comidas. Si alguna
     * colisiona (distancia Euclidiana <= umbral), la marca como comida y
     * retorna cuántas fueron comidas en esta verificación.
     *
     * @param estado estado del juego (no nulo)
     * @param umbralColision distancia máxima para considerar colisión (px)
     * @return cantidad de frutas comidas en esta verificación
     */
    public int verificarColisionesYMarcar(EstadoJuego estado, int umbralColision) {
        int comidas = 0;
        final int px = estado.getPacman().getPosicion().getX();
        final int py = estado.getPacman().getPosicion().getY();

        for (Fruta f : estado.getFrutas()) {
            if (f.isComida()) {
                continue;
            }
            int dx = px - f.getPosicion().getX();
            int dy = py - f.getPosicion().getY();
            if (Math.hypot(dx, dy) <= umbralColision) {
                f.comer();
                comidas++;
            }
        }
        return comidas;
    }

    /**
     * Genera una posición aleatoria válida dentro de los límites del tablero.
     *
     * @param lim límites del tablero (no nulo)
     * @return posición aleatoria dentro del rectángulo de juego
     */
    public Posicion posicionAleatoria(LimitesTablero lim) {
        int x = lim.getMinX() + random.nextInt(Math.max(1, lim.getMaxX() - lim.getMinX() + 1));
        int y = lim.getMinY() + random.nextInt(Math.max(1, lim.getMaxY() - lim.getMinY() + 1));
        return new Posicion(x, y);
    }
}
