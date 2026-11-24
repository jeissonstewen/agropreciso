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
 * Pruebas unitarias del patrón STATE.
 *
 * Comprueban que el comportamiento del lote cambie con su estado y que las
 * transiciones inválidas se rechacen en lugar de dejar el objeto en un
 * estado incoherente.
 */
@DisplayName("State: ciclo de vida del lote")
class CicloDeVidaLoteTest {

    private final PrintStream salidaOriginal = System.out;
    private Lote lote;

    @BeforeEach
    void prepararLoteYSilenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        lote = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("un lote nuevo nace en preparación y no admite riego")
    void estadoInicial() {
        assertEquals("en preparacion", lote.getEstado().getNombre());
        assertFalse(lote.admiteRiego());
    }

    @Test
    @DisplayName("sembrar lleva a 'sembrado' y habilita el riego")
    void sembrarHabilitaElRiego() {
        lote.sembrar();

        assertEquals("sembrado", lote.getEstado().getNombre());
        assertTrue(lote.admiteRiego());
    }

    @Test
    @DisplayName("el ciclo completo vuelve al punto de partida")
    void cicloCompleto() {
        lote.sembrar();
        lote.madurar();
        assertEquals("maduro", lote.getEstado().getNombre());

        lote.cosechar();
        assertEquals("en barbecho", lote.getEstado().getNombre());
        assertFalse(lote.admiteRiego(), "en barbecho no se riega");

        lote.prepararSuelo();
        assertEquals("en preparacion", lote.getEstado().getNombre());
    }

    @Test
    @DisplayName("no se puede cosechar un lote recién sembrado")
    void transicionInvalida() {
        lote.sembrar();

        IllegalStateException error = assertThrows(IllegalStateException.class, lote::cosechar);

        assertTrue(error.getMessage().contains("sembrado"), error.getMessage());
        assertEquals("sembrado", lote.getEstado().getNombre(),
                "una transicion rechazada no debe cambiar el estado");
    }

    @Test
    @DisplayName("cada estado permite exactamente una transición")
    void cadaEstadoPermiteUna() {
        // en preparación: solo sembrar
        assertThrows(IllegalStateException.class, lote::madurar);
        assertThrows(IllegalStateException.class, lote::cosechar);
        assertThrows(IllegalStateException.class, lote::prepararSuelo);
        lote.sembrar();

        // sembrado: solo madurar
        assertThrows(IllegalStateException.class, lote::sembrar);
        assertThrows(IllegalStateException.class, lote::cosechar);
        lote.madurar();

        // maduro: solo cosechar
        assertThrows(IllegalStateException.class, lote::madurar);
        lote.cosechar();

        // en barbecho: solo preparar el suelo
        assertThrows(IllegalStateException.class, lote::sembrar);
        lote.prepararSuelo();

        assertEquals("en preparacion", lote.getEstado().getNombre());
    }

    @Test
    @DisplayName("el estado manda sobre la lectura: en barbecho no se riega aunque esté seco")
    void elEstadoMandaSobreLaLectura() {
        lote.sembrar();
        lote.madurar();
        lote.cosechar();                       // queda en barbecho

        // El sensor marca 64 % con umbral 70 %: está seco, pero no corresponde regar.
        assertFalse(lote.admiteRiego());
    }

    @Test
    @DisplayName("el estado aparece en describir()")
    void elEstadoSeReporta() {
        lote.sembrar();

        assertTrue(lote.describir(0).contains("sembrado"), lote.describir(0));
    }

    @Test
    @DisplayName("el mensaje de rechazo nombra el lote y su estado")
    void mensajeDeRechazoUtil() {
        IllegalStateException error = assertThrows(IllegalStateException.class, lote::cosechar);

        assertTrue(error.getMessage().contains("Cafe-Norte"), error.getMessage());
        assertTrue(error.getMessage().contains("en preparacion"), error.getMessage());
    }
}
