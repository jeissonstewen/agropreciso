/**
 * Demostración de los patrones COMPOSITE (estructura de la finca) y
 * DECORATOR (sensores).
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoCompositeDecorator
 */
public class MainDemoCompositeDecorator {

    public static void main(String[] args) {

        // ------------------------------------------------------------
        //  COMPOSITE
        // ------------------------------------------------------------
        Lote cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .agregarSensor(new SensorFijo("CN-T", "TEMPERATURA", 22.0, "C"))
                .setActuador(new RiegoGoteo());

        Lote cafeSur = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CS-H", "HUMEDAD", 78.0, "%"))
                .setActuador(new RiegoGoteo());

        Lote maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 31.0, "%"))
                .agregarSensor(new SensorFijo("ME-T", "TEMPERATURA", 37.0, "C"))
                .setActuador(new RiegoAspersor());

        ZonaCultivo invernadero = new ZonaCultivo("Invernadero")
                .agregar(cafeNorte)
                .agregar(cafeSur);
        ZonaCultivo campoAbierto = new ZonaCultivo("Campo abierto")
                .agregar(maizEste);
        ZonaCultivo finca = new ZonaCultivo("Finca La Esperanza")
                .agregar(invernadero)
                .agregar(campoAbierto);

        System.out.println("=== PRUEBA 1: el arbol completo de la finca ===");
        System.out.print(finca.describir(0));
        System.out.println();

        System.out.println("=== PRUEBA 2: operaciones agregadas recursivas ===");
        System.out.printf("  Area de la finca      : %.1f ha%n", finca.getAreaHectareas());
        System.out.printf("  Area del invernadero  : %.1f ha  (1.5 + 2.0)%n",
                invernadero.getAreaHectareas());
        System.out.println("  Sensores en la finca  : " + finca.contarSensores());
        System.out.println("  Sensores en Cafe-Norte: " + cafeNorte.contarSensores());
        System.out.println();

        System.out.println("=== PRUEBA 3: el cliente trata igual la hoja y el compuesto ===");
        // El mismo metodo recibe un lote suelto y la finca entera.
        informar(cafeNorte);
        informar(invernadero);
        informar(finca);
        System.out.println();

        System.out.println("=== PRUEBA 4: agregar y quitar elementos del arbol ===");
        Lote lotePrueba = new Lote("Papa-Oeste", "papa", 3.0, new UmbralesLote(50.0, 20.0));
        campoAbierto.agregar(lotePrueba);
        System.out.printf("  Tras agregar Papa-Oeste : %.1f ha, %s en campo abierto%n",
                finca.getAreaHectareas(),
                ZonaCultivo.contar(campoAbierto.getHijos().size(), "elemento", "elementos"));
        campoAbierto.quitar(lotePrueba);
        System.out.printf("  Tras quitarlo           : %.1f ha, %s%n",
                finca.getAreaHectareas(),
                ZonaCultivo.contar(campoAbierto.getHijos().size(), "elemento", "elementos"));
        System.out.println();

        // ------------------------------------------------------------
        //  DECORATOR
        // ------------------------------------------------------------
        System.out.println("=== PRUEBA 5: un sensor sin decorar y decorado ===");
        Sensor crudo = new SensorFijo("CN-H", "HUMEDAD", 64.0, "%");
        System.out.println("  Sin decorar            : " + crudo.leer());
        Sensor calibrado = new SensorConCalibracion(crudo, -1.5);
        System.out.println("  Con calibracion(-1.5)  : " + calibrado.leer());
        System.out.println();

        System.out.println("=== PRUEBA 6: decoradores apilados ===");
        // El sensor averiado devuelve -40 %, un valor fisicamente imposible.
        Sensor averiado = new SensorFijo("CS-H", "HUMEDAD", -40.0, "%");
        SensorConValidacion validado = new SensorConValidacion(averiado, 0.0, 100.0);
        SensorConRegistro vigilado = new SensorConRegistro(new SensorConCalibracion(validado, 2.0));
        Lectura resultado = vigilado.leer();
        System.out.println("  Valor crudo            : -40.0 %");
        System.out.println("  Tras validar y calibrar: " + resultado.getValor() + " %");
        System.out.println("  Correcciones aplicadas : " + validado.getCorrecciones());
        System.out.println("  Cadena                 : " + vigilado.getDescripcion());
        System.out.println();

        System.out.println("=== PRUEBA 7: los dos patrones juntos ===");
        // Se decora un sensor YA instalado en una hoja del arbol: ni la zona
        // ni el lote se enteran, porque un decorador sigue siendo un Sensor.
        Lote cafeVigilado = new Lote("Cafe-Vigilado", "cafe", 1.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorConRegistro(
                        new SensorConCalibracion(
                                new SensorFijo("CV-H", "HUMEDAD", 66.0, "%"), -1.5)))
                .setActuador(new RiegoGoteo());
        invernadero.agregar(cafeVigilado);
        finca.monitorear();
    }

    /**
     * Código cliente: no sabe si recibe un lote o una zona con cien lotes.
     * Ese es exactamente el punto del Composite.
     */
    private static void informar(ComponenteFinca componente) {
        System.out.printf("  %-20s -> %.1f ha, %s%n",
                componente.getNombre(), componente.getAreaHectareas(),
                ZonaCultivo.contar(componente.contarSensores(), "sensor", "sensores"));
    }
}
