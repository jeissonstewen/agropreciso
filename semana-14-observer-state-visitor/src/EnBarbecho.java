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
