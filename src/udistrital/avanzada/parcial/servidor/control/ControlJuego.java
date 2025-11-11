package udistrital.avanzada.parcial.servidor.control;

import udistrital.avanzada.parcial.servidor.modelo.*;
import java.util.List;
import udistrital.avanzada.parcial.mensajes.SnapshotFactory;
import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

/**
 * Controlador responsable del "motor" del juego en el servidor.
 *
 * 
 * 
 * "MODIFICACION"
 * <p>
 * Maneja la lógica del juego incluyendo:
 * <ul>
 *   <li>Movimiento de Pac-Man según comandos recibidos</li>
 *   <li>Detección de colisiones con paredes</li>
 *   <li>Detección de colisiones con frutas y actualización de puntaje</li>
 *   <li>Notificación a la capa de interfaz para refrescar la vista</li>
 * </ul>
 * </p>
 *
 * TODO: Se modificó la idea de esta clase, implementación posibles pero es obligatoria?
 * <p>
 * Importante: <b>no</b> realiza operaciones de red ni de persistencia. Las clases
 * encargadas de red (por ejemplo {@code ManejadorCliente}) deben:
 * <ul>
 *   <li>invocar {@link #procesarComando(Direccion)} al recibir comandos del cliente</li>
 *   <li>consultar {@link #getEstadoActual()} para obtener información actualizada</li>
 * </ul>
 * </p>
 * 
 * Modificada: Juan Ariza
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 1.2
 * @since 2025-11-11
 */
public class ControlJuego {

    private final EstadoJuego estado;
    private final ControlInterfazServidor controlInterfaz;

    /**
     * Crea el controlador de juego.
     *
     * @param estado instancia compartida de {@link EstadoJuego} (debe venir inicializada)
     * @param controlInterfaz controlador de interfaz del servidor para refrescar la UI (opcional, puede ser null)
     */
    public ControlJuego(EstadoJuego estado, ControlInterfazServidor controlInterfaz) {
        this.estado = estado;
        this.controlInterfaz = controlInterfaz;
    }

    /**
     * Procesa un comando de movimiento de forma síncrona.
     *
     * <p>Este método ejecuta un ciclo completo de actualización:</p>
     * <ol>
     *   <li>Intenta mover a Pac-Man en la dirección especificada</li>
     *   <li>Detecta colisión con paredes (límites del tablero)</li>
     *   <li>Detecta colisión con frutas</li>
     *   <li>Actualiza el puntaje si corresponde</li>
     *   <li>Refresca la interfaz del servidor</li>
     * </ol>
     *
     * @param direccion dirección del movimiento solicitado
     * @return objeto con el resultado del movimiento (colisiones, frutas comidas, puntos)
     */
    public synchronized ResultadoMovimiento procesarComando(Direccion direccion) {
        boolean chocoConPared = false;
        int frutasComidasAntes = contarFrutasComidas();
        int puntajeAntes = estado.getPuntaje();

        // 1) Intentar mover Pac-Man
        if (direccion != null && direccion != Direccion.NINGUNA) {
            chocoConPared = intentarMoverPacman(direccion);
        }

        // 2) Detectar colisiones con frutas
        detectarColisionesFrutas();

        // 3) Calcular resultados
        int frutasComidasDespues = contarFrutasComidas();
        int frutasComidas = frutasComidasDespues - frutasComidasAntes;
        int puntosGanados = estado.getPuntaje() - puntajeAntes;

        // 4) Refrescar interfaz del servidor
        if (controlInterfaz != null) {
            actualizarVista();
        }

        return new ResultadoMovimiento(chocoConPared, frutasComidas, puntosGanados);
    }

