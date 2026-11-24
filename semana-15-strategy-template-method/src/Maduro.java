/* ---------------------------------------------------------------
 * COPIA de la Semana 14 (patrones Observer, State y Visitor).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-14-observer-state-visitor/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * ESTADO CONCRETO: el cultivo llegó a punto de cosecha.
 * Sigue admitiendo riego hasta que se recoja.
 */
public class Maduro extends EstadoBase {

    @Override public String getNombre()    { return "maduro"; }
    @Override public boolean admiteRiego() { return true; }

    @Override
    public void cosechar(Lote lote) {
        lote.cambiarEstado(new EnBarbecho());
    }
}
