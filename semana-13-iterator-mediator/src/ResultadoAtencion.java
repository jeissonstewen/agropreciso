/* ---------------------------------------------------------------
 * COPIA de la Semana 12 (patrones Chain of Responsibility y Command).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-12-chain-command/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * Lo que devuelve la cadena: quién atendió la incidencia y qué orden
 * generó, o la constancia de que nadie pudo atenderla.
 *
 * Existe para que el emisor sepa el DESENLACE sin tener que conocer a los
 * manejadores. También es lo que hace verificables las pruebas: permiten
 * comprobar qué eslabón respondió sin inspeccionar la cadena por dentro.
 */
public class ResultadoAtencion {

    private final String atendidaPor;   // null si nadie la atendió
    private final Orden  orden;         // null si nadie la atendió

    private ResultadoAtencion(String atendidaPor, Orden orden) {
        this.atendidaPor = atendidaPor;
        this.orden       = orden;
    }

    public static ResultadoAtencion atendida(String rol, Orden orden) {
        return new ResultadoAtencion(rol, orden);
    }

    public static ResultadoAtencion sinAtender() {
        return new ResultadoAtencion(null, null);
    }

    public boolean fueAtendida()   { return atendidaPor != null; }
    public String  getAtendidaPor(){ return atendidaPor; }
    public Orden   getOrden()      { return orden; }

    @Override
    public String toString() {
        return fueAtendida()
                ? "atendida por " + atendidaPor + " -> " + orden.getDescripcion()
                : "sin atender: ningun responsable de la cadena pudo hacerse cargo";
    }
}
