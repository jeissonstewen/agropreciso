/* ---------------------------------------------------------------
 * COPIA de la Semana 14 (patrones Observer, State y Visitor).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-14-observer-state-visitor/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * ESTADO CONCRETO: el suelo está listo pero no hay cultivo.
 * No se riega —no hay nada que regar— y lo único posible es sembrar.
 */
public class EnPreparacion extends EstadoBase {

    @Override public String getNombre()    { return "en preparacion"; }
    @Override public boolean admiteRiego() { return false; }

    @Override
    public void sembrar(Lote lote) {
        lote.cambiarEstado(new Sembrado());
    }
}
