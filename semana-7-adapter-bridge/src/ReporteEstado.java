import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ABSTRACCIÓN REFINADA: el estado actual de los lotes.
 *
 * Solo decide el contenido —un título, una línea de contexto y una tabla de
 * lecturas— y lo manda escribir. No sabe si el resultado será texto de
 * consola, Markdown o CSV.
 */
public class ReporteEstado extends Reporte {

    private final String finca;
    private final List<Lectura> lecturas;

    public ReporteEstado(SalidaReporte salida, String finca, List<Lectura> lecturas) {
        super(salida);
        this.finca    = finca;
        this.lecturas = lecturas;
    }

    @Override
    public void generar() {
        salida.escribirTitulo("Estado de " + finca);
        salida.escribirLinea("Sensores consultados: " + lecturas.size());

        List<List<String>> filas = new ArrayList<>();
        for (Lectura l : lecturas) {
            filas.add(Arrays.asList(l.getIdSensor(), l.getTipo(),
                    String.format("%.1f", l.getValor()), l.getUnidad()));
        }
        salida.escribirTabla(Arrays.asList("sensor", "tipo", "valor", "unidad"), filas);
        salida.cerrar();
    }

    @Override
    public String getNombre() { return "Reporte de estado"; }
}
