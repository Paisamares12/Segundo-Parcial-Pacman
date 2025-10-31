package Control;
/**
 * Clase principal del proyecto Pacman con sockets.
 * 
 * <p>
 * Su función es iniciar la aplicación creando una instancia del
 * controlador principal {@link ControlLogica}, que se encarga de
 * coordinar la lógica del juego y la comunicación por sockets.
 * </p>
 * 
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-10-31
 */
public class Launcher {

    /**
     * Método principal que inicia la ejecución del programa.
     *
     * @param args argumentos de línea de comandos (no usados en esta versión)
     */
    public static void main(String[] args) {
        new ControlLogica();
    }
}
