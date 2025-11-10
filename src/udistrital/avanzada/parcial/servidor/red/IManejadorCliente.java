/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */package udistrital.avanzada.parcial.servidor.red;

import java.net.Socket;

/**
 * Interfaz que define el contrato para manejadores de clientes.
 * 
 * <p>Esta interfaz permite diferentes implementaciones de manejadores
 * de clientes, facilitando el testing y la extensibilidad del sistema.</p>
 * 
 * <p>Cumple con:</p>
 * <ul>
 *   <li><b>I - Interface Segregation:</b> Interfaz específica para manejo de clientes</li>
 *   <li><b>D - Dependency Inversion:</b> El servidor depende de esta abstracción</li>
 * </ul>
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-10
 */
public interface IManejadorCliente extends Runnable {
    
    /**
     * Obtiene el socket del cliente que está siendo manejado.
     * 
     * @return socket del cliente
     */
    Socket getSocket();
    
    /**
     * Cierra la conexión con el cliente de forma segura.
     */
    void cerrarConexion();
    
    /**
     * Verifica si la conexión con el cliente sigue activa.
     * 
     * @return true si la conexión está activa, false en caso contrario
     */
    boolean estaConectado();
}
