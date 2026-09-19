import java.util.Random;

/**
 * PRODUCTO CONCRETO, familia INVERNADERO.
 * Temperatura regulada: entre 18 y 28 °C.
 */
public class SensorTemperaturaInvernadero implements Sensor {

    private static final double MIN = 18.0;
    private static final double MAX = 28.0;

    private final String id;
    private final Random aleatorio = new Random();

    public SensorTemperaturaInvernadero(String id) {
        this.id = id;
    }

    @Override
    public Lectura leer() {
        double valor = MIN + aleatorio.nextDouble() * (MAX - MIN);
        return new Lectura(id, getTipo(), valor, "C");
    }

    @Override
    public String getTipo() { return "TEMPERATURA"; }
}
