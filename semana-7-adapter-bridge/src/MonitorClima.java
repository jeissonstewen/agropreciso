/**
 * CLIENTE del Adapter.
 *
 * Decide si hay que regar comparando la humedad con un umbral. Solo conoce
 * la interfaz ProveedorClima: le da igual si los datos vienen de un sensor
 * instalado en el lote o del servicio meteorológico de un tercero.
 */
public class MonitorClima {

    private final ProveedorClima proveedor;
    private final double umbralHumedadMin;

    public MonitorClima(ProveedorClima proveedor, double umbralHumedadMin) {
        this.proveedor        = proveedor;
        this.umbralHumedadMin = umbralHumedadMin;
    }

    /** Devuelve true si la humedad está por debajo del umbral. */
    public boolean requiereRiego() {
        return proveedor.obtenerHumedad().getValor() < umbralHumedadMin;
    }

    public void informar() {
        System.out.println("  Origen: " + proveedor.getOrigen());
        System.out.println("  " + proveedor.obtenerTemperatura());
        System.out.println("  " + proveedor.obtenerHumedad());
        System.out.println("  Requiere riego (umbral " + umbralHumedadMin + " %): " + requiereRiego());
    }
}
