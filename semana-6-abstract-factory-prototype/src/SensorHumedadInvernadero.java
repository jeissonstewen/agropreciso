import java.util.Random;

/**
 * PRODUCTO CONCRETO, familia INVERNADERO.
 * Ambiente controlado: la humedad se mueve en un rango estrecho (60 a 85 %).
 */
public class SensorHumedadInvernadero implements Sensor {

    private static final double MIN = 60.0;
    private static final double MAX = 85.0;

    private final String id;
    private final Random aleatorio = new Random();

    public SensorHumedadInvernadero(String id) {
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
