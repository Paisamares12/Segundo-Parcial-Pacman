package udistrital.avanzada.parcial.mensajes;

import java.util.List;
import udistrital.avanzada.parcial.servidor.modelo.*;

/**
 * Clase de utilidad encargada de construir objetos {@link SnapshotTablero}
 * a partir del estado actual del juego en el servidor.
 *
 * <p>Convierte el modelo (objetos complejos) en una estructura de datos
 * ligera y serializable que puede enviarse al cliente.</p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 1.0
 * @since 2025-11-11
 */
public class SnapshotFactory {

    /**
     * Crea un {@link SnapshotTablero} a partir del estado actual del juego.
     *
     * @param estado instancia actual de {@link EstadoJuego}
     * @return un snapshot serializable con la información necesaria
     */
    public static SnapshotTablero fromEstado(EstadoJuego estado) {
        if (estado == null) return null;

        SnapshotTablero snap = new SnapshotTablero();

        // Posición de Pac-Man
        Pacman pac = estado.getPacman();
        if (pac != null && pac.getPosicion() != null) {
            snap.setPacmanX(pac.getPosicion().getX());
            snap.setPacmanY(pac.getPosicion().getY());
            if (pac.getDireccion() != null) {
                snap.setDireccionPacman(pac.getDireccion().name());
            }
        }

        // Límites del tablero
        LimitesTablero lim = estado.getLimites();
        if (lim != null) {
            snap.setLimites(lim.getMinX(), lim.getMinY(), lim.getMaxX(), lim.getMaxY());
        }

        // Frutas
        List<Fruta> frutas = estado.getFrutas();
        if (frutas != null && !frutas.isEmpty()) {
            int n = frutas.size();
            int[] x = new int[n];
            int[] y = new int[n];
            boolean[] comidas = new boolean[n];

            for (int i = 0; i < n; i++) {
                Fruta f = frutas.get(i);
                if (f != null && f.getPosicion() != null) {
                    x[i] = f.getPosicion().getX();
                    y[i] = f.getPosicion().getY();
                    comidas[i] = f.isComida();
                }
            }
            snap.setFrutas(x, y, comidas);
        }

        // Puntaje
        snap.setPuntaje(estado.getPuntaje());

        return snap;
    }
}
