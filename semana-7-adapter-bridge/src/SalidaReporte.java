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
