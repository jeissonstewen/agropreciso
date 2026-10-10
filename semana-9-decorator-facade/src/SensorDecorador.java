/* ---------------------------------------------------------------
 * COPIA de la Semana 8 (patrones Composite y Decorator).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-8-composite-decorator/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PATRÓN DECORATOR — el DECORADOR ABSTRACTO.
 *
 * Implementa la misma interfaz que decora (Sensor) y además ENVUELVE a un
 * Sensor. Esa doble condición es la clave del patrón: como por fuera sigue
 * siendo un Sensor, un decorador puede usarse en cualquier sitio donde se
 * espere uno —dentro de un Lote, de una estación o de un reporte— y como
 * por dentro guarda otro Sensor, puede añadirle comportamiento antes o
 * después de delegarle la lectura.
 *
 * Por defecto delega sin hacer nada; cada decorador concreto sobreescribe
 * solo lo que necesita cambiar.
 */
public abstract class SensorDecorador implements Sensor {

    /** El objeto envuelto. Puede ser un sensor real u otro decorador. */
    protected final Sensor sensorEnvuelto;

    protected SensorDecorador(Sensor sensorEnvuelto) {
        if (sensorEnvuelto == null) {
            throw new IllegalArgumentException("Un decorador necesita un sensor que envolver");
        }
        this.sensorEnvuelto = sensorEnvuelto;
    }

    @Override
    public Lectura leer() {
        return sensorEnvuelto.leer();
    }

    @Override
    public String getTipo() {
        return sensorEnvuelto.getTipo();
    }

    /** Cadena de decoradores aplicados, para la demo y las pruebas. */
    public abstract String getDescripcion();
}
