/**
 * ADAPTEE (el "adaptado") — servicio de clima de un tercero.
 *
 * Simula la API meteorológica que la finca contrata. NO la controlamos: su
 * firma, su formato y sus unidades vienen dados, y no podemos cambiarlos.
 * Es justamente eso lo que hace necesario un Adapter.
 *
 * Incompatibilidades con nuestro sistema:
 *   1. Devuelve un String con pares clave=valor, no un objeto.
 *   2. La temperatura viene en grados FAHRENHEIT.
 *   3. La humedad viene como FRACCIÓN de 0 a 1, no en porcentaje.
 *   4. El método se llama fetchWeather() y recibe un código de estación.
 */
public class ServicioClimaExterno {

    /** Respuesta cruda del servicio, con el formato que él decide. */
    public String fetchWeather(String stationCode) {
        // Valores fijos: al ser un servicio externo simulado, no aleatoriza,
        // así la demo y las pruebas son reproducibles.
        if ("EST-NORTE".equals(stationCode)) {
            return "station=EST-NORTE;temp_f=86.5;hum_ratio=0.42;ts=1727360000";
        }
        if ("EST-SUR".equals(stationCode)) {
            return "station=EST-SUR;temp_f=71.6;hum_ratio=0.305;ts=1727360000";
        }
        return "error=unknown_station";
    }
}