    /**
     * Intenta mover a Pac-Man en la dirección indicada.
     *
     * <p>Aplica el desplazamiento según {@link ConstantesJuego#PASO_PIXELES}
     * y verifica si la nueva posición está dentro de los límites. Si está
     * fuera, NO mueve a Pac-Man y retorna true indicando colisión con pared.</p>
     *
     * @param direccion dirección del movimiento
     * @return true si chocó con una pared, false si el movimiento fue válido
     */
    private boolean intentarMoverPacman(Direccion direccion) {
        Pacman pac = estado.getPacman();
        if (pac == null || pac.getPosicion() == null) {
            return false;
        }

        Posicion pos = pac.getPosicion();
        int paso = ConstantesJuego.PASO_PIXELES;

        // Calcular nueva posición
        int dx = direccion.dx() * paso;
        int dy = direccion.dy() * paso;
        int nx = pos.getX() + dx;
        int ny = pos.getY() + dy;

        // Verificar límites
        LimitesTablero limites = estado.getLimites();
        if (limites != null) {
            // Detectar colisión con pared ANTES de mover
            if (nx < limites.getMinX() || nx > limites.getMaxX() ||
                ny < limites.getMinY() || ny > limites.getMaxY()) {
                // Colisión con pared - NO mover
                System.out.println("¡Colisión con pared! Posición intentada: (" + nx + "," + ny + ")");
                return true;
            }
        }

        // Movimiento válido - actualizar posición
        pos.setX(nx);
        pos.setY(ny);
        pac.setDireccion(direccion);
        return false;
    }

    /**
     * Detecta colisiones entre Pac-Man y las frutas, marcándolas como
     * comidas y acumulando el puntaje correspondiente.
     */
    private void detectarColisionesFrutas() {
        Pacman pac = estado.getPacman();
        if (pac == null || pac.getPosicion() == null) {
            return;
        }

        Posicion posPac = pac.getPosicion();
        List<Fruta> frutas = estado.getFrutas();
        if (frutas == null || frutas.isEmpty()) {
            return;
        }

        for (Fruta f : frutas) {
            if (f == null || f.isComida()) {
                continue;
            }

            Posicion posF = f.getPosicion();
            if (posF == null) {
                continue;
            }

            if (colisiona(posPac, posF)) {
                f.comer();
                int puntos = f.getTipo().getPuntaje();
                estado.sumarPuntos(puntos);
                System.out.println("¡Pac-Man comió " + f.getTipo() + "! +" + puntos + " pts. Total: " + estado.getPuntaje());
            }
        }
    }

    /**
     * Verifica si dos posiciones colisionan según el radio de colisión
     * definido en {@link ConstantesJuego#RADIO_COLISION}.
     *
     * @param a primera posición (normalmente Pac-Man)
     * @param b segunda posición (fruta)
     * @return true si la distancia euclidiana es menor o igual al radio de colisión
     */
    private boolean colisiona(Posicion a, Posicion b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        int dist2 = dx * dx + dy * dy;
        int tol = ConstantesJuego.RADIO_COLISION;
        return dist2 <= (tol * tol);
    }

    /**
     * Cuenta cuántas frutas han sido comidas en el estado actual.
     *
     * @return cantidad de frutas comidas
     */
    private int contarFrutasComidas() {
        return (int) estado.getFrutas().stream()
                .filter(Fruta::isComida)
                .count();
    }

    /**
     * Actualiza la vista del servidor con el estado más reciente.
     */
    private void actualizarVista() {
        SnapshotTablero snapshot = SnapshotFactory.fromEstado(estado);
        controlInterfaz.cargarSnapshot(snapshot);
        controlInterfaz.actualizarHUD(estado.getPuntaje(), 0L);
    }

    /**
     * Devuelve el estado de juego actual.
     *
     * @return instancia de {@link EstadoJuego}
     */
    public synchronized EstadoJuego getEstadoActual() {
        return estado;
    }

    /**
     * Verifica si el juego ha terminado (todas las frutas comidas).
     *
     * @return true si todas las frutas fueron comidas
     */
    public synchronized boolean juegoTerminado() {
        return estado.todasLasFrutasComidas();
    }

    /**
     * Obtiene la cantidad de frutas que aún no han sido comidas.
     *
     * @return cantidad de frutas restantes
     */
    public synchronized int getFrutasRestantes() {
        return (int) estado.getFrutas().stream()
                .filter(f -> !f.isComida())
                .count();
    }
}

