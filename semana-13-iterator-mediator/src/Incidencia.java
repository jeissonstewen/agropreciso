/* ---------------------------------------------------------------
 * COPIA de la Semana 12 (patrones Chain of Responsibility y Command).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-12-chain-command/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.Objects;

/**
 * La PETICIÓN que viaja por la cadena.
 *
 * Es inmutable: ningún eslabón puede modificarla al pasarla al siguiente.
 * Lleva el lote afectado, el tipo —que determina la severidad— y, cuando
 * la originó una medición, la lectura concreta.
 */
public class Incidencia {

    private final Lote           lote;
    private final TipoIncidencia tipo;
    private final Lectura        lectura;   // puede ser null: no toda incidencia viene de un sensor

    public Incidencia(Lote lote, TipoIncidencia tipo, Lectura lectura) {
        this.lote    = Objects.requireNonNull(lote, "Una incidencia pertenece a un lote");
        this.tipo    = Objects.requireNonNull(tipo, "Una incidencia necesita un tipo");
        this.lectura = lectura;
    }

    public Incidencia(Lote lote, TipoIncidencia tipo) {
        this(lote, tipo, null);
    }

    public Lote           getLote()      { return lote; }
    public TipoIncidencia getTipo()      { return tipo; }
    public Severidad      getSeveridad() { return tipo.getSeveridad(); }
    public Lectura        getLectura()   { return lectura; }

    @Override
    public String toString() {
        return String.format("[%s] %s en %s%s",
                getSeveridad(), tipo.getDescripcion(), lote.getNombre(),
                lectura == null ? "" : " (" + lectura + ")");
    }
}
