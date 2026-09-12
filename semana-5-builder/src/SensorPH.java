/* ---------------------------------------------------------------
 * COPIA de la Semana 4 (patrón Factory Method).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-4-factory-method/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.Random;

/** PRODUCTO CONCRETO: mide la acidez del suelo en la escala de pH (0 a 14). */
public class SensorPH implements Sensor {

    private static final double MIN = 4.5;
    private static final double MAX = 8.5;

    private final String id;
    private final Random aleatorio = new Random();

    public SensorPH(String id) {
        this.id = id;
    }

    @Override
    public Lectura leer() {
        double valor = MIN + aleatorio.nextDouble() * (MAX - MIN);
        return new Lectura(id, getTipo(), valor, "pH");
    }

    @Override
    public String getTipo() { return "PH"; }
}
