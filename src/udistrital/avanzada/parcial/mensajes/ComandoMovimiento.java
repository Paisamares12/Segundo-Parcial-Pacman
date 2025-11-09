package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;

/**
 * Mensaje enviado por el cliente al servidor para solicitar un movimiento en
 * una dirección específica.
 *
 * <p>
 * Contiene solo el nombre de la dirección, sin incluir la lógica del juego. El
 * servidor traduce este comando en una acción sobre su modelo (por ejemplo,
 * mover a Pac-Man 4 píxeles hacia la derecha).
 * </p>
 *
 * <h3>Uso típico</h3>
 * <ul>
 * <li>El cliente crea una instancia con la dirección deseada.</li>
 * <li>El controlador del cliente la envía por el socket al servidor.</li>
 * <li>El servidor la recibe y ejecuta el movimiento.</li>
 * </ul>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-09
 */
public class ComandoMovimiento implements Serializable {

    /**
     * Dirección solicitada (por ejemplo, "ARRIBA", "ABAJO", etc.).
     */
    private final String direccion;

    /**
     * Crea un nuevo comando de movimiento.
     *
     * @param direccion nombre de la dirección (debe coincidir con el enum
     * Direccion)
     */
    public ComandoMovimiento(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el nombre de la dirección solicitada.
     *
     * @return texto con la dirección (en mayúsculas)
     */
    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "ComandoMovimiento{" + "direccion='" + direccion + '\'' + '}';
    }
}
