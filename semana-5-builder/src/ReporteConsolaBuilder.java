import java.util.ArrayList;
import java.util.List;

/**
 * BUILDER CONCRETO: arma un ReporteFinca para imprimir en consola.
 *
 * Guarda las partes a medida que el cliente las va pidiendo y, en
 * construir(), valida lo mínimo (el título es obligatorio) y crea el
 * producto de una sola vez. Después se reinicia solo, así que un mismo
 * builder sirve para armar varios reportes distintos.
 */
public class ReporteConsolaBuilder implements ReporteBuilder {

    private String        titulo;
    private String        finca;
    private String        responsable;
    private List<Lectura> lecturas   = new ArrayList<>();
    private List<String>  alertas    = new ArrayList<>();
    private List<String>  inventario = new ArrayList<>();
    private boolean       conResumen = false;

    @Override
    public ReporteBuilder conTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    @Override
    public ReporteBuilder conEncabezado(String finca, String responsable) {
        this.finca       = finca;
        this.responsable = responsable;
        return this;
    }

    @Override
    public ReporteBuilder conLecturas(List<Lectura> lecturas) {
        this.lecturas.addAll(lecturas);
        return this;
    }

    @Override
    public ReporteBuilder conAlertas(List<String> alertas) {
        this.alertas.addAll(alertas);
        return this;
    }

    @Override
    public ReporteBuilder conInventario(List<String> items) {
        this.inventario.addAll(items);
        return this;
    }

    @Override
    public ReporteBuilder conResumen() {
        this.conResumen = true;
        return this;
    }

    @Override
    public ReporteFinca construir() {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalStateException("Un reporte necesita titulo antes de construirse");
        }
        ReporteFinca reporte = new ReporteFinca(titulo, finca, responsable,
                                                lecturas, alertas, inventario, conResumen);
        reiniciar();
        return reporte;
    }

    /** Vuelve al estado inicial para que el siguiente reporte arranque limpio. */
    private void reiniciar() {
        titulo = null; finca = null; responsable = null;
        lecturas   = new ArrayList<>();
        alertas    = new ArrayList<>();
        inventario = new ArrayList<>();
        conResumen = false;
    }
}
