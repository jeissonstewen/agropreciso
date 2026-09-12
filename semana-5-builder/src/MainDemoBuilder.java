import java.util.Arrays;
import java.util.List;

/**
 * Demostración del patrón BUILDER en el módulo de reportes.
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoBuilder
 */
public class MainDemoBuilder {

    public static void main(String[] args) {

        // Contexto: sesión activa (Semana 3) y lecturas de los sensores (Semana 4).
        SesionUsuario.getInstancia().iniciarSesion("admin", "1234");
        List<Lectura> lecturas = Arrays.asList(
                new CreadorHumedad().tomarLectura("S-01"),
                new CreadorTemperatura().tomarLectura("S-02"),
                new CreadorPH().tomarLectura("S-03"));
        List<String> inventario = Arrays.asList(
                "Fertilizante NPK: 12 bultos",
                "Semilla de maiz: 40 kg",
                "Cafe en cadena de frio: 300 kg a 6.0 C");
        System.out.println();

        System.out.println("=== PRUEBA 1: reporte minimo, solo el titulo ===");
        ReporteBuilder builder = new ReporteConsolaBuilder();
        builder.conTitulo("Reporte vacio").construir().imprimir();

        System.out.println("=== PRUEBA 2: el cliente arma el reporte paso a paso ===");
        // Cada linea agrega una parte; el orden lo decide el cliente.
        ReporteFinca personalizado = builder
                .conTitulo("Reporte de humedad")
                .conEncabezado("Finca La Esperanza", "operario1")
                .conLecturas(lecturas.subList(0, 1))
                .conResumen()
                .construir();
        personalizado.imprimir();

        System.out.println("=== PRUEBA 3: el Director aplica las recetas ===");
        DirectorReportes director = new DirectorReportes(builder);
        director.reporteOperario(lecturas).imprimir();
        director.reporteGerencial(lecturas, inventario).imprimir();

        System.out.println("=== PRUEBA 4: las alertas salen de la configuracion global ===");
        ConfiguracionSistema.getInstancia().setUmbralHumedadMin(95.0); // cualquier humedad queda por debajo
        ReporteFinca conAlerta = director.reporteOperario(lecturas);
        conAlerta.imprimir();
        System.out.println("Alertas generadas: " + conAlerta.getAlertas().size());
        System.out.println();

        System.out.println("=== PRUEBA 5: un mismo builder sirve para varios reportes ===");
        ReporteFinca a = builder.conTitulo("Reporte A").conInventario(inventario).construir();
        ReporteFinca b = builder.conTitulo("Reporte B").construir();
        System.out.println("Reporte A -> secciones: " + a.contarSecciones()
                + ", items de inventario: " + a.getInventario().size());
        System.out.println("Reporte B -> secciones: " + b.contarSecciones()
                + ", items de inventario: " + b.getInventario().size()
                + "  (no heredo nada del A)");
        System.out.println();

        System.out.println("=== PRUEBA 6: no se puede construir un reporte sin titulo ===");
        try {
            builder.conLecturas(lecturas).construir();
            System.out.println("ERROR: se construyo un reporte sin titulo");
        } catch (IllegalStateException e) {
            System.out.println("Excepcion esperada: " + e.getMessage());
        }
    }
}
