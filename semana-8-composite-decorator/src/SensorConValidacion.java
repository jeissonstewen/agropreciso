/**
 * DECORADOR CONCRETO: descarta lecturas imposibles.
 *
 * Un sensor con la conexión suelta puede devolver -40 % de humedad. Este
 * decorador acota el valor al rango físico válido y lleva la cuenta de
 * cuántas veces tuvo que intervenir, dato útil para mantenimiento.
 *
 * Este decorador SUSTITUYE el valor cuando está fuera de rango, y lo deja
 * intacto cuando es correcto.
 */
public class SensorConValidacion extends SensorDecorador {

    private final double minimo;
    private final double maximo;
    private int correcciones = 0;

    public SensorConValidacion(Sensor sensorEnvuelto, double minimo, double maximo) {
        super(sensorEnvuelto);
        if (minimo > maximo) {
            throw new IllegalArgumentException("El minimo no puede ser mayor que el maximo");
        }
        this.minimo = minimo;
        this.maximo = maximo;
    }

    @Override
    public Lectura leer() {
        Lectura original = sensorEnvuelto.leer();
        double valor = original.getValor();
        if (valor < minimo || valor > maximo) {
            correcciones++;
            valor = (valor < minimo) ? minimo : maximo;
            return new Lectura(original.getIdSensor(), original.getTipo(), valor,
                               original.getUnidad());
        }
        return original;                   // en rango: se devuelve tal cual
    }

    /** Cuántas lecturas hubo que corregir. */
    public int getCorrecciones() { return correcciones; }

    @Override
    public String getDescripcion() {
        return SensorConCalibracion.descripcionDe(sensorEnvuelto)
                + " + validacion(" + minimo + ".." + maximo + ")";
    }
}
