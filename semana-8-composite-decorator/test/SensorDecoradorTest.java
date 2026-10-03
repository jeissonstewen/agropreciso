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
 * Pruebas unitarias del patrón DECORATOR.
 *
 * Verifican que cada decorador agregue su comportamiento sin romper la
 * interfaz Sensor, que se puedan apilar y que un sensor decorado siga
 * sirviendo donde se espera uno normal.
 */
@DisplayName("Decorator: sensores decorados")
class SensorDecoradorTest {

    private static final double DELTA = 0.001;

    private final PrintStream salidaOriginal = System.out;
    private Sensor base;

    @BeforeEach
    void prepararSensorYSilenciarSalida() {
        base = new SensorFijo("S-01", "HUMEDAD", 64.0, "%");
        // SensorConRegistro imprime; se silencia para que el informe de
        // pruebas quede limpio. Las comprobaciones usan getHistorial().
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("la calibración aplica el offset al valor")
    void calibracionAplicaElOffset() {
        Sensor calibrado = new SensorConCalibracion(base, -1.5);

        assertEquals(62.5, calibrado.leer().getValor(), DELTA);
    }

    @Test
    @DisplayName("el decorador conserva id, tipo y unidad del sensor envuelto")
    void conservaLosDemasCampos() {
        Lectura lectura = new SensorConCalibracion(base, -1.5).leer();

        assertEquals("S-01", lectura.getIdSensor());
        assertEquals("HUMEDAD", lectura.getTipo());
        assertEquals("%", lectura.getUnidad());
    }

    @Test
    @DisplayName("dos calibraciones apiladas acumulan sus offsets")
    void calibracionesApiladasSeAcumulan() {
        Sensor doble = new SensorConCalibracion(new SensorConCalibracion(base, -1.5), 0.5);

        assertEquals(63.0, doble.leer().getValor(), DELTA);   // 64.0 - 1.5 + 0.5
    }

    @Test
    @DisplayName("la validación acota una lectura por debajo del mínimo")
    void validacionCorrigePorDebajo() {
        Sensor averiado = new SensorFijo("S-02", "HUMEDAD", -40.0, "%");
        SensorConValidacion validado = new SensorConValidacion(averiado, 0.0, 100.0);

        assertEquals(0.0, validado.leer().getValor(), DELTA);
        assertEquals(1, validado.getCorrecciones());
    }

    @Test
    @DisplayName("la validación acota una lectura por encima del máximo")
    void validacionCorrigePorEncima() {
        Sensor averiado = new SensorFijo("S-03", "HUMEDAD", 180.0, "%");
        SensorConValidacion validado = new SensorConValidacion(averiado, 0.0, 100.0);

        assertEquals(100.0, validado.leer().getValor(), DELTA);
    }

    @Test
    @DisplayName("la validación no toca una lectura que está en rango")
    void validacionRespetaLoCorrecto() {
        SensorConValidacion validado = new SensorConValidacion(base, 0.0, 100.0);

        Lectura lectura = validado.leer();

        assertEquals(64.0, lectura.getValor(), DELTA);
        assertEquals(0, validado.getCorrecciones());
    }

    @Test
    @DisplayName("el registro guarda el historial sin alterar el valor")
    void registroNoAlteraElValor() {
        SensorConRegistro vigilado = new SensorConRegistro(base);

        vigilado.leer();
        Lectura segunda = vigilado.leer();

        assertEquals(64.0, segunda.getValor(), DELTA);
        assertEquals(2, vigilado.getHistorial().size());
    }

    @Test
    @DisplayName("el orden de la pila importa: validar y luego calibrar")
    void elOrdenDeLaPilaImporta() {
        Sensor averiado = new SensorFijo("S-04", "HUMEDAD", -40.0, "%");

        // Primero se acota a 0 y después se le suman 2.
        Sensor validarYCalibrar =
                new SensorConCalibracion(new SensorConValidacion(averiado, 0.0, 100.0), 2.0);
        // Al revés: se suman 2 (-38) y después se acota a 0.
        Sensor calibrarYValidar =
                new SensorConValidacion(new SensorConCalibracion(averiado, 2.0), 0.0, 100.0);

        assertEquals(2.0, validarYCalibrar.leer().getValor(), DELTA);
        assertEquals(0.0, calibrarYValidar.leer().getValor(), DELTA);
    }

    @Test
    @DisplayName("getDescripcion() encadena los decoradores aplicados")
    void describeLaCadena() {
        SensorConRegistro cadena = new SensorConRegistro(
                new SensorConCalibracion(new SensorConValidacion(base, 0.0, 100.0), 2.0));

        String descripcion = cadena.getDescripcion();

        assertTrue(descripcion.contains("validacion"), descripcion);
        assertTrue(descripcion.contains("calibracion"), descripcion);
        assertTrue(descripcion.endsWith("registro"), descripcion);
    }

    @Test
    @DisplayName("un decorador exige un sensor que envolver")
    void noSePuedeDecorarNada() {
        assertThrows(IllegalArgumentException.class, () -> new SensorConRegistro(null));
    }

    @Test
    @DisplayName("un sensor decorado sirve donde se espera un Sensor: dentro de un Lote")
    void elDecoradoEncajaEnElComposite() {
        Sensor decorado = new SensorConRegistro(new SensorConCalibracion(base, -1.5));
        Lote lote = new Lote("Cafe-Vigilado", "cafe", 1.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(decorado);
        ZonaCultivo zona = new ZonaCultivo("Invernadero").agregar(lote);

        // Ni el lote ni la zona saben que hay decoradores de por medio.
        assertEquals(1, zona.contarSensores());
        assertSame(decorado, lote.getSensores().get(0));
        assertEquals(62.5, lote.getSensores().get(0).leer().getValor(), DELTA);
    }
}
