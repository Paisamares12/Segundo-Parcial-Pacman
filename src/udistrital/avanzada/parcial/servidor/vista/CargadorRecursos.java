/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.servidor.vista;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase utilitaria responsable de cargar y gestionar los recursos gráficos
 * del juego (imágenes).
 *
 * <p>Implementa el patrón Singleton para garantizar una única instancia de
 * carga de recursos y cacheo de imágenes.</p>
 *
 * <p>Las imágenes se cargan desde el directorio {@code /Imagenes/} en el
 * classpath y se mantienen en memoria para acceso rápido.</p>
 *
 * <h3>Recursos disponibles:</h3>
 * <ul>
 *   <li>Pac-Man en cuatro direcciones (arriba, abajo, izquierda, derecha)</li>
 *   <li>Imagen de comida (frutas)</li>
 *   <li>Imagen de pared (bordes del tablero)</li>
 * </ul>
 *
 * <p>Cumple con SOLID:</p>
 * <ul>
 *   <li><b>S - Single Responsibility:</b> Solo se encarga de cargar recursos</li>
 *   <li><b>O - Open/Closed:</b> Fácil agregar nuevos recursos sin modificar código existente</li>
 * </ul>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-11
 */
public class CargadorRecursos {

    /** Instancia única del cargador (Singleton) */
    private static CargadorRecursos instancia;

    /** Caché de imágenes cargadas */
    private final Map<String, BufferedImage> imagenes;

    /** Ruta base de los recursos de imagen */
    private static final String RUTA_BASE = "/Imagenes/";

    // Nombres de los archivos de recursos
    private static final String IMG_COMIDA = "Comida.png";
    private static final String IMG_PACMAN_ARRIBA = "PacmanArriba.png";
    private static final String IMG_PACMAN_ABAJO = "PacmanAbajo.png";
    private static final String IMG_PACMAN_IZQUIERDA = "PacmanIzquierda.png";
    private static final String IMG_PACMAN_DERECHA = "PacmanDerecha.png";
    private static final String IMG_PARED = "Pared.png";

    /**
     * Constructor privado para implementar Singleton.
     * Inicializa el caché de imágenes.
     */
    private CargadorRecursos() {
        this.imagenes = new HashMap<>();
    }

    /**
     * Obtiene la instancia única del cargador de recursos.
     *
     * @return instancia única de CargadorRecursos
     */
    public static synchronized CargadorRecursos getInstancia() {
        if (instancia == null) {
            instancia = new CargadorRecursos();
        }
        return instancia;
    }

    /**
     * Carga todas las imágenes necesarias para el juego.
     *
     * <p>Este método debe llamarse una vez al iniciar la aplicación.
     * Las imágenes se mantienen en caché para acceso rápido posterior.</p>
     *
     * @throws IOException si alguna imagen no puede ser cargada
     */
    public void cargarRecursos() throws IOException {
        System.out.println("Cargando recursos gráficos...");

        cargarImagen("comida", IMG_COMIDA);
        cargarImagen("pacman_arriba", IMG_PACMAN_ARRIBA);
        cargarImagen("pacman_abajo", IMG_PACMAN_ABAJO);
        cargarImagen("pacman_izquierda", IMG_PACMAN_IZQUIERDA);
        cargarImagen("pacman_derecha", IMG_PACMAN_DERECHA);
        cargarImagen("pared", IMG_PARED);

        System.out.println("✓ Recursos gráficos cargados exitosamente (" + imagenes.size() + " imágenes)");
    }

    /**
     * Carga una imagen específica desde el classpath.
     *
     * @param clave identificador para almacenar la imagen en caché
     * @param nombreArchivo nombre del archivo de imagen
     * @throws IOException si la imagen no puede ser cargada
     */
    private void cargarImagen(String clave, String nombreArchivo) throws IOException {
        String rutaCompleta = RUTA_BASE + nombreArchivo;
        InputStream is = getClass().getResourceAsStream(rutaCompleta);

        if (is == null) {
            throw new IOException("No se pudo encontrar el recurso: " + rutaCompleta);
        }

        BufferedImage imagen = ImageIO.read(is);
        if (imagen == null) {
            throw new IOException("No se pudo cargar la imagen: " + rutaCompleta);
        }

        imagenes.put(clave, imagen);
        System.out.println("  - Cargada: " + nombreArchivo);
    }

    /**
     * Obtiene la imagen de comida (fruta).
     *
     * @return imagen de comida, o null si no se ha cargado
     */
    public BufferedImage getImagenComida() {
        return imagenes.get("comida");
    }

    /**
     * Obtiene la imagen de Pac-Man según la dirección especificada.
     *
     * @param direccion dirección de Pac-Man (en texto: "ARRIBA", "ABAJO", etc.)
     * @return imagen correspondiente a la dirección, o imagen hacia derecha por defecto
     */
    public BufferedImage getImagenPacman(String direccion) {
        if (direccion == null || direccion.isEmpty()) {
            return imagenes.get("pacman_derecha"); // Por defecto
        }

        switch (direccion.toUpperCase()) {
            case "ARRIBA":
                return imagenes.get("pacman_arriba");
            case "ABAJO":
                return imagenes.get("pacman_abajo");
            case "IZQUIERDA":
                return imagenes.get("pacman_izquierda");
            case "DERECHA":
                return imagenes.get("pacman_derecha");
            default:
                return imagenes.get("pacman_derecha");
        }
    }

    /**
     * Obtiene la imagen de pared.
     *
     * @return imagen de pared, o null si no se ha cargado
     */
    public BufferedImage getImagenPared() {
        return imagenes.get("pared");
    }

    /**
     * Verifica si todos los recursos han sido cargados correctamente.
     *
     * @return true si todas las imágenes están disponibles
     */
    public boolean recursosDisponibles() {
        return imagenes.size() == 6 && 
               getImagenComida() != null &&
               getImagenPacman("ARRIBA") != null &&
               getImagenPared() != null;
    }
}