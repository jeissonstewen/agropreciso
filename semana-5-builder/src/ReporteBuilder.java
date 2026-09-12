import java.util.List;

/**
 * BUILDER (interfaz) — aquí se declara el PATRÓN BUILDER.
 *
 * Define, paso a paso, las partes con las que se puede armar un reporte.
 * Cada método devuelve el propio builder para poder encadenar llamadas.
 * Las partes son opcionales: el cliente llama solo a las que necesita y al
 * final pide el producto con construir().
 *
 * Al ser una interfaz, mañana puede existir otro builder que arme el mismo
 * reporte en otra representación (por ejemplo, un archivo de texto) sin
 * cambiar ni al Director ni al código cliente.
 */
public interface ReporteBuilder {

    ReporteBuilder conTitulo(String titulo);
    ReporteBuilder conEncabezado(String finca, String responsable);
    ReporteBuilder conLecturas(List<Lectura> lecturas);
    ReporteBuilder conAlertas(List<String> alertas);
    ReporteBuilder conInventario(List<String> items);
    ReporteBuilder conResumen();

    /** Entrega el producto terminado y deja el builder listo para otro reporte. */
    ReporteFinca construir();
}
