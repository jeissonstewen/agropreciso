import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** IMPLEMENTADOR CONCRETO: formato Markdown, para pegar en el informe. */
public class SalidaMarkdown implements SalidaReporte {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void escribirTitulo(String titulo) {
        buffer.append("## ").append(titulo).append("\n\n");
    }

    @Override
    public void escribirLinea(String texto) {
        buffer.append("- ").append(texto).append('\n');
    }

    @Override
    public void escribirTabla(List<String> encabezados, List<List<String>> filas) {
        buffer.append("| ").append(String.join(" | ", encabezados)).append(" |\n");
        List<String> separador = new ArrayList<>(Collections.nCopies(encabezados.size(), "---"));
        buffer.append("| ").append(String.join(" | ", separador)).append(" |\n");
        for (List<String> fila : filas) {
            buffer.append("| ").append(String.join(" | ", fila)).append(" |\n");
        }
    }

    @Override
    public void cerrar() {
        System.out.print(buffer);
    }

    @Override
    public String getFormato() { return "MARKDOWN"; }

    public String getContenido() { return buffer.toString(); }
}
