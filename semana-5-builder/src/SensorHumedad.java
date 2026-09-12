/* ---------------------------------------------------------------
 * COPIA de la Semana 4 (patrón Factory Method).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-4-factory-method/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.Random;

/** PRODUCTO CONCRETO: mide humedad del suelo entre 0 y 100 %. */
public class SensorHumedad implements Sensor {

    private static final double MIN = 10.0;
    private static final double MAX = 90.0;

    private final String id;
    private final Random aleatorio = new Random();

    public SensorHumedad(String id) {
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
