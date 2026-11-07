import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón MEDIATOR.
 *
 * Comprueban el enrutamiento de cada evento, que agregar o quitar un
 * colega no afecte a los demás, y —la más importante— que ningún colega
 * guarde una referencia a otro.
 */
@DisplayName("Mediator: centro de operaciones")
class CentroDeOperacionesTest {

    private final PrintStream salidaOriginal = System.out;

    private CentroDeOperaciones centro;
    private GestorIncidencias gestor;
    private OperacionesCampo operaciones;
    private Bitacora bitacora;
    private ZonaCultivo finca;

    @BeforeEach
    void armarEquipoYSilenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        centro = new CentroDeOperaciones();

        ManejadorIncidencia cadena = new AtencionOperario();
        cadena.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());

        gestor      = new GestorIncidencias(centro, cadena);
        operaciones = new OperacionesCampo(centro, new PanelDeControl());
        bitacora    = new Bitacora(centro);

        centro.registrarGestor(gestor);
        centro.registrarOperaciones(operaciones);
        centro.registrarBitacora(bitacora);

        // Un lote seco (64 % frente a un umbral de 70 %) y uno correcto.
        finca = new ZonaCultivo("Finca")
                .agregar(seco("Cafe-Norte"))
                .agregar(correcto("Cafe-Sur"));
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    private Lote seco(String nombre) {
        return new Lote(nombre, "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo(nombre + "-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
    }

    private Lote correcto(String nombre) {
        return new Lote(nombre, "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo(nombre + "-H", "HUMEDAD", 85.0, "%"))
                .setActuador(new RiegoGoteo());
    }

    // ---------------- el flujo completo ----------------

    @Test
    @DisplayName("una anomalía recorre los cuatro colegas sin que se conozcan")
    void flujoCompleto() {
        new EstacionSensores(centro).inspeccionar(finca, TipoRecorrido.PROFUNDIDAD);

        // 3 avisos por el lote seco: anomalía, orden generada, orden ejecutada.
        assertEquals(3, centro.getAvisosRecibidos());
        assertEquals(1, operaciones.getPanel().cuantasEjecutadas());
        assertEquals(1, bitacora.cuantasEntradas());
    }

    @Test
    @DisplayName("un lote sin anomalías no genera ningún aviso")
    void sinAnomaliasNoHayAvisos() {
        ZonaCultivo sana = new ZonaCultivo("Finca").agregar(correcto("Cafe-Sur"));

        new EstacionSensores(centro).inspeccionar(sana, TipoRecorrido.PROFUNDIDAD);

        assertEquals(0, centro.getAvisosRecibidos());
        assertEquals(0, bitacora.cuantasEntradas());
    }

    @Test
    @DisplayName("la bitácora registra la descripción de la orden ejecutada")
    void laBitacoraRegistraLaOrden() {
        new EstacionSensores(centro).inspeccionar(finca, TipoRecorrido.PROFUNDIDAD);

        assertTrue(bitacora.getEntradas().get(0).contains("Cafe-Norte"),
                bitacora.getEntradas().toString());
    }

    // ---------------- agregar y quitar colegas ----------------

    @Test
    @DisplayName("sin bitácora registrada el sistema sigue funcionando")
    void laBitacoraEsOpcional() {
        CentroDeOperaciones minimo = new CentroDeOperaciones();
        ManejadorIncidencia cadena = new AtencionOperario();
        cadena.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());
        OperacionesCampo ops = new OperacionesCampo(minimo, new PanelDeControl());
        minimo.registrarGestor(new GestorIncidencias(minimo, cadena));
        minimo.registrarOperaciones(ops);
        // no se registra bitácora

        new EstacionSensores(minimo).inspeccionar(finca, TipoRecorrido.PROFUNDIDAD);

        assertEquals(1, ops.getPanel().cuantasEjecutadas(),
                "los demas colegas deben seguir trabajando");
    }

    @Test
    @DisplayName("si falta un colega imprescindible, el centro lo dice claramente")
    void faltaUnColegaImprescindible() {
        CentroDeOperaciones vacio = new CentroDeOperaciones();
        EstacionSensores estacion = new EstacionSensores(vacio);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> estacion.inspeccionar(finca, TipoRecorrido.PROFUNDIDAD));

        assertTrue(error.getMessage().contains("gestor de incidencias"), error.getMessage());
    }

    // ---------------- la invariante del patrón ----------------

    @Test
    @DisplayName("NINGÚN colega guarda una referencia a otro colega")
    void losColegasNoSeConocen() throws Exception {
        Colega[] colegas = { new EstacionSensores(centro), gestor, operaciones, bitacora };

        for (Colega colega : colegas) {
            for (Field campo : colega.getClass().getDeclaredFields()) {
                assertTrue(!Colega.class.isAssignableFrom(campo.getType()),
                        colega.getNombre() + " no deberia tener un campo de tipo Colega: "
                                + campo.getName());
            }
        }
    }

    @Test
    @DisplayName("todo colega conoce al mediador")
    void todoColegaConoceAlMediador() throws Exception {
        Field campo = Colega.class.getDeclaredField("mediador");

        assertEquals(Mediador.class, campo.getType());
        assertTrue(java.lang.reflect.Modifier.isFinal(campo.getModifiers()),
                "la referencia al mediador no deberia cambiar");
    }

    @Test
    @DisplayName("un colega exige mediador y nombre")
    void colegaInvalido() {
        assertThrows(NullPointerException.class, () -> new GestorIncidencias(null, null));
    }

    @Test
    @DisplayName("el centro cuenta todos los avisos que enruta")
    void elCentroCuentaLosAvisos() {
        ZonaCultivo dosSecos = new ZonaCultivo("Finca")
                .agregar(seco("Cafe-Norte")).agregar(seco("Cafe-Alto"));

        new EstacionSensores(centro).inspeccionar(dosSecos, TipoRecorrido.PROFUNDIDAD);

        assertEquals(6, centro.getAvisosRecibidos(), "3 avisos por cada lote seco");
    }

    @Test
    @DisplayName("la orden que ejecuta el panel es exactamente la que produjo la cadena")
    void laOrdenViajaIntactaPorElCentro() {
        new EstacionSensores(centro).inspeccionar(finca, TipoRecorrido.PROFUNDIDAD);

        assertEquals(1, operaciones.getPanel().getHistorial().size());
        assertTrue(operaciones.getPanel().getHistorial().get(0).contains("Regar Cafe-Norte"),
                operaciones.getPanel().getHistorial().toString());
    }

    @Test
    @DisplayName("el flujo termina: el centro no se reenvía avisos a sí mismo")
    void elFlujoTermina() {
        // Si ORDEN_EJECUTADA volviera a disparar una orden, esto no pararía.
        assertTimeoutPreemptively(java.time.Duration.ofSeconds(2), () ->
                new EstacionSensores(centro).inspeccionar(finca, TipoRecorrido.PROFUNDIDAD));

        assertEquals(3, centro.getAvisosRecibidos(), "la cadena de avisos debe ser finita");
    }
}
