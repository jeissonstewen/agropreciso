/**
 * Demostración de los patrones ABSTRACT FACTORY (equipamiento por zona) y
 * PROTOTYPE (plantillas de lote).
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoAbstractFactoryPrototype
 */
public class MainDemoAbstractFactoryPrototype {

    public static void main(String[] args) {

        // ------------------------------------------------------------
        //  ABSTRACT FACTORY
        // ------------------------------------------------------------
        System.out.println("=== PRUEBA 1: familia de INVERNADERO ===");
        EstacionMonitoreo invernadero = new EstacionMonitoreo(new FabricaInvernadero(), "INV");
        invernadero.tomarLecturas();
        System.out.println("  Actuador: " + invernadero.getActuador().getTipo());
        System.out.println();

        System.out.println("=== PRUEBA 2: familia de CAMPO ABIERTO ===");
        EstacionMonitoreo campo = new EstacionMonitoreo(new FabricaCampoAbierto(), "CAM");
        campo.tomarLecturas();
        System.out.println("  Actuador: " + campo.getActuador().getTipo());
        System.out.println();

        System.out.println("=== PRUEBA 3: el cliente no cambia al cambiar de familia ===");
        // El mismo metodo recibe las dos fabricas por la misma interfaz.
        FabricaEquipamiento[] fabricas = { new FabricaInvernadero(), new FabricaCampoAbierto() };
        for (FabricaEquipamiento f : fabricas) {
            instalarYRegar(f);
        }
        System.out.println();

        // ------------------------------------------------------------
        //  PROTOTYPE
        // ------------------------------------------------------------
        System.out.println("=== PRUEBA 4: varios lotes a partir de una plantilla ===");
        // Se arma UNA plantilla de cafe en invernadero: umbrales propios y equipamiento.
        Lote plantillaCafe = new Lote("PLANTILLA", "cafe", 0.0, new UmbralesLote(70.0, 26.0));
        plantillaCafe.equipar(new FabricaInvernadero());

        Lote plantillaMaiz = new Lote("PLANTILLA", "maiz", 0.0, new UmbralesLote(40.0, 35.0));
        plantillaMaiz.equipar(new FabricaCampoAbierto());

        CatalogoLotes catalogo = new CatalogoLotes();
        catalogo.registrar("cafe-invernadero", plantillaCafe);
        catalogo.registrar("maiz-campo", plantillaMaiz);

        Lote cafe1 = catalogo.crear("cafe-invernadero", "Cafe-Norte", 1.5);
        Lote cafe2 = catalogo.crear("cafe-invernadero", "Cafe-Sur", 2.0);
        Lote maiz1 = catalogo.crear("maiz-campo", "Maiz-Este", 6.0);
        System.out.println("  " + cafe1);
        System.out.println("  " + cafe2);
        System.out.println("  " + maiz1);
        System.out.println("  Plantillas disponibles: " + catalogo.claves());
        System.out.println("  cafe1 == cafe2 ? " + (cafe1 == cafe2) + "  (objetos distintos)");
        System.out.println();

        System.out.println("=== PRUEBA 5: copia profunda, el clon no afecta a la plantilla ===");
        cafe1.getUmbrales().setHumedadMin(55.0);        // se cambia el umbral del clon
        cafe1.agregarSensor(new SensorTemperaturaInvernadero("EXTRA-T")); // y se le agrega un sensor
        System.out.println("  Clon      -> " + cafe1);
        System.out.println("  Plantilla -> " + plantillaCafe);
        System.out.println("  Umbrales compartidos? "
                + (cafe1.getUmbrales() == plantillaCafe.getUmbrales()));
        System.out.println("  Lista de sensores compartida? "
                + (cafe1.getSensores() == plantillaCafe.getSensores()));
        System.out.println();

        System.out.println("=== PRUEBA 6: un clon opera con su propio umbral y su propio riego ===");
        cafe2.monitorear();
        maiz1.monitorear();
        System.out.println();

        System.out.println("=== PRUEBA 7: pedir una plantilla que no existe ===");
        try {
            catalogo.crear("papa-paramo", "Papa-1", 1.0);
        } catch (IllegalArgumentException e) {
            System.out.println("  Excepcion esperada: " + e.getMessage());
        }
    }

    /**
     * Código cliente: equipa una estación y ejecuta un riego de prueba.
     * Solo conoce FabricaEquipamiento; no sabe qué familia recibió.
     */
    private static void instalarYRegar(FabricaEquipamiento fabrica) {
        EstacionMonitoreo estacion = new EstacionMonitoreo(fabrica, "TST");
        System.out.println("  Zona " + estacion.getZona() + ": "
                + estacion.getSensores().size() + " sensores instalados");
        System.out.print("  ");
        estacion.getActuador().regar(5);
    }
}
