/**
 * Demostración de los patrones DECORATOR (aplicado ahora al servicio de
 * clima) y FACADE (la cara simple de todo el subsistema).
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoDecoratorFacade
 */
public class MainDemoDecoratorFacade {

    public static void main(String[] args) {

        // ------------------------------------------------------------
        //  DECORATOR sobre el servicio de clima
        // ------------------------------------------------------------
        System.out.println("=== PRUEBA 1: sin cache, cada consulta llama al servicio ===");
        ServicioClimaInestable servicio = new ServicioClimaInestable(0);
        ProveedorClima sinCache = new AdaptadorClimaExterno(servicio, "EST-NORTE");
        for (int i = 0; i < 2; i++) {
            sinCache.obtenerTemperatura();
            sinCache.obtenerHumedad();
        }
        System.out.println("  Llamadas reales al servicio externo: " + servicio.getLlamadas());
        System.out.println();

        System.out.println("=== PRUEBA 2: con cache, una sola llamada por magnitud ===");
        ServicioClimaInestable servicio2 = new ServicioClimaInestable(0);
        ClimaConCache conCache = new ClimaConCache(
                new AdaptadorClimaExterno(servicio2, "EST-NORTE"));
        for (int i = 0; i < 2; i++) {
            conCache.obtenerTemperatura();
            conCache.obtenerHumedad();
        }
        System.out.println("  Llamadas reales al servicio externo: " + servicio2.getLlamadas());
        System.out.println("  Consultas servidas desde el cache  : " + conCache.getAciertos());
        System.out.println("  Valor devuelto                     : " + conCache.obtenerTemperatura());
        System.out.println();

        System.out.println("=== PRUEBA 3: el reintento absorbe una caida pasajera ===");
        // El servicio falla las dos primeras llamadas y responde a la tercera.
        ServicioClimaInestable inestable = new ServicioClimaInestable(2);
        ClimaConReintento conReintento = new ClimaConReintento(
                new AdaptadorClimaExterno(inestable, "EST-NORTE"), 3);
        System.out.println("  Resultado: " + conReintento.obtenerTemperatura());
        System.out.println("  Intentos fallidos absorbidos: " + conReintento.getReintentos());
        System.out.println();

        System.out.println("=== PRUEBA 4: si se agotan los intentos, el error se propaga ===");
        ClimaConReintento sinSuerte = new ClimaConReintento(
                new AdaptadorClimaExterno(new ServicioClimaInestable(99), "EST-NORTE"), 2);
        try {
            sinSuerte.obtenerTemperatura();
        } catch (IllegalStateException e) {
            System.out.println("  Excepcion esperada: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== PRUEBA 5: los dos decoradores apilados ===");
        ServicioClimaInestable servicio3 = new ServicioClimaInestable(1);
        ClimaConCache climaRobusto = new ClimaConCache(
                new ClimaConReintento(
                        new AdaptadorClimaExterno(servicio3, "EST-NORTE"), 3));
        climaRobusto.obtenerTemperatura();
        climaRobusto.obtenerTemperatura();
        System.out.println("  Llamadas reales: " + servicio3.getLlamadas()
                + " (1 fallida + 1 buena; la segunda consulta salio del cache)");
        System.out.println("  Cadena: " + climaRobusto.getDescripcion());
        System.out.println();

        // ------------------------------------------------------------
        //  FACADE
        // ------------------------------------------------------------
        System.out.println("=== PRUEBA 6: la jornada completa con cuatro llamadas ===");
        // La fachada recibe el clima YA decorado: no sabe ni le importa que
        // lleve cache y reintento detras.
        FachadaAgroPrecision sistema = new FachadaAgroPrecision(climaRobusto);

        sistema.iniciarJornada("admin", "1234");
        sistema.consultarClima();
        sistema.monitorearFinca();
        System.out.println();   // el reporte vuelve a leer los sensores: de ahi el [LOG]
        sistema.emitirReporteEstado("consola");
        System.out.println();

        System.out.println("=== PRUEBA 7: cambiar de formato es un parametro ===");
        sistema.emitirReporteEstado("csv");
        System.out.println();
        sistema.emitirReporteAlertas("markdown");
        System.out.println();

        System.out.println("=== PRUEBA 8: la fachada no encierra al subsistema ===");
        // Quien necesite mas que lo que ofrece la fachada, entra directo.
        ZonaCultivo finca = sistema.getFinca();
        System.out.printf("  Area total desde el Composite: %.1f ha, %d sensores%n",
                finca.getAreaHectareas(), finca.contarSensores());
        System.out.print(finca.describir(0));
        System.out.println();

        System.out.println("=== PRUEBA 9: sin jornada iniciada no se opera ===");
        sistema.cerrarJornada();
        try {
            sistema.monitorearFinca();
        } catch (IllegalStateException e) {
            System.out.println("  Excepcion esperada: " + e.getMessage());
        }
    }
}
