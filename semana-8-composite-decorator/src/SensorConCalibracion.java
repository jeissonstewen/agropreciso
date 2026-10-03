/**
 * DECORADOR CONCRETO: corrige la desviación de fábrica del sensor.
 *
 * Un sensor de humedad puede marcar sistemáticamente 1.5 puntos de más. En
 * vez de tocar la clase del sensor —que puede ser de un proveedor y estar
 * bien escrita— se le suma el offset por fuera.
 *
 * Este decorador ALTERA el valor de la lectura.
 */
public class SensorConCalibracion extends SensorDecorador {

    private final double offset;

    public SensorConCalibracion(Sensor sensorEnvuelto, double offset) {
        super(sensorEnvuelto);
        this.offset = offset;
    }

    @Override
    public Lectura leer() {
        Lectura original = sensorEnvuelto.leer();     // 1. se delega
        return new Lectura(original.getIdSensor(),    // 2. se añade el comportamiento
                           original.getTipo(),
                           original.getValor() + offset,
                           original.getUnidad());
    }

    @Override
    public String getDescripcion() {
        return descripcionDe(sensorEnvuelto) + " + calibracion(" + offset + ")";
    }

    /** Si lo envuelto es otro decorador, encadena su descripción. */
    static String descripcionDe(Sensor sensor) {
        return (sensor instanceof SensorDecorador)
                ? ((SensorDecorador) sensor).getDescripcion()
                : sensor.getTipo();
    }
}
