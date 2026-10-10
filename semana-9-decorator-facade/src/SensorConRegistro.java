/* ---------------------------------------------------------------
 * COPIA de la Semana 8 (patrones Composite y Decorator).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-8-composite-decorator/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DECORADOR CONCRETO: deja constancia de cada lectura.
 *
 * Imprime la lectura por consola y la guarda en un historial, para auditar
 * después qué midió cada sensor.
 *
 * Este decorador NO altera el valor: agrega un efecto alrededor de la
 * lectura. Sirve para mostrar que el patrón no sirve solo para transformar
 * datos, sino también para observar.
 */
public class SensorConRegistro extends SensorDecorador {

    private final List<Lectura> historial = new ArrayList<>();

    public SensorConRegistro(Sensor sensorEnvuelto) {
        super(sensorEnvuelto);
    }

    @Override
    public Lectura leer() {
        Lectura lectura = sensorEnvuelto.leer();   // el valor se devuelve intacto
        historial.add(lectura);
        System.out.println("    [LOG] lectura #" + historial.size() + ": " + lectura);
        return lectura;
    }

    public List<Lectura> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    @Override
    public String getDescripcion() {
        return SensorConCalibracion.descripcionDe(sensorEnvuelto) + " + registro";
    }
}
