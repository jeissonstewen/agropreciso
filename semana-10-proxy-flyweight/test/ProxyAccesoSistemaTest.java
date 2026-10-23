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
 * Pruebas unitarias del patrón PROXY de protección.
 *
 * Verifican la tabla de permisos por rol, que el proxy bloquee ANTES de
 * llegar al subsistema y que sea intercambiable con la fachada real.
 */
@DisplayName("Proxy: control de acceso al sistema AgroPrecisión")
class ProxyAccesoSistemaTest {

    private final PrintStream salidaOriginal = System.out;
    private ByteArrayOutputStream capturada;
    private SistemaAgroPrecision proxy;
    private SistemaAgroPrecision espia;

    /**
     * Doble de prueba que registra si el subsistema llegó a ser invocado.
     * Es lo que permite comprobar que el proxy bloquea ANTES de delegar.
     */
    private static class SistemaEspia implements SistemaAgroPrecision {
        private int llamadasRecibidas = 0;

        @Override public boolean iniciarJornada(String usuario, String clave) {
            return SesionUsuario.getInstancia().iniciarSesion(usuario, clave);
        }
        @Override public void monitorearFinca()                  { llamadasRecibidas++; }
        @Override public void consultarClima()                   { llamadasRecibidas++; }
        @Override public void emitirReporteEstado(String f)      { llamadasRecibidas++; }
        @Override public void emitirReporteAlertas(String f)     { llamadasRecibidas++; }
        @Override public void cerrarJornada()                    { llamadasRecibidas++; }

        int getLlamadasRecibidas() { return llamadasRecibidas; }
    }

    @BeforeEach
    void prepararProxyYCapturarSalida() {
        capturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturada));
        espia = new SistemaEspia();
        proxy = new ProxyAccesoSistema(espia);
    }

    @AfterEach
    void cerrarSesionYRestaurar() {
        // La sesión es un Singleton: su estado sobrevive entre pruebas.
        SesionUsuario.getInstancia().cerrarSesion();
        System.setOut(salidaOriginal);
    }

    // ---------------- sin sesión ----------------

    @Test
    @DisplayName("sin jornada iniciada el proxy bloquea toda operación")
    void sinSesionNoSeOpera() {
        assertThrows(SecurityException.class, () -> proxy.monitorearFinca());
        assertThrows(SecurityException.class, () -> proxy.consultarClima());
        assertThrows(SecurityException.class, () -> proxy.emitirReporteEstado("consola"));
    }

    @Test
    @DisplayName("el proxy bloquea ANTES de llegar al subsistema")
    void bloqueaAntesDeDelegar() {
        assertThrows(SecurityException.class, () -> proxy.monitorearFinca());

        assertEquals(0, ((SistemaEspia) espia).getLlamadasRecibidas(),
                "el subsistema no deberia haberse enterado de la llamada");
    }

    @Test
    @DisplayName("iniciarJornada() no exige sesión previa: es la autenticación")
    void iniciarJornadaNoExigeSesion() {
        assertTrue(proxy.iniciarJornada("operario", "riego"));
    }

    // ---------------- OPERARIO ----------------

    @Test
    @DisplayName("el OPERARIO puede monitorear y consultar el clima")
    void operarioMonitorea() {
        proxy.iniciarJornada("operario", "riego");

        proxy.monitorearFinca();
        proxy.consultarClima();

        assertEquals(2, ((SistemaEspia) espia).getLlamadasRecibidas());
    }

    @Test
    @DisplayName("el OPERARIO no puede emitir reportes")
    void operarioNoReporta() {
        proxy.iniciarJornada("operario", "riego");

        SecurityException error = assertThrows(SecurityException.class,
                () -> proxy.emitirReporteEstado("consola"));

        assertTrue(error.getMessage().contains("AGRONOMO"), error.getMessage());
    }

    @Test
    @DisplayName("el OPERARIO no puede cerrar la jornada")
    void operarioNoCierra() {
        proxy.iniciarJornada("operario", "riego");

        SecurityException error =
                assertThrows(SecurityException.class, () -> proxy.cerrarJornada());

        assertTrue(error.getMessage().contains("ADMINISTRADOR"), error.getMessage());
    }

    // ---------------- AGRONOMO ----------------

    @Test
    @DisplayName("el AGRONOMO puede emitir reportes")
    void agronomoReporta() {
        proxy.iniciarJornada("ingeniero", "agro");

        proxy.emitirReporteEstado("csv");
        proxy.emitirReporteAlertas("consola");

        assertEquals(2, ((SistemaEspia) espia).getLlamadasRecibidas());
    }

    @Test
    @DisplayName("el AGRONOMO tampoco puede cerrar la jornada")
    void agronomoNoCierra() {
        proxy.iniciarJornada("ingeniero", "agro");

        assertThrows(SecurityException.class, () -> proxy.cerrarJornada());
    }

    // ---------------- ADMINISTRADOR ----------------

    @Test
    @DisplayName("el ADMINISTRADOR puede todas las operaciones")
    void administradorPuedeTodo() {
        proxy.iniciarJornada("admin", "1234");

        proxy.monitorearFinca();
        proxy.consultarClima();
        proxy.emitirReporteEstado("consola");
        proxy.emitirReporteAlertas("consola");
        proxy.cerrarJornada();

        assertEquals(5, ((SistemaEspia) espia).getLlamadasRecibidas());
    }

    // ---------------- el patrón en sí ----------------

    @Test
    @DisplayName("el proxy lleva la cuenta de los accesos denegados")
    void auditaLosBloqueos() {
        proxy.iniciarJornada("operario", "riego");

        assertThrows(SecurityException.class, () -> proxy.emitirReporteEstado("consola"));
        assertThrows(SecurityException.class, () -> proxy.cerrarJornada());

        assertEquals(2, ((ProxyAccesoSistema) proxy).getAccesosDenegados());
    }

    @Test
    @DisplayName("proxy y fachada real son intercambiables para el cliente")
    void proxyYFachadaSonIntercambiables() {
        // El mismo tipo estático sirve para los dos: esa es la clave del patrón.
        SistemaAgroPrecision comoProxy   = proxy;
        SistemaAgroPrecision comoFachada = new FachadaAgroPrecision(
                new AdaptadorClimaExterno(new ServicioClimaInestable(0), "EST-NORTE"));

        assertTrue(comoProxy.iniciarJornada("admin", "1234"));
        SesionUsuario.getInstancia().cerrarSesion();
        assertTrue(comoFachada.iniciarJornada("admin", "1234"));
    }

    @Test
    @DisplayName("el proxy exige un sistema al que representar")
    void noSePuedeRepresentarNada() {
        assertThrows(IllegalArgumentException.class, () -> new ProxyAccesoSistema(null));
    }

    @Test
    @DisplayName("una credencial inválida deja la sesión cerrada y el proxy sigue bloqueando")
    void credencialInvalida() {
        assertFalse(proxy.iniciarJornada("admin", "clave-mala"));

        assertThrows(SecurityException.class, () -> proxy.monitorearFinca());
    }
}
