/**
 * Variante del servicio externo que SIMULA las dos molestias reales de una
 * API de terceros: cobra por llamada y a veces se cae.
 *
 * Cuenta cuántas veces la han llamado —así se puede comprobar el efecto
 * del caché— y falla las primeras llamadas —así se puede comprobar el
 * efecto del reintento—. No forma parte de ningún patrón: es el banco de
 * pruebas de los decoradores.
 */
public class ServicioClimaInestable extends ServicioClimaExterno {

    private final int fallosIniciales;
    private int llamadas = 0;

    /** @param fallosIniciales cuántas de las primeras llamadas deben fallar */
    public ServicioClimaInestable(int fallosIniciales) {
        this.fallosIniciales = fallosIniciales;
    }

    @Override
    public String fetchWeather(String stationCode) {
        llamadas++;
        if (llamadas <= fallosIniciales) {
            throw new IllegalStateException("timeout de la API meteorologica");
        }
        return super.fetchWeather(stationCode);
    }

    /** Cuántas veces se llamó de verdad al servicio. */
    public int getLlamadas() { return llamadas; }
}
