import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón BRIDGE.
 *
 * Verifican que la misma abstracción (Reporte) produzca resultados
 * distintos según el implementador (SalidaReporte) y que las dos
 * jerarquías puedan crecer por separado.
 */
@DisplayName("Bridge: reportes sobre distintos formatos de salida")
class ReporteBridgeTest {

    private final PrintStream salidaOriginal = System.out;
    private List<Lectura> lecturas;

    /**
     * generar() termina llamando a cerrar(), que imprime. Se redirige la
     * salida estándar para que el informe de pruebas quede limpio; las
     * comprobaciones se hacen sobre getContenido().
     */
    @BeforeEach
    void prepararDatosYSilenciarSalida() {
        lecturas = Arrays.asList(
                new SensorFijo("S-01", "TEMPERATURA", 24.5, "C").leer(),
                new SensorFijo("S-02", "HUMEDAD", 31.0, "%").leer());
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("el reporte de estado en CSV separa las celdas por comas")
    void reporteEstadoEnCsv() {
        SalidaCsv csv = new SalidaCsv();

        new ReporteEstado(csv, "Finca La Esperanza", lecturas).generar();

        String contenido = csv.getContenido();
        assertTrue(contenido.contains("sensor,tipo,valor,unidad"), contenido);
        assertTrue(contenido.contains("S-01,TEMPERATURA,24.5,C"), contenido);
        assertTrue(contenido.contains("S-02,HUMEDAD,31.0,%"), contenido);
    }

    @Test
    @DisplayName("el mismo reporte en Markdown produce una tabla con separador")
    void reporteEstadoEnMarkdown() {
        SalidaMarkdown markdown = new SalidaMarkdown();

        new ReporteEstado(markdown, "Finca La Esperanza", lecturas).generar();

        String contenido = markdown.getContenido();
        assertTrue(contenido.startsWith("## Estado de Finca La Esperanza"), contenido);
        assertTrue(contenido.contains("| sensor | tipo | valor | unidad |"), contenido);
        assertTrue(contenido.contains("| --- | --- | --- | --- |"), contenido);
    }

    @Test
    @DisplayName("el CSV entrecomilla los valores que contienen comas")
    void csvEscapaLasComas() {
        SalidaCsv csv = new SalidaCsv();

        new ReporteAlertas(csv, Arrays.asList("Lote Cafe-Norte, pH fuera de rango")).generar();

        assertTrue(csv.getContenido().contains("\"Lote Cafe-Norte, pH fuera de rango\""),
                csv.getContenido());
    }

    @Test
    @DisplayName("el reporte de alertas sin alertas lo dice explícitamente")
    void reporteAlertasVacio() {
        SalidaConsola consola = new SalidaConsola();

        new ReporteAlertas(consola, Collections.emptyList()).generar();

        assertTrue(consola.getContenido().contains("Sin alertas"), consola.getContenido());
    }

    @Test
    @DisplayName("una misma abstracción sobre dos implementadores da salidas distintas")
    void mismaAbstraccionDistintoFormato() {
        SalidaConsola consola = new SalidaConsola();
        SalidaCsv csv = new SalidaCsv();

        new ReporteEstado(consola, "Finca La Esperanza", lecturas).generar();
        new ReporteEstado(csv, "Finca La Esperanza", lecturas).generar();

        assertFalse(consola.getContenido().equals(csv.getContenido()),
                "los dos formatos no deberian producir el mismo texto");
        // Pero ambos escriben los mismos datos: el contenido lo decide la abstracción.
        assertTrue(consola.getContenido().contains("S-01"));
        assertTrue(csv.getContenido().contains("S-01"));
    }

    @Test
    @DisplayName("describir() informa la combinación de abstracción e implementador")
    void describeLaCombinacion() {
        Reporte reporte = new ReporteAlertas(new SalidaMarkdown(), Collections.emptyList());

        assertEquals("Reporte de alertas en formato MARKDOWN", reporte.describir());
    }

    @Test
    @DisplayName("se puede agregar un formato nuevo sin tocar la jerarquía de Reporte")
    void admiteUnImplementadorNuevo() {
        // Implementador definido aquí mismo, que el código de produccion no conoce.
        SalidaEnMemoria enMemoria = new SalidaEnMemoria();

        new ReporteEstado(enMemoria, "Finca La Esperanza", lecturas).generar();

        assertEquals("PRUEBA", enMemoria.getFormato());
        assertEquals(4, enMemoria.getEventos().size(),
                "titulo + linea + tabla + cierre: " + enMemoria.getEventos());
    }

    /** IMPLEMENTADOR CONCRETO creado solo para esta prueba. */
    private static class SalidaEnMemoria implements SalidaReporte {

        private final List<String> eventos = new ArrayList<>();

        @Override public void escribirTitulo(String titulo) { eventos.add("titulo:" + titulo); }
        @Override public void escribirLinea(String texto)   { eventos.add("linea:" + texto); }

        @Override
        public void escribirTabla(List<String> encabezados, List<List<String>> filas) {
            eventos.add("tabla:" + encabezados.size() + "x" + filas.size());
        }

        @Override public void cerrar()          { eventos.add("cierre"); }
        @Override public String getFormato()    { return "PRUEBA"; }

        List<String> getEventos() { return eventos; }
    }
}
