import java.util.Iterator;

/**
 * Demostración de los patrones ITERATOR (recorridos del árbol de la finca)
 * y MEDIATOR (centro de operaciones que coordina a los colegas).
 *
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoIteratorMediator
 */
public class MainDemoIteratorMediator {

    public static void main(String[] args) {

        ZonaCultivo finca = construirFinca();

        // ============================================================
        //  ITERATOR
        // ============================================================
        System.out.println("=== PRUEBA 1: la finca tiene tres niveles de profundidad ===");
        System.out.print(finca.describir(0));
        System.out.println();

        System.out.println("=== PRUEBA 2: recorrido EN PROFUNDIDAD ===");
        imprimirRecorrido(finca, TipoRecorrido.PROFUNDIDAD);
        System.out.println();

        System.out.println("=== PRUEBA 3: recorrido POR NIVELES sobre el MISMO arbol ===");
        imprimirRecorrido(finca, TipoRecorrido.NIVELES);
        System.out.println("  Mismo arbol, otro orden. ZonaCultivo y Lote no cambiaron.");
        System.out.println();

        System.out.println("=== PRUEBA 4: for-each, porque la finca es Iterable ===");
        double total = 0;
        for (Lote lote : finca) {                       // ← el patrón, en una línea
            total += lote.getAreaHectareas();
        }
        System.out.printf("  Area sumada recorriendo lote por lote: %.1f ha%n", total);
        System.out.printf("  Area que reporta el Composite        : %.1f ha%n",
                finca.getAreaHectareas());
        System.out.println();

        System.out.println("=== PRUEBA 5: dos recorridos simultaneos no se estorban ===");
        Iterator<Lote> a = finca.iterador(TipoRecorrido.PROFUNDIDAD);
        Iterator<Lote> b = finca.iterador(TipoRecorrido.NIVELES);
        for (int i = 1; i <= 3; i++) {
            System.out.printf("  paso %d -> profundidad: %-12s niveles: %s%n",
                    i, a.next().getNombre(), b.next().getNombre());
        }
        System.out.println("  Cada iterador lleva su propio estado.");
        System.out.println();

        // ============================================================
        //  MEDIATOR
        // ============================================================
        System.out.println("=== PRUEBA 6: la jornada coordinada por el centro ===");
        CentroDeOperaciones centro = new CentroDeOperaciones();

        ManejadorIncidencia cadena = new AtencionOperario();
        cadena.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());

        EstacionSensores estacion   = new EstacionSensores(centro);
        GestorIncidencias gestor    = new GestorIncidencias(centro, cadena);
        OperacionesCampo operaciones = new OperacionesCampo(centro, new PanelDeControl());
        Bitacora bitacora           = new Bitacora(centro);

        centro.registrarGestor(gestor);
        centro.registrarOperaciones(operaciones);
        centro.registrarBitacora(bitacora);

        int anomalas = estacion.inspeccionar(finca, TipoRecorrido.PROFUNDIDAD);
        System.out.println("  Lecturas anomalas encontradas: " + anomalas);
        System.out.println("  Avisos enrutados por el centro: " + centro.getAvisosRecibidos());
        System.out.println();

        System.out.println("=== PRUEBA 7: la bitacora se entero sin que nadie la llamara ===");
        System.out.println("  Entradas registradas: " + bitacora.cuantasEntradas());
        System.out.println("  Ordenes en el panel  : " + operaciones.getPanel().cuantasEjecutadas());
        System.out.println();

        System.out.println("=== PRUEBA 8: ningun colega conoce a otro ===");
        System.out.println("  EstacionSensores  -> solo el centro");
        System.out.println("  GestorIncidencias -> el centro y la cadena");
        System.out.println("  OperacionesCampo  -> el centro y el panel");
        System.out.println("  Bitacora          -> solo el centro");
        System.out.println("  Cuatro colegas, cuatro referencias. Sin mediador serian hasta doce.");
        System.out.println();

        System.out.println("=== PRUEBA 9: el centro funciona sin bitacora registrada ===");
        CentroDeOperaciones centroMinimo = new CentroDeOperaciones();
        ManejadorIncidencia cadena2 = new AtencionOperario();
        cadena2.encadenar(new AtencionAgronomo()).encadenar(new AtencionAdministrador());
        centroMinimo.registrarGestor(new GestorIncidencias(centroMinimo, cadena2));
        centroMinimo.registrarOperaciones(new OperacionesCampo(centroMinimo, new PanelDeControl()));
        // No se registra bitacora: quitar un colega no rompe a los demas.
        new EstacionSensores(centroMinimo).inspeccionar(finca, TipoRecorrido.NIVELES);
        System.out.println("  Funciono sin bitacora: el colega es opcional.");
    }

    /**
     * Finca de TRES niveles, a propósito: con un árbol plano los dos
     * recorridos darían el mismo orden y no se vería la diferencia.
     *
     *   Finca
     *   ├── Invernadero
     *   │   ├── Sector A
     *   │   │   ├── Cafe-Norte
     *   │   │   └── Cafe-Sur
     *   │   └── Cafe-Alto
     *   └── Campo abierto
     *       └── Maiz-Este
     */
    private static ZonaCultivo construirFinca() {
        Lote cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote cafeSur = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CS-H", "HUMEDAD", 78.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote cafeAlto = new Lote("Cafe-Alto", "cafe", 1.2, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CA-H", "HUMEDAD", 61.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 52.0, "%"))
                .setActuador(new RiegoAspersor());

        ZonaCultivo sectorA = new ZonaCultivo("Sector A")
                .agregar(cafeNorte).agregar(cafeSur);
        ZonaCultivo invernadero = new ZonaCultivo("Invernadero")
                .agregar(sectorA).agregar(cafeAlto);
        ZonaCultivo campoAbierto = new ZonaCultivo("Campo abierto")
                .agregar(maizEste);

        return new ZonaCultivo("Finca La Esperanza")
                .agregar(invernadero).agregar(campoAbierto);
    }

    private static void imprimirRecorrido(ZonaCultivo finca, TipoRecorrido tipo) {
        StringBuilder sb = new StringBuilder("  ");
        Iterator<Lote> lotes = finca.iterador(tipo);
        while (lotes.hasNext()) {
            sb.append(lotes.next().getNombre());
            if (lotes.hasNext()) sb.append("  ->  ");
        }
        System.out.println(sb);
    }
}
