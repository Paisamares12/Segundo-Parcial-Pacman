package udistrital.avanzada.parcial.servidor.control;

import udistrital.avanzada.parcial.servidor.modelo.*;
import java.util.Iterator;
import java.util.List;

/**
 * Controlador responsable del "motor" del juego en el servidor.
 *
 * <p>
 * Ejecuta un game-loop que:
 * <ul>
 *   <li>Mueve a Pac-Man según la dirección actual (recibida desde la red).</li>
 *   <li>Detecta colisiones Pac-Man <-> Fruta y actualiza el puntaje.</li>
 *   <li>Notifica a la capa de interfaz del servidor para refrescar el tablero / HUD.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Importante: <b>no</b> realiza operaciones de red ni de persistencia. Las clases
 * encargadas de red (por ejemplo {@code ManejadorCliente}) deben:
 * <ul>
 *   <li>invocar {@link #setDireccion(Direccion)} al recibir comandos del cliente</li>
 *   <li>consultar {@link #getEstadoActual()} periódicamente para construir y enviar snapshots</li>
 * </ul>
 * </p>
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 1.1
 * @since 2025-11-11
 */
public class ControlJuego {

    /** Intervalo del game loop en milisegundos. Ajustable a gusto. */
    private static final long TICK_MS = 100L;

    private final EstadoJuego estado;
    private final ControlInterfazServidor controlInterfaz; // puede ser null si no hay UI servidor

    private Thread hiloJuego;
    private volatile boolean jugando = false;

    /** Dirección actual del jugador (manejada por la capa de red). */
    private volatile Direccion direccionActual = null;

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
     * Inicia el motor del juego. Si ya está iniciado, no hace nada.
     */
    public synchronized void iniciarJuego() {
        if (jugando) return;
        jugando = true;

        hiloJuego = new Thread(() -> {
            long last = System.currentTimeMillis();
            while (jugando) {
                long now = System.currentTimeMillis();
                long elapsed = now - last;
                if (elapsed >= TICK_MS) {
                    try {
                        actualizarTick();
                    } catch (Throwable t) {
                        // Nunca permitir que una excepción detenga el motor.
                        System.err.println("Error en motor de juego: " + t.getMessage());
                        t.printStackTrace();
                    }
                    last = now;
                } else {
                    try {
                        Thread.sleep(Math.max(1, TICK_MS - elapsed));
                    } catch (InterruptedException ignored) {
                        // ignorar interrupciones salvo que se vaya a detener explícitamente
                    }
                }
            }
        }, "MotorJuego-Thread");
        hiloJuego.setDaemon(true);
        hiloJuego.start();
    }

    /**
     * Detiene el motor de juego de forma ordenada.
     */
    public synchronized void detenerJuego() {
        jugando = false;
        if (hiloJuego != null) {
            hiloJuego.interrupt();
            try {
                hiloJuego.join(200);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Actualización por tick (ejecutada en el hilo del motor).
     *
     * - Mueve Pac-Man según {@link #direccionActual} usando {@link ConstantesJuego#PASO_PIXELES}.
     * - Evita salirse de los límites usando {@link LimitesTablero}.
     * - Detecta colisiones con frutas (usa {@link ConstantesJuego#RADIO_COLISION}).
     * - Marca frutas como comidas y suma puntaje en {@link EstadoJuego}.
     * - Pide a {@link ControlInterfazServidor} refrescar la vista si está presente.
     */
    private synchronized void actualizarTick() {
        // 1) Mover Pac-Man
        Pacman pac = estado.getPacman();
        if (pac != null) {
            moverPacmanPorTick(pac);
        }

        // 2) Detectar colisiones con frutas (y actualizar puntaje)
        detectarColisionesFrutas();

        // 3) Notificar a la UI del servidor (si existe)
        if (controlInterfaz != null) {
            // refresca solo el tablero (la UI puede preguntar puntaje si necesita)
            controlInterfaz.refrescarTablero();
            // actualiza HUD (tiempo no es modelado en EstadoJuego en esta versión -> 0)
            controlInterfaz.actualizarHUD(estado.getPuntaje(), 0L);
        }
    }

    /**
     * Mueve la posición de Pac-Man según la dirección actual y PASO_PIXELES.
     *
     * @param pac instancia de Pacman cuyo posicion se modificará
     */
    private void moverPacmanPorTick(Pacman pac) {
        if (direccionActual == null) return; // no hay movimiento solicitado

        Posicion pos = pac.getPosicion();
        if (pos == null) return;

        int paso = ConstantesJuego.PASO_PIXELES;

        // Direcciones en tu modelo usan dx()/dy() (enteros -1/0/1)
        int dx = direccionActual.dx() * paso;
        int dy = direccionActual.dy() * paso;

        int nx = pos.getX() + dx;
        int ny = pos.getY() + dy;

        // Respetar límites del tablero
        LimitesTablero limites = estado.getLimites();
        if (limites != null) {
            nx = Math.max(limites.getMinX(), Math.min(nx, limites.getMaxX()));
            ny = Math.max(limites.getMinY(), Math.min(ny, limites.getMaxY()));
        }

        pos.setX(nx);
        pos.setY(ny);
        // actualizar en el objeto Pacman
        pac.setPosicion(pos);
    }

    /**
     * Recorre las frutas y determina si alguna fue comida por Pac-Man.
     * Cuando una fruta es comida, marca la fruta y acumula puntaje.
     */
    private void detectarColisionesFrutas() {
        Pacman pac = estado.getPacman();
        if (pac == null) return;

        Posicion posPac = pac.getPosicion();
        if (posPac == null) return;

        List<Fruta> frutas = estado.getFrutas();
        if (frutas == null || frutas.isEmpty()) return;

        Iterator<Fruta> it = frutas.iterator();
        while (it.hasNext()) {
            Fruta f = it.next();
            if (f == null) continue;
            if (f.isComida()) continue;

            Posicion posF = f.getPosicion();
            if (posF == null) continue;

            if (colisiona(posPac, posF)) {
                // marcar como comida (el modelo soporta comer())
                f.comer();
                // sumar puntaje según TipoFruta
                int puntos = f.getTipo().getPuntaje();
                estado.sumarPuntos(puntos);
                System.out.println("Pac-Man comió " + f.getTipo() + " -> +" + puntos + " pts. Total: " + estado.getPuntaje());
            }
        }
    }

    /**
     * Detección simple por proximidad usando {@link ConstantesJuego#RADIO_COLISION}.
     *
     * @param a posición A (normalmente Pac-Man)
     * @param b posición B (fruta)
     * @return true si la distancia euclidiana <= RADIO_COLISION
     */
    private boolean colisiona(Posicion a, Posicion b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        int dist2 = dx * dx + dy * dy;
        int tol = ConstantesJuego.RADIO_COLISION;
        return dist2 <= (tol * tol);
    }

    /**
     * Actualiza la dirección deseada de Pac-Man. Este método es thread-safe
     * y puede ser invocado desde hilos de red (por ejemplo {@code ManejadorCliente}).
     *
     * @param direccion nueva dirección solicitada (puede ser null para detener movimiento)
     */
    public synchronized void setDireccion(Direccion direccion) {
        this.direccionActual = direccion;
    }

    /**
     * Devuelve el estado de juego actual. La referencia es la del modelo; si
     * necesitas enviar snapshots en red, conviene construir un DTO inmutable
     * desde esta información (lo hace la capa de red).
     *
     * @return instancia de {@link EstadoJuego}
     */
    public synchronized EstadoJuego getEstadoActual() {
        return estado;
    }
}

