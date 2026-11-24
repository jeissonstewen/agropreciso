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
 * Pruebas unitarias del patrón OBSERVER.
 *
 * Comprueban que el sujeto difunda a todos sus suscriptores, que
 * desuscribirse deje de notificar y que el sujeto funcione sin nadie
 * escuchando.
 */
@DisplayName("Observer: publicación de alertas")
class AlertasObserverTest {

    private final PrintStream salidaOriginal = System.out;

    private MonitorDeAlertas monitor;
    private HistorialAlertas historial;
    private ContadorPorSeveridad contador;
    private ZonaCultivo finca;
    private Lote seco;

    @BeforeEach
    void prepararYSilenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        monitor   = new MonitorDeAlertas();
        historial = new HistorialAlertas();
        contador  = new ContadorPorSeveridad();

        // 45 % frente a un umbral de 70 %: faltan 25 puntos -> CRITICA
        seco = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CS-H", "HUMEDAD", 45.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote sano = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 52.0, "%"))
                .setActuador(new RiegoAspersor());
        seco.sembrar();
        sano.sembrar();

        finca = new ZonaCultivo("Finca").agregar(seco).agregar(sano);
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("todos los suscritos reciben la misma alerta")
    void difusionATodos() {
        monitor.suscribir(historial);
        monitor.suscribir(contador);

        int publicadas = monitor.revisarFinca(finca);

        assertEquals(1, publicadas, "solo el lote seco genera alerta");
        assertEquals(1, historial.cuantas());
        assertEquals(1, contador.total());
    }

    @Test
    @DisplayName("desuscribirse deja de notificar")
    void desuscribirse() {
        monitor.suscribir(historial);
        monitor.suscribir(contador);
        monitor.revisarFinca(finca);

        assertTrue(monitor.quitar(historial));
        monitor.revisarFinca(finca);

        assertEquals(1, historial.cuantas(), "el historial no deberia haber crecido");
        assertEquals(2, contador.total(), "el contador si sigue recibiendo");
    }

    @Test
    @DisplayName("el sujeto funciona sin ningún observador")
    void sinObservadores() {
        assertEquals(0, monitor.cuantosObservadores());

        int publicadas = monitor.revisarFinca(finca);

        assertEquals(1, publicadas, "detecta igual, aunque nadie escuche");
        assertEquals(1, monitor.getAlertasPublicadas());
    }

    @Test
    @DisplayName("suscribir dos veces al mismo observador no lo duplica")
    void sinDuplicados() {
        monitor.suscribir(historial);
        monitor.suscribir(historial);

        assertEquals(1, monitor.cuantosObservadores());

        monitor.revisarFinca(finca);
        assertEquals(1, historial.cuantas(), "no deberia recibir la alerta dos veces");
    }

    @Test
    @DisplayName("la severidad crece con la distancia al umbral")
    void severidadSegunLaDistancia() {
        monitor.suscribir(contador);

        monitor.revisarFinca(finca);

        // 70 - 45 = 25 puntos por debajo -> CRITICA
        assertEquals(1, contador.contar(Severidad.CRITICA));
        assertEquals(0, contador.contar(Severidad.LEVE));
    }

    @Test
    @DisplayName("un lote cuyo estado no admite riego no genera alerta de humedad")
    void elEstadoFiltraLasAlertas() {
        monitor.suscribir(historial);
        seco.madurar();
        seco.cosechar();                       // queda en barbecho

        int publicadas = monitor.revisarFinca(finca);

        assertEquals(0, publicadas, "en barbecho no tiene sentido avisar de sequia");
        assertEquals(0, historial.cuantas());
    }

    @Test
    @DisplayName("un cambio de estado también se publica")
    void publicarCambioDeEstado() {
        monitor.suscribir(historial);
        String antes = seco.getEstado().getNombre();

        seco.madurar();
        monitor.publicarCambioDeEstado(seco, antes);

        assertEquals(1, historial.cuantas());
        assertTrue(historial.getAlertas().get(0).getMensaje().contains("maduro"),
                historial.getAlertas().get(0).toString());
    }

    @Test
    @DisplayName("quitar un observador que no estaba suscrito devuelve false")
    void quitarNoSuscrito() {
        assertFalse(monitor.quitar(historial));
    }

    @Test
    @DisplayName("no se puede suscribir null")
    void suscribirNulo() {
        assertThrows(NullPointerException.class, () -> monitor.suscribir(null));
    }

    @Test
    @DisplayName("la alerta es inmutable y llega igual a todos")
    void alertaInmutable() {
        monitor.suscribir(historial);
        monitor.suscribir(contador);

        monitor.revisarFinca(finca);

        Alerta alerta = historial.getAlertas().get(0);
        assertEquals("Cafe-Sur", alerta.getLote());
        assertEquals(Severidad.CRITICA, alerta.getSeveridad());
    }
}
