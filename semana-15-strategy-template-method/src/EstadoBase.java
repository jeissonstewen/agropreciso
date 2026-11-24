/* ---------------------------------------------------------------
 * COPIA de la Semana 14 (patrones Observer, State y Visitor).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-14-observer-state-visitor/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * Base común de los estados: rechaza TODAS las transiciones.
 *
 * Cada estado concreto sobreescribe únicamente la que sí permite. Así una
 * clase de estado ocupa cinco líneas y es imposible olvidarse de prohibir
 * una transición: lo prohibido es el valor por defecto.
 */
public abstract class EstadoBase implements EstadoLote {

    @Override public void sembrar(Lote lote)       { rechazar("sembrar", lote); }
    @Override public void madurar(Lote lote)       { rechazar("madurar", lote); }
    @Override public void cosechar(Lote lote)      { rechazar("cosechar", lote); }
    @Override public void prepararSuelo(Lote lote) { rechazar("preparar el suelo de", lote); }

    /** Mensaje único para toda transición inválida. */
    private void rechazar(String operacion, Lote lote) {
        throw new IllegalStateException(String.format(
                "No se puede %s %s: esta %s", operacion, lote.getNombre(), getNombre()));
    }

    @Override
    public String toString() { return getNombre(); }
}
