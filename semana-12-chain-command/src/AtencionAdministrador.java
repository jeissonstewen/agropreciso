/**
 * MANEJADOR CONCRETO: el administrador de la finca.
 *
 * Último eslabón de la cadena. Atiende lo que implica gasto o parada de
 * equipo: un sensor averiado se reemplaza instalando uno de repuesto.
 */
public class AtencionAdministrador extends ManejadorIncidencia {

    public AtencionAdministrador() {
        super("ADMINISTRADOR");
    }

    @Override
    protected boolean puedeAtender(Incidencia incidencia) {
        return incidencia.getSeveridad() == Severidad.CRITICA;
    }

    @Override
    protected Orden atender(Incidencia incidencia) {
        Sensor repuesto = new SensorFijo(
                incidencia.getLote().getNombre() + "-REP", "HUMEDAD", 68.0, "%");
        return new OrdenInstalarSensor(incidencia.getLote(), repuesto);
    }
}
