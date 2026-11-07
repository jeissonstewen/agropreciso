/* ---------------------------------------------------------------
 * COPIA de la Semana 12 (patrones Chain of Responsibility y Command).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-12-chain-command/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * MANEJADOR CONCRETO: el agrónomo.
 *
 * Atiende lo que exige un criterio técnico sobre el cultivo. Un umbral mal
 * configurado se corrige ajustándolo al valor que el cultivo necesita, no
 * regando más.
 */
public class AtencionAgronomo extends ManejadorIncidencia {

    /** Umbral de humedad recomendado para el café. */
    private static final double UMBRAL_RECOMENDADO = 70.0;

    public AtencionAgronomo() {
        super("AGRONOMO");
    }

    @Override
    protected boolean puedeAtender(Incidencia incidencia) {
        return incidencia.getSeveridad() == Severidad.MEDIA;
    }

    @Override
    protected Orden atender(Incidencia incidencia) {
        return new OrdenCambiarUmbralHumedad(incidencia.getLote(), UMBRAL_RECOMENDADO);
    }
}
