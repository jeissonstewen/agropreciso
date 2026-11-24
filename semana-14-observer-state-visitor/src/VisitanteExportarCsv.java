/**
 * VISITANTE CONCRETO: vuelca la finca a CSV.
 *
 * Es el visitante que mejor demuestra el patrón: se escribió después de
 * los otros dos y no obligó a cambiar una sola línea del árbol. Una
 * operación nueva, una clase nueva.
 */
public class VisitanteExportarCsv implements VisitanteFinca {

    private final StringBuilder filas = new StringBuilder();
    private int cuantasFilas = 0;

    public VisitanteExportarCsv() {
        filas.append("tipo,nombre,hectareas,sensores,estado").append(System.lineSeparator());
    }

    @Override
    public void visitarZona(ZonaCultivo zona) {
        agregar("zona", zona.getNombre(), zona.getAreaHectareas(),
                zona.contarSensores(), "-");
    }

    @Override
    public void visitarLote(Lote lote) {
        agregar("lote", lote.getNombre(), lote.getAreaHectareas(),
                lote.contarSensores(), lote.getEstado().getNombre());
    }

    private void agregar(String tipo, String nombre, double ha, int sensores, String estado) {
        filas.append(String.format("%s,%s,%.1f,%d,%s%n", tipo, escapar(nombre), ha, sensores, estado));
        cuantasFilas++;
    }

    /** Si el nombre trae comas, se entrecomilla como manda el CSV. */
    private String escapar(String valor) {
        return valor.contains(",") ? '"' + valor + '"' : valor;
    }

    @Override
    public String getResultado() { return filas.toString(); }

    public int cuantasFilas() { return cuantasFilas; }
}
