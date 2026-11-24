import java.util.LinkedHashMap;
import java.util.Map;

/**
 * VISITANTE CONCRETO: resumen de lo que hay en la finca.
 *
 * Cuenta zonas, lotes, sensores y hectáreas, y agrupa los lotes por su
 * estado del ciclo de cultivo. Nótese que para escribirlo no hizo falta
 * tocar ZonaCultivo ni Lote.
 */
public class VisitanteInventario implements VisitanteFinca {

    private int zonas  = 0;
    private int lotes  = 0;
    private int sensores = 0;
    private double hectareas = 0;

    /** Cuántos lotes hay en cada estado. LinkedHashMap para orden estable. */
    private final Map<String, Integer> porEstado = new LinkedHashMap<>();

    @Override
    public void visitarZona(ZonaCultivo zona) {
        zonas++;
    }

    @Override
    public void visitarLote(Lote lote) {
        lotes++;
        sensores  += lote.contarSensores();
        hectareas += lote.getAreaHectareas();
        porEstado.merge(lote.getEstado().getNombre(), 1, Integer::sum);
    }

    @Override
    public String getResultado() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  Zonas: %d · Lotes: %d · Sensores: %d · %.1f ha%n",
                zonas, lotes, sensores, hectareas));
        sb.append("  Lotes por estado:").append(System.lineSeparator());
        for (Map.Entry<String, Integer> e : porEstado.entrySet()) {
            sb.append(String.format("    %-16s %d%n", e.getKey(), e.getValue()));
        }
        return sb.toString();
    }

    public int getZonas()       { return zonas; }
    public int getLotes()       { return lotes; }
    public int getSensores()    { return sensores; }
    public double getHectareas(){ return hectareas; }
    public int enEstado(String nombreEstado) { return porEstado.getOrDefault(nombreEstado, 0); }
}
