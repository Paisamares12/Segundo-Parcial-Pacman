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
 * Controlador de red del cliente.
 *
 * @author Juan Estevan Ariza Ortiz
 * @author Paula Martinez
 * @author Juan Sebastián Bravo Rojas
 * @version 3.3
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
                // Enviar comando
                out.writeObject(cmd);
                out.flush();
                
                // Esperar respuesta del servidor
                Object respuestaObj = in.readObject();
                
                if (respuestaObj instanceof RespuestaMovimiento respuesta) {
                    // Publicar la respuesta en el estado
                    estado.setRespuestaMovimiento(respuesta);
                    
                    // Si el juego terminó, esperar RespuestaFinal
                    if (respuesta.isJuegoTerminado()) {
                        estado.log("\n🎉 ¡Todas las frutas comidas! Recibiendo información final...\n");
                        estado.setMovHabilitado(false);
                        
                        // Recibir respuesta final con toda la información
                        Object finalObj = in.readObject();
                        
                        if (finalObj instanceof RespuestaFinal respuestaFinal) {
                            System.out.println("✓ RespuestaFinal recibida: " + respuestaFinal);
                            
                            // Cast a ClienteEstado para acceder al método setRespuestaFinal
                            if (estado instanceof ClienteEstado) {
                                ((ClienteEstado) estado).setRespuestaFinal(respuestaFinal);
                            }
                        } else {
                            System.err.println("✗ Objeto recibido no es RespuestaFinal: " + 
                                             (finalObj != null ? finalObj.getClass().getName() : "null"));
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