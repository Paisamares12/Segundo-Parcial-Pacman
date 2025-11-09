package udistrital.avanzada.parcial.cliente.control;

import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.mensajes.ComandoMovimiento;
import udistrital.avanzada.parcial.mensajes.SnapshotTablero;
import udistrital.avanzada.parcial.mensajes.SolicitudAutenticacion;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Controlador de red del cliente.
 *
 * <p>No conoce la vista. Recibe acciones (conectar/mover) desde la UI y publica
 * los resultados en {@link ClienteEstado}. Gestiona la autenticación del usuario
 * antes de permitir el inicio del juego.</p>
 *
 * @author Paula Martinez
 * @version 2.0
 * @since 2025-11-09
 */
public class ControlCliente {

    private final ClienteEstado estado;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * El control depende SOLO del estado del cliente (modelo observable).
     * 
     * @param estado objeto ClienteEstado que mantiene el estado observable del cliente
     */
    public ControlCliente(ClienteEstado estado) {
        this.estado = estado;
    }

    /**
     * Conecta al servidor y comienza a escuchar snapshots.
     * 
     * <p>Este método establece la conexión TCP con el servidor, envía las credenciales
     * del usuario para autenticación y, si la autenticación es exitosa, inicia un hilo
     * para escuchar actualizaciones del tablero de juego.</p>
     * 
     * <p>Si la autenticación falla, cierra la conexión y deshabilita los movimientos.</p>
     * 
     * @param host dirección del servidor
     * @param puerto puerto del servidor
     * @param usuario nombre de usuario
     * @param pass contraseña del usuario
     */
    public void conectar(String host, int puerto, String usuario, String pass) {
        try {
            socket = new Socket(host, puerto);
            estado.log("Conectado al servidor " + host + ":" + puerto);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Enviar solicitud de autenticación
            SolicitudAutenticacion solicitud = new SolicitudAutenticacion(usuario, pass);
            out.writeObject(solicitud);
            out.flush();
            estado.log("Credenciales enviadas. Esperando respuesta...");

            // Esperar respuesta de autenticación
            Object respuestaObj = in.readObject();
            if (respuestaObj instanceof RespuestaAutenticacion respuesta) {
                if (respuesta.isExitosa()) {
                    estado.log("Autenticación exitosa: " + respuesta.getMensaje());
                    estado.setMovHabilitado(true);
                    iniciarListenerSnapshots();
                } else {
                    estado.log("Autenticación fallida: " + respuesta.getMensaje());
                    cerrarConexion();
                    estado.setMovHabilitado(false);
                }
            }

        } catch (Exception e) {
            estado.log("Error al conectar: " + e.getMessage());
            cerrarConexion();
        }
    }

    /**
     * Inicia el hilo que escucha los snapshots del tablero enviados por el servidor.
     * 
     * <p>Este hilo se ejecuta continuamente mientras la conexión esté activa,
     * recibiendo objetos SnapshotTablero y actualizando el estado del cliente.</p>
     */
    private void iniciarListenerSnapshots() {
        Thread listener = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Object obj = in.readObject();
                    if (obj instanceof SnapshotTablero snap) {
                        estado.setSnapshot(snap);
                    }
                }
            } catch (Exception e) {
                estado.log("Conexión cerrada: " + e.getMessage());
                estado.setMovHabilitado(false);
            }
        }, "Cliente-Listener");
        listener.setDaemon(true);
        listener.start();
    }

    /**
     * Envía un comando de movimiento al servidor si hay conexión.
     * 
     * @param cmd comando de movimiento a enviar
     */
    private void enviarMovimiento(ComandoMovimiento cmd) {
        try {
            if (out != null) {
                out.writeObject(cmd);
                out.flush();
            }
        } catch (Exception e) {
            estado.log("Error al enviar movimiento: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión con el servidor de forma segura.
     */
    private void cerrarConexion() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            estado.log("Error al cerrar conexión: " + e.getMessage());
        }
    }

    // Acciones que la vista invoca

    /**
     * Envía comando de movimiento hacia arriba.
     */
    public void moverArriba() {
        enviarMovimiento(new ComandoMovimiento("ARRIBA"));
    }

    /**
     * Envía comando de movimiento hacia abajo.
     */
    public void moverAbajo() {
        enviarMovimiento(new ComandoMovimiento("ABAJO"));
    }

    /**
     * Envía comando de movimiento hacia la izquierda.
     */
    public void moverIzquierda() {
        enviarMovimiento(new ComandoMovimiento("IZQUIERDA"));
    }

    /**
     * Envía comando de movimiento hacia la derecha.
     */
    public void moverDerecha() {
        enviarMovimiento(new ComandoMovimiento("DERECHA"));
    }
}