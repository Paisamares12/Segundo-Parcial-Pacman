package udistrital.avanzada.parcial.cliente.control;

import udistrital.avanzada.parcial.mensajes.ComandoMovimiento;
import udistrital.avanzada.parcial.mensajes.RespuestaMovimiento;
import udistrital.avanzada.parcial.mensajes.RespuestaFinal;
import udistrital.avanzada.parcial.mensajes.SolicitudAutenticacion;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import udistrital.avanzada.parcial.cliente.api.EstadoClienteObservable;

/**
 * Controlador de red del cliente para el juego distribuido <b>Pac-Man</b>.
 *
 * <p>
 * Se encarga de gestionar toda la comunicación con el servidor:
 * </p>
 *
 * <ul>
 * <li>Establece la conexión mediante un socket.</li>
 * <li>Envía las credenciales del usuario y procesa la autenticación.</li>
 * <li>Envía los comandos de movimiento del jugador.</li>
 * <li>Recibe y procesa las respuestas del servidor (movimientos, final del
 * juego, etc.).</li>
 * </ul>
 *
 * <p>
 * Implementa el principio de inversión de dependencias (DIP) al depender de la
 * interfaz {@link EstadoClienteObservable}, lo cual permite abstraerse de la
 * implementación concreta del estado (por ejemplo, {@link ClienteEstado}).
 * </p>
 * <p>
 * Originalmente hecho por Paula Martínez, pero modificado por Juan Sebastián
 * Bravo Rojas y Juan Estevan Ariza Ortiz
 * </p>
 *
 * @author Paula Martinez
 *
 * @version 4.0
 * @since 2025-11-11
 */
public class ControlCliente {

    /**
     * Estado observable que mantiene la información actual del cliente y
     * permite registrar logs.
     */
    private final EstadoClienteObservable estado;

    /**
     * Socket de comunicación con el servidor.
     */
    private Socket socket;

    /**
     * Flujo de salida para enviar objetos al servidor.
     */
    private ObjectOutputStream out;

    /**
     * Flujo de entrada para recibir objetos del servidor.
     */
    private ObjectInputStream in;

    /**
     * Constructor que inicializa el controlador con el estado observable del
     * cliente.
     *
     * @param estado instancia del estado del cliente (observable) que recibirá
     * las actualizaciones.
     */
    public ControlCliente(EstadoClienteObservable estado) {
        this.estado = estado;
    }

    /**
     * Establece conexión con el servidor, envía las credenciales de
     * autenticación y espera la respuesta correspondiente.
     *
     * <p>
     * Si la autenticación es exitosa, habilita los movimientos en el estado del
     * cliente. Si falla, cierra la conexión y desactiva el control de
     * movimiento.
     * </p>
     *
     * @param host dirección IP o nombre del servidor.
     * @param puerto número de puerto del servidor.
     * @param usuario nombre de usuario o alias del jugador.
     * @param pass contraseña proporcionada por el jugador.
     */
    public void conectar(String host, int puerto, String usuario, String pass) {
        try {
            // 1️⃣ Establecer conexión de red con el servidor
            socket = new Socket(host, puerto);
            estado.log("✓ Conectado al servidor " + host + ":" + puerto);

            // 2️⃣ Inicializar los flujos de entrada y salida
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // 3️⃣ Enviar solicitud de autenticación
            SolicitudAutenticacion solicitud = new SolicitudAutenticacion(usuario, pass);
            out.writeObject(solicitud);
            out.flush();
            estado.log("Credenciales enviadas. Esperando respuesta...");

            // 4️⃣ Esperar y procesar respuesta del servidor
            Object respuestaObj = in.readObject();
            if (respuestaObj instanceof RespuestaAutenticacion respuesta) {
                if (respuesta.isExitosa()) {
                    // Autenticación exitosa → se permite jugar
                    estado.log("✓ Autenticación exitosa: " + respuesta.getMensaje());
                    estado.log("Puedes comenzar a jugar usando las flechas o WASD");
                    estado.log("═════════════════════════════════════════════════");
                    estado.log("");
                    estado.setMovHabilitado(true);
                } else {
                    // Falló la autenticación → cerrar conexión
                    estado.log("✗ Autenticación fallida: " + respuesta.getMensaje());
                    cerrarConexion();
                    estado.setMovHabilitado(false);
                }
            }

        } catch (Exception e) {
            // Error en la conexión o autenticación
            estado.log("✗ Error al conectar: " + e.getMessage());
            cerrarConexion();
        }
    }

