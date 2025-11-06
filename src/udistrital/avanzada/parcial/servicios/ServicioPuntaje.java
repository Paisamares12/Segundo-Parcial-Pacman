package udistrital.avanzada.parcial.servicios;

import udistrital.avanzada.parcial.modelo.*;

/**
 * Servicio de dominio para el cálculo y actualización del puntaje.
 *
 * <p>
 * Su responsabilidad es sumar al marcador según el {@link TipoFruta} de cada
 * fruta comida. No interactúa con UI ni con red.</p>
 *
 * <p>
 * Uso típico: tras detectar colisiones y marcar frutas como comidas, invocar
 * {@link #acreditarPorFrutasComidas(EstadoJuego)} para acumular los puntos
 * correspondientes.</p>
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-11-06
 */
public class ServicioPuntaje {

    /**
     * Recorre las frutas del estado; por cada fruta marcada como comida,
     * acredita su puntaje al marcador acumulado del juego.
     *
     * <p>
     * No vuelve a acreditar una fruta ya comida si se llama repetidamente: el
     * modelo debe garantizar que una fruta comida no “des-coma”. Si necesitas
     * idempotencia total, mueve las frutas comidas a otra colección o
     * remuévelas del tablero tras acreditar.</p>
     *
     * @param estado estado del juego (no nulo)
     * @return puntos acreditados en esta llamada
     */
    public int acreditarPorFrutasComidas(EstadoJuego estado) {
        int sumados = 0;
        for (Fruta f : estado.getFrutas()) {
            if (f.isComida()) {
                sumados += f.getTipo().getPuntaje();
            }
        }
        if (sumados > 0) {
            estado.sumarPuntos(sumados);
        }
        return sumados;
    }

    /**
     * Acredita el puntaje de una fruta individual y la marca como comida (si
     * aún no lo estaba).
     *
     * @param estado estado del juego (no nulo)
     * @param fruta fruta objetivo (no nula)
     * @return puntos sumados (0 si ya estaba comida)
     */
    public int acreditarFruta(EstadoJuego estado, Fruta fruta) {
        if (fruta.isComida()) {
            return 0;
        }
        fruta.comer();
        int puntos = fruta.getTipo().getPuntaje();
        estado.sumarPuntos(puntos);
        return puntos;
    }
}
