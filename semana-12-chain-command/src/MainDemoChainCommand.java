/**
 * Demostración de los patrones CHAIN OF RESPONSIBILITY (atención de
 * incidencias) y COMMAND (órdenes de operación con bitácora y deshacer).
 *
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoChainCommand
 */
public class MainDemoChainCommand {

    public static void main(String[] args) {

        // --- la finca, armada con el Composite de la Semana 8 ---
        Lote cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote cafeSur = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(35.0, 26.0))
                .agregarSensor(new SensorFijo("CS-H", "HUMEDAD", 48.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", -12.0, "%"))
                .setActuador(new RiegoAspersor());
        ZonaCultivo finca = new ZonaCultivo("Finca La Esperanza")
                .agregar(cafeNorte).agregar(cafeSur).agregar(maizEste);

        // --- la cadena: operario -> agronomo -> administrador ---
        ManejadorIncidencia cadena = new AtencionOperario();
        cadena.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());

        PanelDeControl panel = new PanelDeControl();

        // ============================================================
        //  CHAIN OF RESPONSIBILITY
        // ============================================================
        System.out.println("=== PRUEBA 1: una incidencia LEVE la resuelve el primer eslabon ===");
        ResultadoAtencion r1 = cadena.manejar(
                new Incidencia(cafeNorte, TipoIncidencia.HUMEDAD_BAJA,
                        cafeNorte.getSensores().get(0).leer()));
        System.out.println("  Resultado: " + r1);
        System.out.println();

        System.out.println("=== PRUEBA 2: una MEDIA escala un eslabon ===");
        ResultadoAtencion r2 = cadena.manejar(
                new Incidencia(cafeSur, TipoIncidencia.UMBRAL_INADECUADO));
        System.out.println("  Resultado: " + r2);
        System.out.println();

        System.out.println("=== PRUEBA 3: una CRITICA recorre la cadena completa ===");
        ResultadoAtencion r3 = cadena.manejar(
                new Incidencia(maizEste, TipoIncidencia.SENSOR_AVERIADO,
                        maizEste.getSensores().get(0).leer()));
        System.out.println("  Resultado: " + r3);
        System.out.println();

        System.out.println("=== PRUEBA 4: una cadena incompleta deja la incidencia sin atender ===");
        // Solo el operario: nadie puede hacerse cargo de una critica.
        ManejadorIncidencia cadenaCorta = new AtencionOperario();
        ResultadoAtencion r4 = cadenaCorta.manejar(
                new Incidencia(maizEste, TipoIncidencia.SENSOR_AVERIADO));
        System.out.println("  Resultado: " + r4);
        System.out.println();

        System.out.println("=== PRUEBA 5: reordenar la cadena cambia quien atiende ===");
        // El administrador primero: como solo acepta criticas, sigue escalando.
        ManejadorIncidencia alReves = new AtencionAdministrador();
        alReves.encadenar(new AtencionAgronomo()).encadenar(new AtencionOperario());
        ResultadoAtencion r5 = alReves.manejar(
                new Incidencia(cafeNorte, TipoIncidencia.HUMEDAD_BAJA));
        System.out.println("  Resultado: " + r5);
        System.out.println("  Ningun manejador cambio: solo cambio el orden de los enlaces.");
        System.out.println();

        // ============================================================
        //  COMMAND
        // ============================================================
        System.out.println("=== PRUEBA 6: el panel ejecuta las ordenes que produjo la cadena ===");
        panel.ejecutar(r1.getOrden());   // regar Cafe-Norte
        panel.ejecutar(r2.getOrden());   // ajustar umbral de Cafe-Sur
        panel.ejecutar(r3.getOrden());   // instalar sensor en Maiz-Este
        System.out.println("  Ordenes ejecutadas: " + panel.cuantasEjecutadas());
        System.out.println();

        System.out.println("=== PRUEBA 7: deshacer la ultima orden reversible ===");
        System.out.println("  Sensores en Maiz-Este antes: " + maizEste.contarSensores());
        panel.deshacerUltima();
        System.out.println("  Sensores en Maiz-Este despues: " + maizEste.contarSensores());
        System.out.println();

        System.out.println("=== PRUEBA 8: una orden definitiva no se deshace ===");
        System.out.printf("  Umbral de Cafe-Sur antes: %.1f %%%n",
                cafeSur.getUmbrales().getHumedadMin());
        panel.deshacerUltima();          // deshace el cambio de umbral
        System.out.printf("  Umbral de Cafe-Sur despues: %.1f %%%n",
                cafeSur.getUmbrales().getHumedadMin());
        panel.deshacerUltima();          // la siguiente es el riego: no se puede
        System.out.println("  El riego sigue en la bitacora: la operacion ocurrio de verdad.");
        System.out.println();

        System.out.println("=== PRUEBA 9: ordenes programadas para la noche ===");
        panel.programar(new OrdenRegar(cafeNorte, 20));
        panel.programar(new OrdenRegar(maizEste, 30));
        System.out.println("  Pendientes: " + panel.cuantasPendientes());
        int ejecutadas = panel.ejecutarPendientes();
        System.out.println("  Ejecutadas en orden de llegada: " + ejecutadas);
        System.out.println();

        System.out.println("=== PRUEBA 10: la bitacora completa ===");
        panel.imprimirHistorial();
        System.out.println();
        System.out.print(finca.describir(0));
    }
}
