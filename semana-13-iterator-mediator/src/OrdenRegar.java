/* ---------------------------------------------------------------
 * COPIA de la Semana 12 (patrones Chain of Responsibility y Command).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-12-chain-command/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.Objects;

/**
 * ORDEN CONCRETA: aplica riego a un lote.
 *
 * Es el caso de orden NO REVERSIBLE del proyecto. Una vez que el agua se
 * aplicó no hay forma de retirarla, así que la orden lo declara y deshacer()
 * falla en voz alta en lugar de simular que hizo algo.
 *
 * Nótese que esta orden no decide CUÁNTO regar: recibe los minutos ya
 * calculados. La regla que elige ese número corresponde al patrón Strategy,
 * previsto para el módulo de riego.
 */
public class OrdenRegar implements Orden {

    private final Lote lote;          // el receptor
    private final int  minutos;

    public OrdenRegar(Lote lote, int minutos) {
        this.lote = Objects.requireNonNull(lote, "La orden necesita un lote");
        if (minutos <= 0) {
            throw new IllegalArgumentException("Los minutos de riego deben ser positivos");
        }
        this.minutos = minutos;
    }

    @Override
    public void ejecutar() {
        if (lote.getActuador() == null) {
            throw new IllegalStateException("El lote " + lote.getNombre() + " no tiene actuador");
        }
        System.out.print("    ");
        lote.getActuador().regar(minutos);
    }

    @Override
    public void deshacer() {
        throw new UnsupportedOperationException(
                "El riego no se puede deshacer: el agua ya se aplico");
    }

    @Override
    public boolean esReversible() { return false; }

    @Override
    public String getDescripcion() {
        return String.format("Regar %s durante %d min", lote.getNombre(), minutos);
    }
}
