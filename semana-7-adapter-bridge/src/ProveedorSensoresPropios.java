/**
 * Implementación NATIVA de ProveedorClima: usa los sensores de la finca.
 *
 * Existe para demostrar que el cliente trata igual al adaptador y a una
 * implementación propia: los dos son ProveedorClima.
 */
public class ProveedorSensoresPropios implements ProveedorClima {

    private final Sensor sensorTemperatura;
    private final Sensor sensorHumedad;

    public ProveedorSensoresPropios(Sensor sensorTemperatura, Sensor sensorHumedad) {
        this.sensorTemperatura = sensorTemperatura;
        this.sensorHumedad     = sensorHumedad;
    }

    @Override
    public Lectura obtenerTemperatura() { return sensorTemperatura.leer(); }

    @Override
    public Lectura obtenerHumedad()     { return sensorHumedad.leer(); }

    @Override
    public String getOrigen()           { return "Sensores propios"; }
}
