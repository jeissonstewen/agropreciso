/**
 * Proveedor de clima de valores FIJOS, para la demo y las pruebas.
 *
 * Es un *stub*, el equivalente de `SensorFijo` (Semana 7) pero del lado
 * del clima: permite escribir una prueba con un resultado esperado y
 * mostrar en la demo un día lluvioso sin depender de qué devuelve el
 * servicio externo simulado.
 *
 * Existe gracias al patrón ADAPTER de la Semana 7: el sistema programa
 * contra `ProveedorClima`, así que cualquier implementación encaja —la que
 * traduce un servicio en Fahrenheit o esta, que no traduce nada.
 */
public class ClimaFijo implements ProveedorClima {

    private final String origen;
    private final double temperaturaC;
    private final double humedadPorcentaje;

    public ClimaFijo(String origen, double temperaturaC, double humedadPorcentaje) {
        this.origen            = origen;
        this.temperaturaC      = temperaturaC;
        this.humedadPorcentaje = humedadPorcentaje;
    }

    @Override
    public Lectura obtenerTemperatura() {
        return new Lectura(origen, "TEMPERATURA", temperaturaC, "C");
    }

    @Override
    public Lectura obtenerHumedad() {
        return new Lectura(origen, "HUMEDAD", humedadPorcentaje, "%");
    }

    @Override
    public String getOrigen() { return origen; }
}
