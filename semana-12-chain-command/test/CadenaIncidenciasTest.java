import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón CHAIN OF RESPONSIBILITY.
 *
 * Comprueban que la incidencia la atienda el eslabón correcto, que la
 * cadena se detenga ahí, y que reordenarla cambie el desenlace sin tocar
 * ningún manejador.
 */
@DisplayName("Chain of Responsibility: atención de incidencias")
class CadenaIncidenciasTest {

    private final PrintStream salidaOriginal = System.out;

    private Lote lote;
    private ManejadorIncidencia cadena;

    /** La cadena imprime su recorrido; se silencia para no ensuciar el informe. */
    @BeforeEach
    void prepararCadenaYSilenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        lote = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
        cadena = new AtencionOperario();
        cadena.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("una incidencia LEVE la atiende el primer eslabón")
    void leveLaAtiendeElOperario() {
        ResultadoAtencion r = cadena.manejar(new Incidencia(lote, TipoIncidencia.HUMEDAD_BAJA));

        assertTrue(r.fueAtendida());
        assertEquals("OPERARIO", r.getAtendidaPor());
    }

    @Test
    @DisplayName("una incidencia MEDIA escala hasta el agrónomo")
    void mediaEscalaAlAgronomo() {
        ResultadoAtencion r = cadena.manejar(new Incidencia(lote, TipoIncidencia.UMBRAL_INADECUADO));

        assertEquals("AGRONOMO", r.getAtendidaPor());
    }

    @Test
    @DisplayName("una incidencia CRITICA recorre la cadena completa")
    void criticaLlegaAlAdministrador() {
        ResultadoAtencion r = cadena.manejar(new Incidencia(lote, TipoIncidencia.SENSOR_AVERIADO));

        assertEquals("ADMINISTRADOR", r.getAtendidaPor());
    }

    @Test
    @DisplayName("el eslabón que atiende detiene la cadena: produce una sola orden")
    void soloUnEslabonAtiende() {
        ResultadoAtencion r = cadena.manejar(new Incidencia(lote, TipoIncidencia.HUMEDAD_BAJA));

        // Si siguieran actuando los demás, la orden no sería la del operario.
        assertEquals("OPERARIO", r.getAtendidaPor());
        assertTrue(r.getOrden() instanceof OrdenRegar,
                "la orden deberia ser la del operario, no la de otro eslabon");
    }

    @Test
    @DisplayName("si ningún eslabón puede, la incidencia queda sin atender")
    void cadenaIncompleta() {
        ManejadorIncidencia soloOperario = new AtencionOperario();

        ResultadoAtencion r = soloOperario.manejar(
                new Incidencia(lote, TipoIncidencia.SENSOR_AVERIADO));

        assertFalse(r.fueAtendida());
        assertNull(r.getAtendidaPor());
        assertNull(r.getOrden());
    }

    @Test
    @DisplayName("reordenar la cadena cambia el recorrido sin tocar los manejadores")
    void reordenarLaCadena() {
        ManejadorIncidencia alReves = new AtencionAdministrador();
        alReves.encadenar(new AtencionAgronomo()).encadenar(new AtencionOperario());

        ResultadoAtencion r = alReves.manejar(new Incidencia(lote, TipoIncidencia.HUMEDAD_BAJA));

        // Sigue atendiéndola el operario, pero ahora es el ÚLTIMO en recibirla.
        assertEquals("OPERARIO", r.getAtendidaPor());
    }

    @Test
    @DisplayName("la cadena produce la orden pero no la ejecuta")
    void laCadenaNoEjecuta() {
        double umbralAntes = lote.getUmbrales().getHumedadMin();

        ResultadoAtencion r = cadena.manejar(new Incidencia(lote, TipoIncidencia.UMBRAL_INADECUADO));

        assertNotNull(r.getOrden());
        assertEquals(umbralAntes, lote.getUmbrales().getHumedadMin(), 0.001,
                "la cadena no deberia haber cambiado nada todavia");
    }

    @Test
    @DisplayName("el tipo de incidencia determina su severidad")
    void elTipoFijaLaSeveridad() {
        assertEquals(Severidad.LEVE,    TipoIncidencia.HUMEDAD_BAJA.getSeveridad());
        assertEquals(Severidad.MEDIA,   TipoIncidencia.UMBRAL_INADECUADO.getSeveridad());
        assertEquals(Severidad.CRITICA, TipoIncidencia.SENSOR_AVERIADO.getSeveridad());
    }

    @Test
    @DisplayName("una incidencia exige lote y tipo")
    void incidenciaInvalida() {
        assertThrows(NullPointerException.class, () -> new Incidencia(null, TipoIncidencia.HUMEDAD_BAJA));
        assertThrows(NullPointerException.class, () -> new Incidencia(lote, null));
    }

    @Test
    @DisplayName("la incidencia guarda la lectura que la originó")
    void incidenciaConLectura() {
        Lectura lectura = lote.getSensores().get(0).leer();

        Incidencia i = new Incidencia(lote, TipoIncidencia.HUMEDAD_BAJA, lectura);

        assertEquals(64.0, i.getLectura().getValor(), 0.001);
        assertEquals(Severidad.LEVE, i.getSeveridad());
    }
}
