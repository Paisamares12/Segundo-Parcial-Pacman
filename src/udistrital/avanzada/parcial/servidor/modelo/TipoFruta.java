package udistrital.avanzada.parcial.servidor.modelo;

/**
 * Tipos de frutas disponibles en el tablero, con su respectivo puntaje.
 *
 * <p>
 * Los valores se usan por el sistema de puntaje al ser consumidos por Pac-Man.
 * </p>
 *
 * @author Paula Martínez
 * @version 4.0
 * @since 2025-11-06
 */
public enum TipoFruta {
    CEREZA(100),
    FRESA(300),
    NARANJA(500),
    MANZANA(700),
    MELON(1000),
    GALAXIAN(2000),
    CAMPANA(3000),
    LLAVE(5000);

    private final int puntaje;

    /**
     * Crea un tipo de fruta con su puntaje asociado.
     *
     * @param puntaje cantidad de puntos otorgados al comerla
     */
    TipoFruta(int puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * @return puntaje asignado a la fruta.
     */
    public int getPuntaje() {
        return puntaje;
    }
}
