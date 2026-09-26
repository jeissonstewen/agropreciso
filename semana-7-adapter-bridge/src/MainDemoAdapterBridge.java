import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Demostración de los patrones ADAPTER (servicio de clima externo) y
 * BRIDGE (reportes: qué se reporta x cómo se escribe).
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoAdapterBridge
 */
public class MainDemoAdapterBridge {

    public static void main(String[] args) {

        // ------------------------------------------------------------
        //  ADAPTER
        // ------------------------------------------------------------
        ServicioClimaExterno servicio = new ServicioClimaExterno();

        System.out.println("=== PRUEBA 1: la respuesta cruda del servicio externo ===");
        System.out.println("  " + servicio.fetchWeather("EST-NORTE"));
        System.out.println("  Formato propio, temperatura en F y humedad como fraccion 0-1.");
        System.out.println();

        System.out.println("=== PRUEBA 2: el adaptador traduce a nuestra interfaz ===");
        ProveedorClima externo = new AdaptadorClimaExterno(servicio, "EST-NORTE");
        System.out.println("  " + externo.obtenerTemperatura());
        System.out.println("  " + externo.obtenerHumedad());
        System.out.println("  86.5 F -> 30.3 C  |  0.42 -> 42.0 %");
        System.out.println();

        System.out.println("=== PRUEBA 3: el cliente trata igual las dos fuentes ===");
        ProveedorClima propio = new ProveedorSensoresPropios(
                new SensorFijo("S-01", "TEMPERATURA", 24.0, "C"),
                new SensorFijo("S-02", "HUMEDAD", 31.0, "%"));
        new MonitorClima(propio, 35.0).informar();
        new MonitorClima(externo, 35.0).informar();
        System.out.println();

        System.out.println("=== PRUEBA 4: una respuesta sin el campo esperado ===");
        try {
            new AdaptadorClimaExterno(servicio, "EST-FANTASMA").obtenerTemperatura();
        } catch (IllegalStateException e) {
            System.out.println("  Excepcion esperada: " + e.getMessage());
        }
        System.out.println();

        // ------------------------------------------------------------
        //  BRIDGE
        // ------------------------------------------------------------
        List<Lectura> lecturas = Arrays.asList(
                externo.obtenerTemperatura(),
                externo.obtenerHumedad(),
                new SensorFijo("S-03", "PH", 6.4, "pH").leer());
        List<String> alertas = Arrays.asList(
                "Humedad baja en S-02: 31.0 %",
                "Lote Cafe-Norte, pH fuera de rango");

        System.out.println("=== PRUEBA 5: el mismo reporte en tres formatos ===");
        new ReporteEstado(new SalidaConsola(),  "Finca La Esperanza", lecturas).generar();
        new ReporteEstado(new SalidaMarkdown(), "Finca La Esperanza", lecturas).generar();
        new ReporteEstado(new SalidaCsv(),      "Finca La Esperanza", lecturas).generar();
        System.out.println();

        System.out.println("=== PRUEBA 6: otro reporte sobre los mismos formatos ===");
        new ReporteAlertas(new SalidaConsola(), alertas).generar();
        new ReporteAlertas(new SalidaCsv(),     alertas).generar();
        new ReporteAlertas(new SalidaMarkdown(), Collections.emptyList()).generar();
        System.out.println();

        System.out.println("=== PRUEBA 7: las seis combinaciones con 2 + 3 clases ===");
        // Dos abstracciones y tres implementadores: 6 combinaciones posibles.
        Reporte[] abstracciones = {
                new ReporteEstado(new SalidaConsola(), "Finca La Esperanza", lecturas),
                new ReporteAlertas(new SalidaMarkdown(), alertas) };
        for (Reporte r : abstracciones) {
            System.out.println("  " + r.describir());
        }
        System.out.println("  2 tipos de reporte x 3 formatos = 6 combinaciones, con 5 clases.");
    }
}
