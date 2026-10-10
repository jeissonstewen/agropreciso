import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón DECORATOR aplicado al servicio de clima.
 *
 * Comprueban que el caché reduzca las llamadas reales, que el reintento
 * absorba fallos pasajeros y propague los definitivos, y que ambos se
 * apilen conservando la interfaz ProveedorClima.
 */
@DisplayName("Decorator: proveedor de clima decorado")
class ClimaDecoradorTest {

    private static final double DELTA = 0.01;

    private final PrintStream salidaOriginal = System.out;

    /** ClimaConReintento informa cada fallo; se silencia en las pruebas. */
    @BeforeEach
    void silenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    /** Adaptador sobre un servicio que cuenta llamadas y falla a voluntad. */
    private ProveedorClima adaptadorSobre(ServicioClimaInestable servicio) {
        return new AdaptadorClimaExterno(servicio, "EST-NORTE");
    }

    @Test
    @DisplayName("sin caché cada consulta llega al servicio externo")
    void sinCacheSeLlamaSiempre() {
        ServicioClimaInestable servicio = new ServicioClimaInestable(0);
        ProveedorClima directo = adaptadorSobre(servicio);

        directo.obtenerTemperatura();
        directo.obtenerTemperatura();

        assertEquals(2, servicio.getLlamadas());
    }

    @Test
    @DisplayName("el caché llama una sola vez por magnitud")
    void elCacheEvitaLlamadasRepetidas() {
        ServicioClimaInestable servicio = new ServicioClimaInestable(0);
        ClimaConCache cache = new ClimaConCache(adaptadorSobre(servicio));

        cache.obtenerTemperatura();
        cache.obtenerTemperatura();
        cache.obtenerHumedad();
        cache.obtenerHumedad();

        assertEquals(2, servicio.getLlamadas(), "una llamada por magnitud");
        assertEquals(2, cache.getAciertos(), "las otras dos salen del cache");
    }

    @Test
    @DisplayName("el caché devuelve exactamente la misma lectura guardada")
    void elCacheDevuelveLoMismo() {
        ClimaConCache cache = new ClimaConCache(adaptadorSobre(new ServicioClimaInestable(0)));

        Lectura primera = cache.obtenerTemperatura();
        Lectura segunda = cache.obtenerTemperatura();

        assertSame(primera, segunda);
        assertEquals(30.28, primera.getValor(), DELTA);
    }

    @Test
    @DisplayName("invalidar() obliga a volver a consultar el origen")
    void invalidarVuelveAConsultar() {
        ServicioClimaInestable servicio = new ServicioClimaInestable(0);
        ClimaConCache cache = new ClimaConCache(adaptadorSobre(servicio));

        cache.obtenerTemperatura();
        cache.invalidar();
        cache.obtenerTemperatura();

        assertEquals(2, servicio.getLlamadas());
    }

    @Test
    @DisplayName("el reintento se recupera de fallos pasajeros")
    void elReintentoAbsorbeFallos() {
        // Falla las dos primeras llamadas, responde a la tercera.
        ServicioClimaInestable inestable = new ServicioClimaInestable(2);
        ClimaConReintento robusto = new ClimaConReintento(adaptadorSobre(inestable), 3);

        Lectura lectura = robusto.obtenerTemperatura();

        assertEquals(30.28, lectura.getValor(), DELTA);
        assertEquals(2, robusto.getReintentos());
    }

    @Test
    @DisplayName("si se agotan los intentos el error se propaga")
    void elReintentoSeRinde() {
        ClimaConReintento sinSuerte =
                new ClimaConReintento(adaptadorSobre(new ServicioClimaInestable(99)), 2);

        IllegalStateException error =
                assertThrows(IllegalStateException.class, sinSuerte::obtenerTemperatura);

        assertTrue(error.getMessage().contains("2 intentos"), error.getMessage());
    }

    @Test
    @DisplayName("el reintento exige al menos un intento")
    void reintentoConCeroIntentos() {
        ProveedorClima directo = adaptadorSobre(new ServicioClimaInestable(0));

        assertThrows(IllegalArgumentException.class, () -> new ClimaConReintento(directo, 0));
    }

    @Test
    @DisplayName("caché y reintento se apilan: un fallo absorbido y una sola llamada buena")
    void decoradoresApilados() {
        ServicioClimaInestable servicio = new ServicioClimaInestable(1);
        ClimaConCache apilado = new ClimaConCache(
                new ClimaConReintento(adaptadorSobre(servicio), 3));

        apilado.obtenerTemperatura();
        apilado.obtenerTemperatura();

        assertEquals(2, servicio.getLlamadas(), "1 fallida + 1 buena; la segunda sale del cache");
        assertEquals(1, apilado.getAciertos());
    }

    @Test
    @DisplayName("getDescripcion() encadena los decoradores aplicados")
    void describeLaCadena() {
        ClimaConCache apilado = new ClimaConCache(
                new ClimaConReintento(adaptadorSobre(new ServicioClimaInestable(0)), 3));

        String descripcion = apilado.getDescripcion();

        assertTrue(descripcion.contains("reintento(3)"), descripcion);
        assertTrue(descripcion.endsWith("cache"), descripcion);
    }

    @Test
    @DisplayName("un decorador exige un proveedor que envolver")
    void noSePuedeDecorarNada() {
        assertThrows(IllegalArgumentException.class, () -> new ClimaConCache(null));
    }

    @Test
    @DisplayName("el decorado sigue siendo un ProveedorClima para el resto del sistema")
    void elDecoradoConservaLaInterfaz() {
        ProveedorClima decorado = new ClimaConCache(
                new ClimaConReintento(adaptadorSobre(new ServicioClimaInestable(0)), 3));

        // Se usa por la interfaz, sin saber que lleva decoradores detras.
        assertEquals("HUMEDAD", decorado.obtenerHumedad().getTipo());
        assertTrue(decorado.getOrigen().contains("EST-NORTE"), decorado.getOrigen());
    }
}
