import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón FACADE.
 *
 * Comprueban que las operaciones de negocio funcionen sin que el cliente
 * toque el subsistema, que la fachada siga permitiendo el acceso directo a
 * él, y que valide el estado antes de operar.
 *
 * La fachada escribe por consola —es una aplicación de consola—, así que
 * las pruebas capturan la salida estándar y comprueban sobre el texto.
 */
@DisplayName("Facade: fachada de AgroPrecisión")
class FachadaAgroPrecisionTest {

    private final PrintStream salidaOriginal = System.out;
    private ByteArrayOutputStream capturada;
    private FachadaAgroPrecision sistema;

    @BeforeEach
    void prepararSistemaYCapturarSalida() {
        capturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturada));
        sistema = new FachadaAgroPrecision(
                new ClimaConCache(
                        new AdaptadorClimaExterno(new ServicioClimaInestable(0), "EST-NORTE")));
    }

    @AfterEach
    void cerrarYRestaurar() {
        // La sesión es un Singleton: se cierra para no contaminar la prueba siguiente.
        SesionUsuario.getInstancia().cerrarSesion();
        System.setOut(salidaOriginal);
    }

    private String texto() {
        return capturada.toString();
    }

    @Test
    @DisplayName("iniciarJornada() acepta credenciales válidas")
    void jornadaConCredencialesValidas() {
        assertTrue(sistema.iniciarJornada("admin", "1234"));
        assertTrue(SesionUsuario.getInstancia().estaActiva());
    }

    @Test
    @DisplayName("iniciarJornada() rechaza credenciales inválidas")
    void jornadaConCredencialesInvalidas() {
        assertFalse(sistema.iniciarJornada("admin", "clave-mala"));
        assertFalse(SesionUsuario.getInstancia().estaActiva());
    }

    @Test
    @DisplayName("sin jornada iniciada la fachada no opera")
    void exigeJornadaIniciada() {
        assertThrows(IllegalStateException.class, () -> sistema.monitorearFinca());
        assertThrows(IllegalStateException.class, () -> sistema.emitirReporteEstado("consola"));
    }

    @Test
    @DisplayName("monitorearFinca() recorre el árbol completo con una sola llamada")
    void monitorearRecorreTodaLaFinca() {
        sistema.iniciarJornada("admin", "1234");

        sistema.monitorearFinca();

        String salida = texto();
        assertTrue(salida.contains("Zona Invernadero"), salida);
        assertTrue(salida.contains("Lote Cafe-Norte"), salida);
        assertTrue(salida.contains("Lote Maiz-Este"), salida);
    }

    @Test
    @DisplayName("emitirReporteEstado('csv') usa el Bridge con la salida CSV")
    void reporteEnCsv() {
        sistema.iniciarJornada("admin", "1234");

        sistema.emitirReporteEstado("csv");

        assertTrue(texto().contains("sensor,tipo,valor,unidad"), texto());
    }

    @Test
    @DisplayName("emitirReporteEstado('markdown') cambia el formato con un parámetro")
    void reporteEnMarkdown() {
        sistema.iniciarJornada("admin", "1234");

        sistema.emitirReporteEstado("markdown");

        assertTrue(texto().contains("| sensor | tipo | valor | unidad |"), texto());
    }

    @Test
    @DisplayName("un formato no soportado se rechaza con un mensaje claro")
    void formatoInvalido() {
        sistema.iniciarJornada("admin", "1234");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sistema.emitirReporteEstado("pdf"));

        assertTrue(error.getMessage().contains("pdf"), error.getMessage());
    }

    @Test
    @DisplayName("emitirReporteAlertas() deriva las alertas de los umbrales de cada lote")
    void reporteDeAlertas() {
        sistema.iniciarJornada("admin", "1234");

        sistema.emitirReporteAlertas("consola");

        // Cafe-Norte mide 64.5 % tras calibrar, por debajo de su umbral de 70 %.
        assertTrue(texto().contains("Humedad baja en Cafe-Norte"), texto());
    }

    @Test
    @DisplayName("consultarClima() usa el proveedor decorado sin saberlo")
    void consultarClimaUsaElDecorado() {
        sistema.iniciarJornada("admin", "1234");

        sistema.consultarClima();

        String salida = texto();
        assertTrue(salida.contains("EST-NORTE"), salida);
        assertTrue(salida.contains("TEMPERATURA"), salida);
    }

    @Test
    @DisplayName("la fachada no encierra el subsistema: getFinca() da acceso directo")
    void laFachadaNoEncierra() {
        ZonaCultivo finca = sistema.getFinca();

        assertNotNull(finca);
        assertEquals(9.5, finca.getAreaHectareas(), 0.001);
        assertEquals(4, finca.contarSensores());
    }

    @Test
    @DisplayName("cerrarJornada() deja el sistema sin sesión activa")
    void cerrarJornada() {
        sistema.iniciarJornada("admin", "1234");

        sistema.cerrarJornada();

        assertFalse(SesionUsuario.getInstancia().estaActiva());
    }
}
