/**
 * Demostración de los patrones OBSERVER (alertas), STATE (ciclo de vida
 * del lote) y VISITOR (operaciones sobre la finca).
 *
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoObserverStateVisitor
 */
public class MainDemoObserverStateVisitor {

    public static void main(String[] args) {

        // --- la finca, con el Composite de la Semana 8 ---
        Lote cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote cafeSur = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CS-H", "HUMEDAD", 45.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 52.0, "%"))
                .setActuador(new RiegoAspersor());
        ZonaCultivo finca = new ZonaCultivo("Finca La Esperanza")
                .agregar(new ZonaCultivo("Invernadero").agregar(cafeNorte).agregar(cafeSur))
                .agregar(new ZonaCultivo("Campo abierto").agregar(maizEste));

        // ============================================================
        //  STATE
        // ============================================================
        System.out.println("=== PRUEBA 1: todo lote nace en preparacion y no se riega ===");
        System.out.println("  Estado de Cafe-Norte : " + cafeNorte.getEstado().getNombre());
        System.out.println("  Admite riego         : " + cafeNorte.admiteRiego());
        cafeNorte.monitorear();
        System.out.println();

        System.out.println("=== PRUEBA 2: una transicion valida cambia el comportamiento ===");
        cafeNorte.sembrar();
        System.out.println("  Tras sembrar         : " + cafeNorte.getEstado().getNombre());
        System.out.println("  Admite riego         : " + cafeNorte.admiteRiego());
        cafeNorte.monitorear();
        System.out.println();

        System.out.println("=== PRUEBA 3: una transicion invalida se rechaza ===");
        try {
            cafeNorte.cosechar();           // esta sembrado, todavia no madura
        } catch (IllegalStateException e) {
            System.out.println("  " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== PRUEBA 4: el ciclo completo ===");
        cafeNorte.madurar();
        System.out.println("  madurar()       -> " + cafeNorte.getEstado().getNombre());
        cafeNorte.cosechar();
        System.out.println("  cosechar()      -> " + cafeNorte.getEstado().getNombre()
                + "  (admite riego: " + cafeNorte.admiteRiego() + ")");
        cafeNorte.prepararSuelo();
        System.out.println("  prepararSuelo() -> " + cafeNorte.getEstado().getNombre());
        System.out.println("  El ciclo se cierra sin un solo if sobre el estado.");
        System.out.println();

        // ============================================================
        //  OBSERVER
        // ============================================================
        System.out.println("=== PRUEBA 5: tres suscriptores al mismo sujeto ===");
        MonitorDeAlertas monitor = new MonitorDeAlertas();
        PanelDeAlertas panel = new PanelDeAlertas();
        HistorialAlertas historial = new HistorialAlertas();
        ContadorPorSeveridad contador = new ContadorPorSeveridad();
        monitor.suscribir(panel);
        monitor.suscribir(historial);
        monitor.suscribir(contador);
        System.out.println("  Observadores suscritos: " + monitor.cuantosObservadores());

        // Se siembran los tres lotes para que admitan riego.
        cafeNorte.sembrar();
        cafeSur.sembrar();
        maizEste.sembrar();

        int publicadas = monitor.revisarFinca(finca);
        System.out.println("  Alertas publicadas    : " + publicadas);
        System.out.println("  Guardadas en historial: " + historial.cuantas());
        System.out.println("  Contadas por gravedad : " + contador.total());
        System.out.println();

        System.out.println("=== PRUEBA 6: desuscribir deja de notificar ===");
        monitor.quitar(panel);
        System.out.println("  Observadores suscritos: " + monitor.cuantosObservadores());
        System.out.println("  (el panel ya no imprime; historial y contador siguen)");
        monitor.revisarFinca(finca);
        System.out.println("  Historial acumulado   : " + historial.cuantas());
        System.out.println();

        System.out.println("=== PRUEBA 7: el sujeto funciona sin observadores ===");
        monitor.quitar(historial);
        monitor.quitar(contador);
        System.out.println("  Observadores suscritos: " + monitor.cuantosObservadores());
        int sinNadie = monitor.revisarFinca(finca);
        System.out.println("  Alertas detectadas    : " + sinNadie + " (nadie las escucho)");
        System.out.println();

        System.out.println("=== PRUEBA 8: un cambio de estado tambien se publica ===");
        monitor.suscribir(panel);
        String antes = cafeSur.getEstado().getNombre();
        cafeSur.madurar();
        monitor.publicarCambioDeEstado(cafeSur, antes);
        System.out.println();

        // ============================================================
        //  VISITOR
        // ============================================================
        System.out.println("=== PRUEBA 9: tres operaciones sobre el MISMO arbol ===");
        System.out.println("  --- Inventario ---");
        VisitanteInventario inventario = new VisitanteInventario();
        finca.aceptar(inventario);
        System.out.print(inventario.getResultado());

        System.out.println("  --- Costo de riego ---");
        VisitanteCostoRiego costo = new VisitanteCostoRiego();
        finca.aceptar(costo);
        System.out.print(costo.getResultado());
        System.out.println();

        System.out.println("=== PRUEBA 10: un visitante nuevo, cero cambios en el arbol ===");
        VisitanteExportarCsv csv = new VisitanteExportarCsv();
        finca.aceptar(csv);
        System.out.print(indentar(csv.getResultado()));
        System.out.println("  " + csv.cuantasFilas() + " filas exportadas.");
        System.out.println();

        System.out.println("=== PRUEBA 11: el estado manda sobre el costo ===");
        maizEste.madurar();
        maizEste.cosechar();            // queda en barbecho: ya no se riega
        VisitanteCostoRiego despues = new VisitanteCostoRiego();
        finca.aceptar(despues);
        System.out.println("  Tras cosechar Maiz-Este:");
        System.out.print(despues.getResultado());
        System.out.println();
        System.out.print(finca.describir(0));
    }

    /** Sangra el CSV para que no se confunda con la salida de la demo. */
    private static String indentar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (String linea : texto.split("\\R")) sb.append("    ").append(linea).append('\n');
        return sb.toString();
    }
}
