import java.util.Random;

/**
 * PRODUCTO CONCRETO, familia CAMPO ABIERTO.
 * Sin control ambiental: entre 5 y 38 °C.
 */
public class SensorTemperaturaCampo implements Sensor {

    private static final double MIN = 5.0;
    private static final double MAX = 38.0;

    private final String id;
    private final Random aleatorio = new Random();

    public SensorTemperaturaCampo(String id) {
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
