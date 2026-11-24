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
