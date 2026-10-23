/* ---------------------------------------------------------------
 * COPIA de la Semana 7 (patrones Adapter y Bridge).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-7-adapter-bridge/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.List;

/**
 * IMPLEMENTADOR del patrón Bridge.
 *
 * Define las operaciones primitivas de escritura, sin decir nada sobre el
 * contenido del reporte. Cada implementación concreta decide el FORMATO:
 * consola, Markdown o CSV.
 *
 * Es el otro eje del puente: la jerarquía de Reporte dice QUÉ se escribe,
 * esta jerarquía dice CÓMO se escribe.
 */
public interface SalidaReporte {

    void escribirTitulo(String titulo);

    void escribirLinea(String texto);

    /** Escribe una tabla con encabezados y filas ya formateadas. */
    void escribirTabla(List<String> encabezados, List<List<String>> filas);

    void cerrar();

    /** Nombre del formato: CONSOLA, MARKDOWN o CSV. */
    String getFormato();
}
