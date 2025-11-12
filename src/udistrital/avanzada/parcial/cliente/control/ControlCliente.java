package udistrital.avanzada.parcial.cliente.control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import udistrital.avanzada.parcial.mensajes.ComandoMovimiento;
import udistrital.avanzada.parcial.mensajes.RespuestaMovimiento;
import udistrital.avanzada.parcial.mensajes.RespuestaFinal;
import udistrital.avanzada.parcial.mensajes.SolicitudAutenticacion;
import udistrital.avanzada.parcial.mensajes.RespuestaAutenticacion;
import udistrital.avanzada.parcial.cliente.modelo.ClienteEstado;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
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

    private final EstadoClienteObservable estado;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ControlCliente(EstadoClienteObservable estado) {
        this.estado = estado;
    }

    public void conectar(String host, int puerto, String usuario, String pass) {
        try {
            socket = new Socket(host, puerto);
            estado.log("✓ Conectado al servidor " + host + ":" + puerto);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            SolicitudAutenticacion solicitud = new SolicitudAutenticacion(usuario, pass);
            out.writeObject(solicitud);
            out.flush();
            estado.log("Credenciales enviadas. Esperando respuesta...");

            Object respuestaObj = in.readObject();
            if (respuestaObj instanceof RespuestaAutenticacion respuesta) {
                if (respuesta.isExitosa()) {
                    estado.log("✓ Autenticación exitosa: " + respuesta.getMensaje());
                    estado.log("Puedes comenzar a jugar usando las flechas o WASD");
                    estado.log("═════════════════════════════════════════════════");
                    estado.log("");
                    estado.setMovHabilitado(true);
                } else {
                    estado.log("✗ Autenticación fallida: " + respuesta.getMensaje());
                    cerrarConexion();
                    estado.setMovHabilitado(false);
                }
            }

        } catch (Exception e) {
            estado.log("✗ Error al conectar: " + e.getMessage());
            cerrarConexion();
        }
    }

    private synchronized void enviarMovimiento(ComandoMovimiento cmd) {
        if (estado.isJuegoTerminado()) {
            estado.log("⚠️ El juego ya terminó. No se pueden enviar más movimientos.");
            return;
        }
        
        try {
            if (out != null && in != null) {
                // 1. Enviar comando
                out.writeObject(cmd);
                out.flush();
                
                // 2. Recibir respuesta del servidor
                Object respuestaObj = in.readObject();
                
                if (respuestaObj instanceof RespuestaMovimiento respuesta) {
                    estado.setRespuestaMovimiento(respuesta);
                    
                    // 3. NUEVO: Recibir frame del servidor
                    try {
                        int frameLength = in.readInt();
                        byte[] frameBytes = new byte[frameLength];
                        in.readFully(frameBytes);
                        
                        BufferedImage frame = ImageIO.read(new ByteArrayInputStream(frameBytes));
                        
                        // Publicar frame en el estado
                        if (estado instanceof ClienteEstado) {
                            ((ClienteEstado) estado).setFrame(frame);
                        }
                    } catch (IOException e) {
                        System.err.println("Error al recibir frame: " + e.getMessage());
                    }
                    
                    // 4. Si el juego terminó, esperar RespuestaFinal
                    if (respuesta.isJuegoTerminado()) {
                        estado.log("\n🎉 ¡Todas las frutas comidas! Recibiendo información final...\n");
                        estado.setMovHabilitado(false);
                        
                        Object finalObj = in.readObject();
                        
                        if (finalObj instanceof RespuestaFinal respuestaFinal) {
                            System.out.println("✓ RespuestaFinal recibida: " + respuestaFinal);
                            
                            if (estado instanceof ClienteEstado) {
                                ((ClienteEstado) estado).setRespuestaFinal(respuestaFinal);
                            }
                        }
                        
                        cerrarConexion();
                    }
                }
            }
        } catch (Exception e) {
            estado.log("✗ Error al enviar movimiento: " + e.getMessage());
            e.printStackTrace();
            estado.setMovHabilitado(false);
            cerrarConexion();
        }
    }

    private void cerrarConexion() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
            estado.log("Conexión cerrada.");
        } catch (Exception e) {
            estado.log("✗ Error al cerrar conexión: " + e.getMessage());
        }
    }

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