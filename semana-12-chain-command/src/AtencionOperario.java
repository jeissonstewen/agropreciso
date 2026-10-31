/**
 * MANEJADOR CONCRETO: el operario de campo.
 *
 * Atiende lo que se resuelve con una acción inmediata en el terreno. Una
 * humedad por debajo del umbral se corrige regando.
 */
public class AtencionOperario extends ManejadorIncidencia {

    private static final int MINUTOS_DE_RIEGO = 10;

    public AtencionOperario() {
        super("OPERARIO");
    }

    @Override
    protected boolean puedeAtender(Incidencia incidencia) {
        return incidencia.getSeveridad() == Severidad.LEVE;
    }

    @Override
    protected Orden atender(Incidencia incidencia) {
        return new OrdenRegar(incidencia.getLote(), MINUTOS_DE_RIEGO);
    }
}
