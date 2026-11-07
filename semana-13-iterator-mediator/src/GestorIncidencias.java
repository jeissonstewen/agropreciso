/**
 * COLEGA: convierte un lote con problemas en una incidencia y la escala
 * por la cadena de responsabilidad de la Semana 12.
 *
 * No conoce ni a la estación que detectó el problema ni al panel que
 * ejecutará la orden: recibe el encargo del mediador y le devuelve el
 * desenlace por la misma vía.
 */
public class GestorIncidencias extends Colega {

    private final ManejadorIncidencia cadena;

    public GestorIncidencias(Mediador mediador, ManejadorIncidencia cadena) {
        super(mediador, "GestorIncidencias");
        this.cadena = cadena;
    }

    /** Lo llama el mediador cuando la estación reporta una anomalía. */
    public void escalar(Lote lote) {
        ResultadoAtencion resultado = cadena.manejar(
                new Incidencia(lote, TipoIncidencia.HUMEDAD_BAJA));

        if (resultado.fueAtendida()) {
            avisar(Evento.ORDEN_GENERADA, resultado.getOrden());
        } else {
            avisar(Evento.INCIDENCIA_SIN_ATENDER, lote);
        }
    }
}
