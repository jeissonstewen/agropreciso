/* ---------------------------------------------------------------
 * COPIA de la Semana 7 (patrones Adapter y Bridge).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-7-adapter-bridge/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.List;

/** IMPLEMENTADOR CONCRETO: formato legible para la terminal. */
public class SalidaConsola implements SalidaReporte {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void escribirTitulo(String titulo) {
        buffer.append("=== ").append(titulo.toUpperCase()).append(" ===").append('\n');
    }

    @Override
    public void escribirLinea(String texto) {
        buffer.append("  ").append(texto).append('\n');
    }

    @Override
    public void escribirTabla(List<String> encabezados, List<List<String>> filas) {
        buffer.append("  ").append(String.join(" | ", encabezados)).append('\n');
        for (List<String> fila : filas) {
            buffer.append("  ").append(String.join(" | ", fila)).append('\n');
        }
    }

    @Override
    public void cerrar() {
        System.out.print(buffer);
    }

    @Override
    public String getFormato() { return "CONSOLA"; }

    /** Lo escrito hasta ahora; lo usan las pruebas para verificar el formato. */
    public String getContenido() { return buffer.toString(); }
}
