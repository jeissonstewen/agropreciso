/**
 * PATRÓN ADAPTER — el ADAPTADOR (variante de objeto).
 *
 * Traduce la interfaz de ServicioClimaExterno a la interfaz ProveedorClima
 * que espera nuestro sistema. Hace tres adaptaciones a la vez:
 *
 *   1. DE FORMATO : parsea el String "clave=valor;clave=valor" del servicio.
 *   2. DE UNIDADES: convierte Fahrenheit a Celsius y la fracción 0-1 a %.
 *   3. DE TIPO    : devuelve Lectura, el objeto que ya usa todo el sistema.
 *
 * Usamos la variante de OBJETO (el adaptador COMPONE al adaptado) y no la
 * de clase (heredar de ServicioClimaExterno): Java no tiene herencia
 * múltiple, y además no queremos atarnos por herencia a una clase de un
 * tercero que puede cambiar.
 */
public class AdaptadorClimaExterno implements ProveedorClima {

    private final ServicioClimaExterno servicio;   // el adaptado, por composición
    private final String codigoEstacion;

    public AdaptadorClimaExterno(ServicioClimaExterno servicio, String codigoEstacion) {
        this.servicio       = servicio;
        this.codigoEstacion = codigoEstacion;
    }

    @Override
    public Lectura obtenerTemperatura() {
        double fahrenheit = leerCampo("temp_f");
        double celsius    = (fahrenheit - 32) * 5 / 9;      // adaptación de unidades
        return new Lectura(codigoEstacion, "TEMPERATURA", celsius, "C");
    }

    @Override
    public Lectura obtenerHumedad() {
        double fraccion = leerCampo("hum_ratio");
        return new Lectura(codigoEstacion, "HUMEDAD", fraccion * 100, "%");
    }

    @Override
    public String getOrigen() {
        return "Servicio externo (" + codigoEstacion + ")";
    }

    /**
     * Adaptación de formato: busca una clave en la respuesta cruda del
     * servicio y devuelve su valor numérico.
     */
    private double leerCampo(String clave) {
        String respuesta = servicio.fetchWeather(codigoEstacion);
        for (String par : respuesta.split(";")) {
            String[] partes = par.split("=", 2);
            if (partes.length == 2 && partes[0].equals(clave)) {
                try {
                    return Double.parseDouble(partes[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(
                            "El servicio externo devolvio un valor no numerico para '" + clave + "'", e);
                }
            }
        }
        throw new IllegalStateException(
                "El servicio externo no devolvio el campo '" + clave + "': " + respuesta);
    }
}
