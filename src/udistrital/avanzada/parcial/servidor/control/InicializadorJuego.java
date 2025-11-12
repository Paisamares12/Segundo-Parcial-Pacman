/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.control;

import udistrital.avanzada.parcial.mensajes.SnapshotFactory;
import udistrital.avanzada.parcial.mensajes.SnapshotTablero;
import udistrital.avanzada.parcial.servidor.modelo.*;
import udistrital.avanzada.parcial.servidor.servicios.ServicioFrutas;
import udistrital.avanzada.parcial.servidor.servicios.ServicioTiempo;
import udistrital.avanzada.parcial.servidor.vista.MarcoServidor;

/**
 * Clase responsable de inicializar el estado del juego y la vista del servidor.
 *
 * <p>Configura todo lo necesario para iniciar una partida:</p>
 * <ul>
 *   <li>Crea el estado del juego con límites del tablero</li>
 *   <li>Posiciona a Pac-Man en el centro del tablero</li>
 *   <li>Coloca 4 frutas en posiciones aleatorias</li>
 *   <li>Inicializa el servicio de tiempo para cronometrar la partida</li>
 *   <li>Inicializa la ventana de visualización del servidor</li>
 *   <li>Crea los controladores necesarios</li>
 * </ul>
 *
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo se encarga de la inicialización</li>
 *   <li><b>D - Dependency Inversion:</b> Retorna abstracciones (controladores)</li>
 * </ul>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.1
 * @since 2025-11-11
 */
public class InicializadorJuego {

    /** Límites por defecto del tablero (coinciden con el tamaño del panel) */
    private static final LimitesTablero LIMITES_TABLERO = new LimitesTablero(10, 10, 690, 450);

    /**
     * Inicializa todo el sistema de juego para un nuevo cliente.
     *
     * <p>Crea y configura todos los componentes necesarios para iniciar
     * una partida completa.</p>
     *
     * @return objeto contenedor con todos los componentes inicializados
     */
    public ComponentesJuego inicializar() {
        // 1. Crear estado del juego
        EstadoJuego estado = new EstadoJuego(LIMITES_TABLERO);

        // 2. Posicionar Pac-Man en el centro del tablero
        int centroX = (LIMITES_TABLERO.getMinX() + LIMITES_TABLERO.getMaxX()) / 2;
        int centroY = (LIMITES_TABLERO.getMinY() + LIMITES_TABLERO.getMaxY()) / 2;
        estado.getPacman().setPosicion(new Posicion(centroX, centroY));
        System.out.println("Pac-Man posicionado en el centro: (" + centroX + ", " + centroY + ")");

        // 3. Colocar 4 frutas en posiciones aleatorias
        ServicioFrutas servicioFrutas = new ServicioFrutas();
        servicioFrutas.colocarCuatroFrutasUnicas(estado);
        System.out.println("4 frutas colocadas en posiciones aleatorias");

        // 4. Crear y iniciar servicio de tiempo
        ServicioTiempo servicioTiempo = new ServicioTiempo();
        servicioTiempo.iniciar();
        System.out.println("Cronómetro iniciado");

        // 5. Crear vista del servidor
        MarcoServidor vista = new MarcoServidor();

        // 6. Crear controlador de interfaz
        ControlInterfazServidor controlInterfaz = new ControlInterfazServidor(vista);

        // 7. Crear controlador de juego
        ControlJuego controlJuego = new ControlJuego(estado, controlInterfaz);

        // 8. Mostrar vista
        controlInterfaz.iniciar();

        // 9. Cargar snapshot inicial en la vista
        SnapshotTablero snapshotInicial = SnapshotFactory.fromEstado(estado);
        controlInterfaz.cargarSnapshot(snapshotInicial);
        controlInterfaz.actualizarHUD(0, 0L);

        System.out.println("Sistema de juego inicializado correctamente");

        return new ComponentesJuego(estado, vista, controlInterfaz, controlJuego, servicioTiempo);
    }

    /**
     * Clase contenedora que agrupa todos los componentes del juego.
     *
     * <p>Facilita el paso de múltiples objetos relacionados entre métodos.</p>
     */
    public static class ComponentesJuego {
        private final EstadoJuego estado;
        private final MarcoServidor vista;
        private final ControlInterfazServidor controlInterfaz;
        private final ControlJuego controlJuego;
        private final ServicioTiempo servicioTiempo;

        public ComponentesJuego(EstadoJuego estado, MarcoServidor vista,
                                ControlInterfazServidor controlInterfaz,
                                ControlJuego controlJuego,
                                ServicioTiempo servicioTiempo) {
            this.estado = estado;
            this.vista = vista;
            this.controlInterfaz = controlInterfaz;
            this.controlJuego = controlJuego;
            this.servicioTiempo = servicioTiempo;
        }

        public EstadoJuego getEstado() { return estado; }
        public MarcoServidor getVista() { return vista; }
        public ControlInterfazServidor getControlInterfaz() { return controlInterfaz; }
        public ControlJuego getControlJuego() { return controlJuego; }
        public ServicioTiempo getServicioTiempo() { return servicioTiempo; }
    }
}