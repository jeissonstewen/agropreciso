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
