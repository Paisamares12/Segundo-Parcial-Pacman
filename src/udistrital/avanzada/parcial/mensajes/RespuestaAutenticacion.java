package udistrital.avanzada.parcial.mensajes;

import java.io.Serializable;

/**
 * Clase que representa la respuesta del servidor a una solicitud de
 * autenticación.
 *
 * <p>
 * Esta clase encapsula el resultado de la validación de credenciales, indicando
 * si la autenticación fue exitosa y proporcionando un mensaje descriptivo del
 * resultado.</p>
 * <p>
 * Modificado por Paula Martínez.
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-09
 */
public class RespuestaAutenticacion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Indica si la autenticación fue exitosa
     */
    private boolean exitosa;

    /**
     * Mensaje descriptivo del resultado de la autenticación
     */
    private String mensaje;

    /**
     * Constructor que crea una respuesta de autenticación.
     *
     * @param exitosa true si la autenticación fue exitosa, false en caso
     * contrario
     * @param mensaje mensaje descriptivo del resultado
     */
    public RespuestaAutenticacion(boolean exitosa, String mensaje) {
        this.exitosa = exitosa;
        this.mensaje = mensaje;
    }

    /**
     * Verifica si la autenticación fue exitosa.
     *
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean isExitosa() {
        return exitosa;
    }

    /**
     * Establece el resultado de la autenticación.
     *
     * @param exitosa true para indicar éxito, false para indicar fallo
     */
    public void setExitosa(boolean exitosa) {
        this.exitosa = exitosa;
    }

    /**
     * Obtiene el mensaje descriptivo.
     *
     * @return mensaje del resultado de la autenticación
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje descriptivo.
     *
     * @param mensaje mensaje del resultado
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
