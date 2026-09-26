import java.util.List;

/** IMPLEMENTADOR CONCRETO: formato CSV, para abrir en una hoja de cálculo. */
public class SalidaCsv implements SalidaReporte {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void escribirTitulo(String titulo) {
        buffer.append("# ").append(titulo).append('\n');
    }

    @Override
    public void escribirLinea(String texto) {
        buffer.append(escapar(texto)).append('\n');
    }

    @Override
    public void escribirTabla(List<String> encabezados, List<List<String>> filas) {
        buffer.append(unir(encabezados)).append('\n');
        for (List<String> fila : filas) {
            buffer.append(unir(fila)).append('\n');
        }
    }

    @Override
    public void cerrar() {
        System.out.print(buffer);
    }

    @Override
    public String getFormato() { return "CSV"; }

    public String getContenido() { return buffer.toString(); }

    private String unir(List<String> celdas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < celdas.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(escapar(celdas.get(i)));
        }
        return sb.toString();
    }

    /** Si el valor trae comas o comillas, se entrecomilla como manda el CSV. */
    private String escapar(String valor) {
        if (valor.contains(",") || valor.contains("\"")) {
            return '"' + valor.replace("\"", "\"\"") + '"';
        }
        return valor;
    }
}
