import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón ADAPTER.
 *
 * Verifican las tres adaptaciones que hace AdaptadorClimaExterno —formato,
 * unidades y tipo— y que el cliente funcione igual con el adaptador que con
 * una implementación propia.
 */
@DisplayName("Adapter: adaptador del servicio de clima externo")
class AdaptadorClimaExternoTest {

    /** Tolerancia para comparar dobles: las conversiones no son exactas. */
    private static final double DELTA = 0.01;

    private ServicioClimaExterno servicio;

    @BeforeEach
    void prepararServicio() {
        servicio = new ServicioClimaExterno();
    }

    @Test
    @DisplayName("convierte los grados Fahrenheit del servicio a Celsius")
    void convierteFahrenheitACelsius() {
        ProveedorClima adaptador = new AdaptadorClimaExterno(servicio, "EST-NORTE");

        Lectura temperatura = adaptador.obtenerTemperatura();

        // 86.5 F = (86.5 - 32) * 5/9 = 30.28 C
        assertEquals(30.28, temperatura.getValor(), DELTA);
        assertEquals("C", temperatura.getUnidad());
        assertEquals("TEMPERATURA", temperatura.getTipo());
    }

    @Test
    @DisplayName("convierte la fracción de humedad 0-1 a porcentaje 0-100")
    void convierteFraccionAPorcentaje() {
        ProveedorClima adaptador = new AdaptadorClimaExterno(servicio, "EST-NORTE");

        Lectura humedad = adaptador.obtenerHumedad();

        assertEquals(42.0, humedad.getValor(), DELTA);
        assertEquals("%", humedad.getUnidad());
        assertEquals("HUMEDAD", humedad.getTipo());
    }

    @Test
    @DisplayName("adapta correctamente una segunda estación")
    void adaptaOtraEstacion() {
        ProveedorClima adaptador = new AdaptadorClimaExterno(servicio, "EST-SUR");

        // 71.6 F = 22.0 C ; 0.305 -> 30.5 %
        assertEquals(22.0, adaptador.obtenerTemperatura().getValor(), DELTA);
        assertEquals(30.5, adaptador.obtenerHumedad().getValor(), DELTA);
    }

    @Test
    @DisplayName("lanza IllegalStateException si la respuesta no trae el campo")
    void fallaConRespuestaIncompleta() {
        ProveedorClima adaptador = new AdaptadorClimaExterno(servicio, "EST-FANTASMA");

        IllegalStateException error =
                assertThrows(IllegalStateException.class, adaptador::obtenerTemperatura);

        assertTrue(error.getMessage().contains("temp_f"),
                "el mensaje debe decir qué campo faltó, pero fue: " + error.getMessage());
    }

    @Test
    @DisplayName("el cliente decide el riego igual con el adaptador que con sensores propios")
    void elClienteNoDistingueElOrigen() {
        // Sensores propios con 31 % de humedad: por debajo del umbral de 35 %.
        ProveedorClima propio = new ProveedorSensoresPropios(
                new SensorFijo("S-01", "TEMPERATURA", 24.0, "C"),
                new SensorFijo("S-02", "HUMEDAD", 31.0, "%"));
        // Servicio externo con 42 %: por encima del umbral.
        ProveedorClima externo = new AdaptadorClimaExterno(servicio, "EST-NORTE");

        assertTrue(new MonitorClima(propio, 35.0).requiereRiego());
        assertFalse(new MonitorClima(externo, 35.0).requiereRiego());
    }

    @Test
    @DisplayName("el adaptador identifica su origen para los reportes")
    void informaSuOrigen() {
        ProveedorClima adaptador = new AdaptadorClimaExterno(servicio, "EST-NORTE");

        assertEquals("Servicio externo (EST-NORTE)", adaptador.getOrigen());
    }
}
