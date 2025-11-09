package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.mensajes.SnapshotTablero;

/**
 * Contrato mínimo que la UI del cliente debe implementar para ser actualizada
 * por el controlador de red, sin acoplarse a una implementación gráfica
 * concreta.
 *
 * <p>
 * Incluye operaciones de log, habilitar controles y pintar snapshots.</p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-11-09
 */
public interface IClienteUI {

    /**
     * Agrega una línea al registro visual de la interfaz.
     *
     * @param msg mensaje a mostrar
     */
    void log(String msg);

    /**
     * Habilita o deshabilita los controles de movimiento en la UI.
     *
     * @param habilitado true para habilitar; false para deshabilitar
     */
    void habilitarMovimiento(boolean habilitado);

    /**
     * Carga un snapshot de tablero recibido desde el servidor y solicita
     * repintar.
     *
     * @param snapshot estado compacto para dibujar (puede ser null)
     */
    void cargarSnapshot(SnapshotTablero snapshot);
}
