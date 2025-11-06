package udistrital.avanzada.parcial.modelo;

/**
 * Conjunto de constantes usadas por el modelo y los controladores del juego.
 *
 * <p>
 * Centraliza valores de configuración comunes para mantener consistencia entre
 * servidor y cliente.
 * </p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public final class ConstantesJuego {

    private ConstantesJuego() {
    }

    /**
     * Cantidad de píxeles que avanza Pac-Man por comando.
     */
    public static final int PASO_PIXELES = 4;

    /**
     * Tolerancia (en píxeles) para detectar la colisión con una fruta.
     */
    public static final int RADIO_COLISION = 10;

    /**
     * Límites por defecto del tablero.
     */
    public static final LimitesTablero LIMITE_DEFECTO
            = new LimitesTablero(0, 0, 600, 400);
}
