import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón COMMAND.
 *
 * Comprueban las tres cosas que el patrón compra —parametrizar, encolar y
 * deshacer— y que una orden irreversible se rechace en vez de simular una
 * reversión.
 */
@DisplayName("Command: órdenes de operación y panel de control")
class OrdenesPanelTest {

    private static final double DELTA = 0.001;

    private final PrintStream salidaOriginal = System.out;

    private Lote lote;
    private PanelDeControl panel;

    @BeforeEach
    void prepararPanelYSilenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        lote = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
        panel = new PanelDeControl();
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    // ---------------- ejecutar ----------------

    @Test
    @DisplayName("ejecutar una orden aplica su efecto y la registra")
    void ejecutarRegistra() {
        panel.ejecutar(new OrdenCambiarUmbralHumedad(lote, 55.0));

        assertEquals(55.0, lote.getUmbrales().getHumedadMin(), DELTA);
        assertEquals(1, panel.cuantasEjecutadas());
    }

    @Test
    @DisplayName("instalar un sensor lo agrega al lote")
    void instalarSensor() {
        panel.ejecutar(new OrdenInstalarSensor(lote, new SensorFijo("REP", "HUMEDAD", 68.0, "%")));

        assertEquals(2, lote.contarSensores());
    }

    // ---------------- deshacer ----------------

    @Test
    @DisplayName("deshacer restaura el valor anterior del umbral")
    void deshacerRestaura() {
        panel.ejecutar(new OrdenCambiarUmbralHumedad(lote, 55.0));

        assertTrue(panel.deshacerUltima());
        assertEquals(70.0, lote.getUmbrales().getHumedadMin(), DELTA);
        assertEquals(0, panel.cuantasEjecutadas());
    }

    @Test
    @DisplayName("deshacer retira exactamente el sensor que se instaló")
    void deshacerRetiraElSensor() {
        panel.ejecutar(new OrdenInstalarSensor(lote, new SensorFijo("REP", "HUMEDAD", 68.0, "%")));

        panel.deshacerUltima();

        assertEquals(1, lote.contarSensores());
        assertEquals("CN-H", lote.getSensores().get(0).leer().getIdSensor(),
                "deberia quedar el sensor original");
    }

    @Test
    @DisplayName("el historial es una pila: se deshace la última ejecutada")
    void historialEsPila() {
        panel.ejecutar(new OrdenCambiarUmbralHumedad(lote, 55.0));
        panel.ejecutar(new OrdenCambiarUmbralHumedad(lote, 40.0));

        panel.deshacerUltima();

        assertEquals(55.0, lote.getUmbrales().getHumedadMin(), DELTA,
                "deberia volver al valor intermedio, no al original");
    }

    @Test
    @DisplayName("deshacer sin historial no revienta")
    void deshacerSinHistorial() {
        assertFalse(panel.deshacerUltima());
    }

    // ---------------- lo irreversible ----------------

    @Test
    @DisplayName("una orden de riego se declara no reversible")
    void elRiegoNoEsReversible() {
        assertFalse(new OrdenRegar(lote, 10).esReversible());
    }

    @Test
    @DisplayName("el panel rechaza deshacer un riego y lo conserva en la bitácora")
    void elPanelRechazaDeshacerElRiego() {
        panel.ejecutar(new OrdenRegar(lote, 10));

        assertFalse(panel.deshacerUltima());
        assertEquals(1, panel.cuantasEjecutadas(), "la operacion ocurrio: debe seguir registrada");
    }

    @Test
    @DisplayName("llamar deshacer() directamente sobre un riego falla en voz alta")
    void deshacerRiegoDirectamenteFalla() {
        Orden riego = new OrdenRegar(lote, 10);

        UnsupportedOperationException error =
                assertThrows(UnsupportedOperationException.class, riego::deshacer);

        assertTrue(error.getMessage().contains("agua"), error.getMessage());
    }

    // ---------------- encolar ----------------

    @Test
    @DisplayName("las pendientes no se ejecutan hasta que se piden")
    void programarNoEjecuta() {
        panel.programar(new OrdenCambiarUmbralHumedad(lote, 55.0));

        assertEquals(1, panel.cuantasPendientes());
        assertEquals(0, panel.cuantasEjecutadas());
        assertEquals(70.0, lote.getUmbrales().getHumedadMin(), DELTA);
    }

    @Test
    @DisplayName("la cola es FIFO: se ejecutan en el orden en que se programaron")
    void colaEsFifo() {
        panel.programar(new OrdenCambiarUmbralHumedad(lote, 55.0));
        panel.programar(new OrdenCambiarUmbralHumedad(lote, 40.0));

        assertEquals(2, panel.ejecutarPendientes());
        assertEquals(40.0, lote.getUmbrales().getHumedadMin(), DELTA,
                "la ultima programada debe ser la ultima aplicada");
        assertEquals(0, panel.cuantasPendientes());
    }

    // ---------------- validaciones ----------------

    @Test
    @DisplayName("una orden de riego exige minutos positivos")
    void riegoConMinutosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new OrdenRegar(lote, 0));
    }

    @Test
    @DisplayName("un umbral de humedad fuera de 0 a 100 se rechaza")
    void umbralFueraDeRango() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrdenCambiarUmbralHumedad(lote, 120.0));
    }

    @Test
    @DisplayName("el panel no acepta órdenes nulas")
    void panelRechazaNulos() {
        assertThrows(NullPointerException.class, () -> panel.ejecutar(null));
        assertThrows(NullPointerException.class, () -> panel.programar(null));
    }

    // ---------------- los dos patrones juntos ----------------

    @Test
    @DisplayName("la orden que produce la cadena la ejecuta el panel")
    void cadenaYPanelSeComponen() {
        ManejadorIncidencia cadena = new AtencionOperario();
        cadena.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());

        ResultadoAtencion r = cadena.manejar(new Incidencia(lote, TipoIncidencia.UMBRAL_INADECUADO));
        panel.ejecutar(r.getOrden());

        assertEquals("AGRONOMO", r.getAtendidaPor());
        assertEquals(70.0, lote.getUmbrales().getHumedadMin(), DELTA);
        assertEquals(1, panel.cuantasEjecutadas());
    }
}
