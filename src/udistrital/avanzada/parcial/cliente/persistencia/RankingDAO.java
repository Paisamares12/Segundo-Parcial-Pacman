package udistrital.avanzada.parcial.cliente.persistencia;

import udistrital.avanzada.parcial.cliente.modelo.ResultadoPartida;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DAO para gestionar el archivo de acceso aleatorio con los resultados de las
 * partidas.
 *
 * <p>
 * Utiliza RandomAccessFile para almacenar y recuperar los resultados de todas
 * las partidas jugadas. Los resultados se guardan en registros de tamaño fijo
 * para permitir acceso aleatorio.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 4.0
 * @since 2025-11-11
 */
public class RankingDAO {

    /**
     * Ruta del archivo de ranking
     */
    private static final String RUTA_ARCHIVO = "src/data/ranking.dat";

    /**
     * Tamaño de cada registro en bytes
     */
    private static final int TAMANIO_REGISTRO = 256;

    /**
     * Guarda un resultado de partida en el archivo.
     *
     * @param resultado resultado a guardar
     * @throws IOException si ocurre un error de E/S
     */
    public void guardarResultado(ResultadoPartida resultado) throws IOException {
        File archivo = new File(RUTA_ARCHIVO);

        // Crear directorio si no existe
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            // Ir al final del archivo
            raf.seek(raf.length());

            // Escribir datos (total: 256 bytes)
            escribirTextoFijo(raf, resultado.getNombreJugador(), 50);  // 100 bytes
            raf.writeInt(resultado.getPuntajeTotal());                  // 4 bytes
            raf.writeLong(resultado.getTiempoMs());                     // 8 bytes
            escribirTextoFijo(raf, resultado.getFrutasComidas(), 50);  // 100 bytes
            raf.writeLong(resultado.getFechaHora().getYear());          // 8 bytes
            raf.writeInt(resultado.getFechaHora().getMonthValue());     // 4 bytes
            raf.writeInt(resultado.getFechaHora().getDayOfMonth());     // 4 bytes
            raf.writeInt(resultado.getFechaHora().getHour());           // 4 bytes
            raf.writeInt(resultado.getFechaHora().getMinute());         // 4 bytes
            raf.writeDouble(resultado.getRanking());                    // 8 bytes
            // Padding para completar 256 bytes
            for (int i = 0; i < 12; i++) {
                raf.writeByte(0);
            }
        }

        System.out.println("✓ Resultado guardado en ranking: " + resultado);
    }

    /**
     * Lee todos los resultados del archivo.
     *
     * @return lista de resultados ordenados por ranking (mejor primero)
     * @throws IOException si ocurre un error de E/S
     */
    public List<ResultadoPartida> leerTodos() throws IOException {
        List<ResultadoPartida> resultados = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            return resultados;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long cantidadRegistros = raf.length() / TAMANIO_REGISTRO;

            for (int i = 0; i < cantidadRegistros; i++) {
                raf.seek(i * TAMANIO_REGISTRO);

                ResultadoPartida resultado = new ResultadoPartida();
                resultado.setNombreJugador(leerTextoFijo(raf, 50));
                resultado.setPuntajeTotal(raf.readInt());
                resultado.setTiempoMs(raf.readLong());
                resultado.setFrutasComidas(leerTextoFijo(raf, 50));

                // Leer fecha
                int year = (int) raf.readLong();
                int month = raf.readInt();
                int day = raf.readInt();
                int hour = raf.readInt();
                int minute = raf.readInt();
                resultado.setFechaHora(LocalDateTime.of(year, month, day, hour, minute));

                // El ranking se calcula automáticamente al setear puntaje y tiempo
                raf.readDouble(); // Leer el ranking guardado (ignorar, se recalcula)

                resultados.add(resultado);
            }
        }

        // Ordenar por ranking descendente
        Collections.sort(resultados);

        return resultados;
    }

    /**
     * Escribe un texto de tamaño fijo (padding con espacios si es necesario).
     *
     * @param raf archivo de acceso aleatorio
     * @param texto texto a escribir
     * @param maxChars número máximo de caracteres
     * @throws IOException si ocurre un error de E/S
     */
    private void escribirTextoFijo(RandomAccessFile raf, String texto, int maxChars) throws IOException {
        if (texto == null) {
            texto = "";
        }

        // Truncar si es muy largo
        if (texto.length() > maxChars) {
            texto = texto.substring(0, maxChars);
        }

        // Escribir caracteres (2 bytes cada uno para Unicode)
        for (int i = 0; i < maxChars; i++) {
            if (i < texto.length()) {
                raf.writeChar(texto.charAt(i));
            } else {
                raf.writeChar(' ');
            }
        }
    }

    /**
     * Lee un texto de tamaño fijo.
     *
     * @param raf archivo de acceso aleatorio
     * @param maxChars número de caracteres a leer
     * @return texto leído (sin espacios al final)
     * @throws IOException si ocurre un error de E/S
     */
    private String leerTextoFijo(RandomAccessFile raf, int maxChars) throws IOException {
        StringBuilder sb = new StringBuilder(maxChars);
        for (int i = 0; i < maxChars; i++) {
            sb.append(raf.readChar());
        }
        return sb.toString().trim();
    }

    /**
     * Elimina todos los registros del archivo (útil para pruebas).
     *
     * @throws IOException si ocurre un error de E/S
     */
    public void limpiarRanking() throws IOException {
        File archivo = new File(RUTA_ARCHIVO);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
