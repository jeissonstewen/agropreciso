import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
 * Pruebas unitarias del patrón STRATEGY en el módulo de riego.
 *
 * Lo que hay que demostrar no es que las fórmulas estén bien —son datos
 * *dummy*— sino que la política sea INTERCAMBIABLE: que el mismo lote,
 * con la misma lectura, aplique cantidades distintas según la estrategia
 * que tenga puesta, y que agregar una estrategia nueva no obligue a tocar
 * el lote.
 */
@DisplayName("Strategy: políticas de riego")
class EstrategiasRiegoTest {

    private static final UmbralesLote UMBRALES = new UmbralesLote(45.0, 30.0);
    private static final Lectura SECO  = new Lectura("S-H1", "HUMEDAD", 30.0, "%");  // déficit 15
    private static final Lectura HUMEDO = new Lectura("S-H1", "HUMEDAD", 60.0, "%"); // sin déficit

    private final PrintStream salidaOriginal = System.out;
    private ByteArrayOutputStream capturada;
    private Lote lote;

    @BeforeEach
    void prepararLoteYCapturarSalida() {
        capturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturada));
        lote = new Lote("Cafe-Norte", "Cafe", 2.5, UMBRALES)
                .agregarSensor(new SensorFijo("S-H1", "HUMEDAD", 30.0, "%"))
                .setActuador(new RiegoGoteo());
        lote.sembrar();                       // el State exige cultivo para regar
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    // --- cada estrategia por separado ---

    @Test
    @DisplayName("ahorro de agua: lo mínimo para cruzar el umbral")
    void ahorroDeAgua() {
        // Déficit de 15 puntos, 3 puntos por minuto -> 5 minutos justos.
        assertEquals(5, new EstrategiaAhorroAgua().calcularMinutos(SECO, UMBRALES));
    }

    @Test
    @DisplayName("intensiva: riega más que la de ahorro con la misma lectura")
    void intensivaRiegaMas() {
        int ahorro    = new EstrategiaAhorroAgua().calcularMinutos(SECO, UMBRALES);
        int intensiva = new EstrategiaIntensiva().calcularMinutos(SECO, UMBRALES);
        assertEquals(10, intensiva);
        assertTrue(intensiva > ahorro, "la intensiva debe superar a la de ahorro");
    }

    @Test
    @DisplayName("ninguna estrategia riega si la humedad ya supera el umbral")
    void sinDeficitNadieRiega() {
        assertEquals(0, new EstrategiaAhorroAgua().calcularMinutos(HUMEDO, UMBRALES));
        assertEquals(0, new EstrategiaIntensiva().calcularMinutos(HUMEDO, UMBRALES));
        assertEquals(0, new EstrategiaSegunClima(new ClimaFijo("X", 25.0, 40.0))
                .calcularMinutos(HUMEDO, UMBRALES));
    }

    @Test
    @DisplayName("según clima: con calor riega un 50 % más")
    void climaCalurosoRiegaMas() {
        EstrategiaRiego fresco  = new EstrategiaSegunClima(new ClimaFijo("EST", 24.0, 40.0));
        EstrategiaRiego caluroso = new EstrategiaSegunClima(new ClimaFijo("EST", 31.0, 40.0));
        assertEquals(5, fresco.calcularMinutos(SECO, UMBRALES));
        assertEquals(8, caluroso.calcularMinutos(SECO, UMBRALES));
    }

    @Test
    @DisplayName("según clima: con ambiente saturado decide NO regar aunque el suelo esté seco")
    void climaHumedoNoRiega() {
        EstrategiaRiego lluvia = new EstrategiaSegunClima(new ClimaFijo("EST-LLUVIA", 22.0, 78.0));
        assertEquals(0, lluvia.calcularMinutos(SECO, UMBRALES));
    }

    @Test
    @DisplayName("el tope de seguridad limita el riego por mucho que falte")
    void topeDeSeguridad() {
        Lectura desierto = new Lectura("S-H1", "HUMEDAD", 0.0, "%");
        UmbralesLote exigente = new UmbralesLote(100.0, 30.0);   // déficit de 100 puntos
        assertEquals(15, new EstrategiaAhorroAgua().calcularMinutos(desierto, exigente));
        assertEquals(30, new EstrategiaIntensiva().calcularMinutos(desierto, exigente));
    }

    // --- lo que el patrón promete: intercambiabilidad ---

    @Test
    @DisplayName("LA PRUEBA DEL PATRÓN: la misma lectura da litros distintos al cambiar la política")
    void mismaLecturaDistintaPolitica() {
        lote.setEstrategiaRiego(new EstrategiaAhorroAgua());
        String conAhorro = monitorear();

        lote.setEstrategiaRiego(new EstrategiaIntensiva());
        String conIntensiva = monitorear();

        assertTrue(conAhorro.contains("10.0 L aplicados"),    () -> conAhorro);
        assertTrue(conIntensiva.contains("20.0 L aplicados"), () -> conIntensiva);
        assertNotEquals(conAhorro, conIntensiva);
    }

    @Test
    @DisplayName("la estrategia se puede cambiar en ejecución, entre dos monitoreos")
    void cambioEnEjecucion() {
        assertEquals("AHORRO DE AGUA", lote.getEstrategiaRiego().getNombre());
        lote.setEstrategiaRiego(new EstrategiaIntensiva());
        assertEquals("INTENSIVA", lote.getEstrategiaRiego().getNombre());
    }

    @Test
    @DisplayName("una estrategia escrita en la prueba funciona sin tocar el lote")
    void estrategiaNuevaSinTocarElLote() {
        // Esta clase no existe en el código de producción: el lote no la
        // conoce y no hizo falta modificarlo para admitirla.
        EstrategiaRiego fija = new EstrategiaRiego() {
            @Override public int calcularMinutos(Lectura h, UmbralesLote u) { return 3; }
            @Override public String getNombre() { return "FIJA DE PRUEBA"; }
        };
        lote.setEstrategiaRiego(fija);
        String salida = monitorear();
        assertTrue(salida.contains("FIJA DE PRUEBA"), () -> salida);
        assertTrue(salida.contains("6.0 L aplicados"), () -> salida);   // 3 min x 2 L/min
    }

    @Test
    @DisplayName("una estrategia que devuelve 0 no manda nada al actuador")
    void ceroMinutosNoRiega() {
        lote.setEstrategiaRiego(new EstrategiaSegunClima(new ClimaFijo("EST-LLUVIA", 22.0, 78.0)));
        String salida = monitorear();
        assertTrue(salida.contains("no riega ahora"), () -> salida);
        assertTrue(!salida.contains("L aplicados"), () -> salida);
    }

    // --- fronteras con los otros patrones ---

    @Test
    @DisplayName("FRONTERA: el estado manda sobre la estrategia, no al revés")
    void elEstadoMandaSobreLaEstrategia() {
        lote.setEstrategiaRiego(new EstrategiaIntensiva());
        lote.madurar();
        lote.cosechar();                      // queda en barbecho
        String salida = monitorear();
        assertTrue(salida.contains("Esta en barbecho: no corresponde regar"), () -> salida);
        assertTrue(!salida.contains("L aplicados"), () -> salida);
        // Y la estrategia sigue puesta: el estado no la borró.
        assertEquals("INTENSIVA", lote.getEstrategiaRiego().getNombre());
    }

    @Test
    @DisplayName("FRONTERA: la estrategia decide los minutos; el actuador los aplica")
    void elActuadorSoloAplica() {
        lote.setEstrategiaRiego(new EstrategiaAhorroAgua());
        String conGoteo = monitorear();

        Lote aspersor = new Lote("Maiz", "Maiz", 1.0, UMBRALES)
                .agregarSensor(new SensorFijo("S-H2", "HUMEDAD", 30.0, "%"))
                .setActuador(new RiegoAspersor());
        aspersor.sembrar();
        String conAspersor = monitorear(aspersor);

        // Misma estrategia y mismos 5 minutos, litros distintos: la decisión
        // es de la estrategia y el caudal es del actuador.
        assertTrue(conGoteo.contains("-> 5 min"),    () -> conGoteo);
        assertTrue(conAspersor.contains("-> 5 min"), () -> conAspersor);
        assertTrue(conGoteo.contains("10.0 L"),      () -> conGoteo);
        assertTrue(conAspersor.contains("75.0 L"),   () -> conAspersor);
    }

    @Test
    @DisplayName("la estrategia según clima habla con la interfaz del Adapter, no con el servicio")
    void seApoyaEnElAdapter() {
        ProveedorClima adaptado = new AdaptadorClimaExterno(new ServicioClimaExterno(), "EST-NORTE");
        EstrategiaRiego segunClima = new EstrategiaSegunClima(adaptado);
        // EST-NORTE: 86.5 F = 30.3 C y 0.42 -> 42 %. Con calor, 5 min x 1.5 = 8.
        assertEquals(8, segunClima.calcularMinutos(SECO, UMBRALES));
        assertTrue(segunClima.getNombre().contains("EST-NORTE"));
    }

    // --- validación ---

    @Test
    @DisplayName("el lote rechaza una estrategia nula y conserva la anterior")
    void estrategiaNula() {
        EstrategiaRiego anterior = lote.getEstrategiaRiego();
        assertThrows(IllegalArgumentException.class, () -> lote.setEstrategiaRiego(null));
        assertSame(anterior, lote.getEstrategiaRiego());
    }

    @Test
    @DisplayName("la estrategia según clima exige un proveedor de clima")
    void estrategiaSinClima() {
        assertThrows(IllegalArgumentException.class, () -> new EstrategiaSegunClima(null));
    }

    // --- utilidades ---

    private String monitorear() { return monitorear(lote); }

    /** Ejecuta monitorear() y devuelve lo que imprimió. */
    private String monitorear(Lote cual) {
        capturada.reset();
        cual.monitorear();
        return capturada.toString();
    }
}
