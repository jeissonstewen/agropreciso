/* ---------------------------------------------------------------
 * COPIA de la Semana 14 (patrones Observer, State y Visitor).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-14-observer-state-visitor/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * ESTADO CONCRETO: el lote descansa tras la cosecha.
 * No se riega y no se siembra: primero hay que preparar el suelo, lo que
 * cierra el ciclo devolviéndolo a EnPreparacion.
 */
public class EnBarbecho extends EstadoBase {

    @Override public String getNombre()    { return "en barbecho"; }
    @Override public boolean admiteRiego() { return false; }

    @Override
    public void prepararSuelo(Lote lote) {
        lote.cambiarEstado(new EnPreparacion());
    }
}
