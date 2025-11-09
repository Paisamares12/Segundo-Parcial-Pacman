package udistrital.avanzada.parcial.cliente.control;

import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;
import udistrital.avanzada.parcial.mensajes.ComandoMovimiento;
import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Controlador de red del cliente.
 *
 * <p>
 * No conoce la vista. Recibe acciones (conectar/mover) desde la UI y publica
 * los resultados en {@link ClienteEstado}.</p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public class ControlCliente {

    private final ClienteEstado estado;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * El control depende SOLO del estado del cliente (modelo observable).
     */
    public ControlCliente(ClienteEstado estado) {
        this.estado = estado;
    }

    /**
     * Conecta al servidor y comienza a escuchar snapshots.
     */
    public void conectar(String host, int puerto, String usuario, String pass) {
        try {
            socket = new Socket(host, puerto);
            estado.log("Conectado al servidor " + host + ":" + puerto);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // (opcional) autenticación: enviar SolicitudAutenticacion
            estado.setMovHabilitado(true);

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

        } catch (Exception e) {
            estado.log("Error al conectar: " + e.getMessage());
        }
    }

    /**
     * Envía un comando de movimiento al servidor si hay conexión.
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

    // Acciones que la vista invoca
    public void moverArriba() {
        enviarMovimiento(new ComandoMovimiento("ARRIBA"));
    }

    public void moverAbajo() {
        enviarMovimiento(new ComandoMovimiento("ABAJO"));
    }

    public void moverIzquierda() {
        enviarMovimiento(new ComandoMovimiento("IZQUIERDA"));
    }

    public void moverDerecha() {
        enviarMovimiento(new ComandoMovimiento("DERECHA"));
    }
}
