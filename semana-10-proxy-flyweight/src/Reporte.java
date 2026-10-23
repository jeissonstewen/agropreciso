/* ---------------------------------------------------------------
 * COPIA de la Semana 7 (patrones Adapter y Bridge).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-7-adapter-bridge/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PATRÓN BRIDGE — la ABSTRACCIÓN.
 *
 * Representa un reporte de la finca. Mantiene una referencia a un
 * SalidaReporte (el implementador) y le delega toda la escritura: por eso
 * esta jerarquía puede crecer —nuevos tipos de reporte— sin tocar la de
 * formatos, y al revés.
 *
 * Sin el puente habría que escribir una clase por cada combinación:
 * ReporteEstadoConsola, ReporteEstadoCsv, ReporteAlertasConsola... El
 * puente convierte ese producto (2 x 3 = 6 clases) en una suma (2 + 3).
 */
public abstract class Reporte {

    /** El implementador. Protegido para que las refinaciones lo usen. */
    protected final SalidaReporte salida;

    protected Reporte(SalidaReporte salida) {
        this.salida = salida;
    }

    /** Cada tipo de reporte decide qué escribe; nunca cómo se escribe. */
    public abstract void generar();

    /** Nombre del tipo de reporte, para las pruebas y la demo. */
    public abstract String getNombre();

    /** Combinación efectiva: qué reporte sobre qué formato. */
    public String describir() {
        return getNombre() + " en formato " + salida.getFormato();
    }
}
