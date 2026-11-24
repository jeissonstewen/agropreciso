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
