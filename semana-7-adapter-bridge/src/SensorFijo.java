/**
 * Sensor de valor FIJO, no aleatorio.
 *
 * Los sensores de las semanas anteriores generan valores al azar, lo que
 * impide escribir una prueba unitaria con un resultado esperado. Este
 * doble de prueba (un *stub*) devuelve siempre el mismo valor, y existe
 * gracias a que el sistema programa contra la interfaz Sensor.
 */
public class SensorFijo implements Sensor {

    private final String id;
    private final String tipo;
    private final double valor;
    private final String unidad;

    public SensorFijo(String id, String tipo, double valor, String unidad) {
        this.id     = id;
        this.tipo   = tipo;
        this.valor  = valor;
        this.unidad = unidad;
    }

    @Override
    public Lectura leer() { return new Lectura(id, tipo, valor, unidad); }

    @Override
    public String getTipo() { return tipo; }
}
