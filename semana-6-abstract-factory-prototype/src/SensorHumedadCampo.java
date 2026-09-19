import java.util.Random;

/**
 * PRODUCTO CONCRETO, familia CAMPO ABIERTO.
 * Expuesto al clima: la humedad varía en todo el rango (10 a 90 %).
 */
public class SensorHumedadCampo implements Sensor {

    private static final double MIN = 10.0;
    private static final double MAX = 90.0;

    private final String id;
    private final Random aleatorio = new Random();

    public SensorHumedadCampo(String id) {
        this.id = id;
    }

    @Override
    public Lectura leer() {
        double valor = MIN + aleatorio.nextDouble() * (MAX - MIN);
        return new Lectura(id, getTipo(), valor, "%");
    }

    @Override
    public String getTipo() { return "HUMEDAD"; }
}
