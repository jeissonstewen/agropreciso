/**
 * Producto de una medición: el dato que entrega cualquier sensor.
 * Es común a los tres tipos, por eso no cambia con el patrón.
 */
public class Lectura {

    private final String idSensor;
    private final String tipo;
    private final double valor;
    private final String unidad;

    public Lectura(String idSensor, String tipo, double valor, String unidad) {
        this.idSensor = idSensor;
        this.tipo     = tipo;
        this.valor    = valor;
        this.unidad   = unidad;
    }

    public String getIdSensor() { return idSensor; }
    public String getTipo()     { return tipo; }
    public double getValor()    { return valor; }
    public String getUnidad()   { return unidad; }

    @Override
    public String toString() {
        return String.format("%-12s [%s] %.1f %s", tipo, idSensor, valor, unidad);
    }
}
