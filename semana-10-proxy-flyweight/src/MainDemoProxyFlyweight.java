/**
 * Demostración del patrón PROXY de protección sobre el sistema
 * AgroPrecisión.
 *
 * El patrón FLYWEIGHT no se implementa esta semana: la decisión está
 * argumentada en el README y medida en medicion/BancoMemoriaFlyweight.java.
 *
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoProxyFlyweight
 */
public class MainDemoProxyFlyweight {

    public static void main(String[] args) {

        // El subsistema completo detras de la fachada de la Semana 9.
        ProveedorClima clima = new ClimaConCache(
                new AdaptadorClimaExterno(new ServicioClimaInestable(0), "EST-NORTE"));
        FachadaAgroPrecision fachadaReal = new FachadaAgroPrecision(clima);

        // El cliente recibe el PROXY, no la fachada. Misma interfaz.
        SistemaAgroPrecision sistema = new ProxyAccesoSistema(fachadaReal);

        System.out.println("=== PRUEBA 1: sin jornada iniciada el proxy no deja pasar nada ===");
        intentar(() -> sistema.monitorearFinca(), "monitorear sin sesion");
        System.out.println();

        System.out.println("=== PRUEBA 2: el OPERARIO puede monitorear y consultar el clima ===");
        sistema.iniciarJornada("operario", "riego");
        sistema.consultarClima();
        System.out.println();

        System.out.println("=== PRUEBA 3: pero el OPERARIO no puede emitir reportes ===");
        intentar(() -> sistema.emitirReporteEstado("consola"), "reporte como operario");
        System.out.println();

        System.out.println("=== PRUEBA 4: ni cerrar la jornada, que es cosa del administrador ===");
        intentar(() -> sistema.cerrarJornada(), "cerrar como operario");
        System.out.println();

        System.out.println("=== PRUEBA 5: el AGRONOMO si puede emitir reportes ===");
        SesionUsuario.getInstancia().cerrarSesion();
        sistema.iniciarJornada("ingeniero", "agro");
        sistema.emitirReporteAlertas("consola");
        System.out.println();

        System.out.println("=== PRUEBA 6: el ADMINISTRADOR puede todo ===");
        SesionUsuario.getInstancia().cerrarSesion();
        sistema.iniciarJornada("admin", "1234");
        sistema.monitorearFinca();
        sistema.emitirReporteEstado("csv");
        System.out.println();

        System.out.println("=== PRUEBA 7: el proxy es transparente para el cliente ===");
        // El mismo metodo recibe la fachada real o el proxy: misma interfaz.
        System.out.println("  Con la fachada real:");
        usarElSistema(fachadaReal);
        System.out.println("  Con el proxy (mismo codigo cliente):");
        usarElSistema(sistema);
        System.out.println();

        System.out.println("=== PRUEBA 8: auditoria de los accesos bloqueados ===");
        System.out.println("  Llamadas denegadas por el proxy: "
                + ((ProxyAccesoSistema) sistema).getAccesosDenegados());
        System.out.println();

        System.out.println("=== PRUEBA 9: el proxy cierra la puerta que la fachada dejo abierta ===");
        System.out.println("  SistemaAgroPrecision NO declara getFinca(), asi que un cliente");
        System.out.println("  que sostiene la interfaz no puede alcanzar el subsistema:");
        System.out.println("  sistema.getFinca();  // no compila");
        System.out.println("  Solo quien tenga la clase concreta de la fachada entra directo:");
        System.out.printf("  fachadaReal.getFinca() -> %.1f ha, %d sensores%n",
                fachadaReal.getFinca().getAreaHectareas(),
                fachadaReal.getFinca().contarSensores());

        sistema.cerrarJornada();
    }

    /** Código cliente: solo conoce la interfaz, no sabe si hay proxy. */
    private static void usarElSistema(SistemaAgroPrecision s) {
        s.consultarClima();
    }

    /** Ejecuta una operación esperando que el proxy la rechace. */
    private static void intentar(Runnable operacion, String descripcion) {
        try {
            operacion.run();
            System.out.println("  ERROR: " + descripcion + " no deberia haber pasado");
        } catch (SecurityException e) {
            System.out.println("  Acceso denegado: " + e.getMessage());
        }
    }
}