    /**
     * Envía un comando de movimiento al servidor de forma sincronizada.
     *
     * <p>
     * Este método:
     * </p>
     * <ul>
     * <li>Verifica si el juego sigue activo.</li>
     * <li>Envía el comando de movimiento al servidor.</li>
     * <li>Recibe la respuesta de movimiento y la publica en el estado.</li>
     * <li>Si el servidor indica que el juego terminó, recibe también la
     * {@link RespuestaFinal}.</li>
     * </ul>
     *
     * @param cmd comando de movimiento a enviar al servidor (ARRIBA, ABAJO,
     * IZQUIERDA o DERECHA).
     */
    private synchronized void enviarMovimiento(ComandoMovimiento cmd) {
        // Verificar si el juego sigue activo
        if (estado.isJuegoTerminado()) {
            estado.log("⚠️ El juego ya terminó. No se pueden enviar más movimientos.");
            return;
        }

        try {
            // Verificar que los flujos estén disponibles
            if (out != null && in != null) {
                // 1️⃣ Enviar el comando de movimiento
                out.writeObject(cmd);
                out.flush();

                // 2️⃣ Esperar respuesta del servidor
                Object respuestaObj = in.readObject();

                if (respuestaObj instanceof RespuestaMovimiento respuesta) {
                    // Actualizar el estado del cliente con la respuesta del movimiento
                    estado.setRespuestaMovimiento(respuesta);

                    // 3️⃣ Si el juego terminó, recibir los datos finales
                    if (respuesta.isJuegoTerminado()) {
                        estado.log("\n🎉 ¡Todas las frutas comidas! Recibiendo información final...\n");
                        estado.setMovHabilitado(false);

                        // Recibir la respuesta final con la información del juego completo
                        Object finalObj = in.readObject();

                        if (finalObj instanceof RespuestaFinal respuestaFinal) {
                            System.out.println("✓ RespuestaFinal recibida: " + respuestaFinal);

                            // Solo si el estado es una instancia concreta de ClienteEstado
                            if (estado instanceof ClienteEstado) {
                                ((ClienteEstado) estado).setRespuestaFinal(respuestaFinal);
                            }
                        } else {
                            System.err.println("✗ Objeto recibido no es RespuestaFinal: "
                                    + (finalObj != null ? finalObj.getClass().getName() : "null"));
                        }

                        // 4️⃣ Cerrar la conexión al terminar
                        cerrarConexion();
                    }
                }
            }
        } catch (Exception e) {
            // Manejo de errores en comunicación
            estado.log("✗ Error al enviar movimiento: " + e.getMessage());
            e.printStackTrace();
            estado.setMovHabilitado(false);
            cerrarConexion();
        }
    }

    /**
     * Cierra ordenadamente la conexión con el servidor, liberando los recursos
     * de red y flujos abiertos.
     */
    private void cerrarConexion() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            estado.log("Conexión cerrada.");
        } catch (Exception e) {
            estado.log("✗ Error al cerrar conexión: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Métodos públicos de movimiento — llamados desde la interfaz
    // ──────────────────────────────────────────────────────────────
    /**
     * Envía un comando de movimiento hacia arriba.
     */
    public void moverArriba() {
        enviarMovimiento(new ComandoMovimiento("ARRIBA"));
    }

    /**
     * Envía un comando de movimiento hacia abajo.
     */
    public void moverAbajo() {
        enviarMovimiento(new ComandoMovimiento("ABAJO"));
    }

    /**
     * Envía un comando de movimiento hacia la izquierda.
     */
    public void moverIzquierda() {
        enviarMovimiento(new ComandoMovimiento("IZQUIERDA"));
    }

    /**
     * Envía un comando de movimiento hacia la derecha.
     */
    public void moverDerecha() {
        enviarMovimiento(new ComandoMovimiento("DERECHA"));
    }
}
