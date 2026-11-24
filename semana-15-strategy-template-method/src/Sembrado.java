/* ---------------------------------------------------------------
 * COPIA de la Semana 14 (patrones Observer, State y Visitor).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-14-observer-state-visitor/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * ESTADO CONCRETO: cultivo joven recién plantado.
 * Es el estado que más riego necesita. Todavía no se puede cosechar.
 */
public class Sembrado extends EstadoBase {

    @Override public String getNombre()    { return "sembrado"; }
    @Override public boolean admiteRiego() { return true; }

    @Override
    public void madurar(Lote lote) {
        lote.cambiarEstado(new Maduro());
    }
}
