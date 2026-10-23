/**
 * BANCO DE MEDICIÓN — ¿cuánta memoria ahorraría el patrón Flyweight?
 *
 * Esta clase NO implementa el patrón en el sistema: es la evidencia con la
 * que se sustenta la decisión de NO implementarlo todavía. Mide cuánta
 * memoria ocupan N sensores cuando cada uno guarda su propia copia del
 * estado intrínseco (tipo, unidad, rango) frente a cuando todos comparten
 * un único objeto con esos datos.
 *
 * Compilar y ejecutar, desde semana-10-proxy-flyweight/:
 *    javac -d out/medicion medicion/*.java
 *    java -cp out/medicion BancoMemoriaFlyweight
 *
 * Nota metodológica: la medición con Runtime no es exacta —el recolector de
 * basura y el JIT introducen ruido— pero el orden de magnitud es
 * suficiente para la decisión que se quiere sustentar.
 */
public class BancoMemoriaFlyweight {

    /** Tamaños de finca que se van a medir. */
    private static final int[] TAMANOS = { 5, 100, 10_000, 100_000, 500_000 };

    /** Evita que el recolector se lleve los objetos antes de medirlos. */
    private static Object retenido;

    public static void main(String[] args) {
        System.out.println("=== Banco de memoria: Flyweight hipotetico sobre TipoSensor ===");
        System.out.printf("JVM %s | heap maximo: %d MB%n%n",
                System.getProperty("java.version"),
                Runtime.getRuntime().maxMemory() / (1024 * 1024));

        // Calentamiento: la primera medicion de la JVM esta contaminada por
        // la carga de clases y el JIT. Se descarta a proposito.
        medirSinCompartir(50_000);
        medirConFlyweight(50_000);
        retenido = null;

        System.out.println("  N sensores | sin compartir | con flyweight |      ahorro | por sensor");
        System.out.println("  -----------|---------------|---------------|-------------|-----------");

        long ahorroPorSensor = 0;
        for (int n : TAMANOS) {
            long sinCompartir = medirSinCompartir(n);
            long conFlyweight = medirConFlyweight(n);
            long ahorro = sinCompartir - conFlyweight;
            ahorroPorSensor = (n >= 100_000) ? ahorro / n : ahorroPorSensor;

            System.out.printf("  %10s | %10s KB | %10s KB | %8s KB | %6d B%n",
                    conSeparador(n), conSeparador(sinCompartir / 1024),
                    conSeparador(conFlyweight / 1024), conSeparador(ahorro / 1024),
                    n == 0 ? 0 : ahorro / n);
        }

        System.out.println();
        System.out.println("--- Lectura de los resultados ---");
        System.out.printf("  Ahorro medido por sensor        : ~%d bytes%n", ahorroPorSensor);
        if (ahorroPorSensor > 0) {
            long paraUnMega = (1024 * 1024) / ahorroPorSensor;
            System.out.printf("  Sensores para ahorrar 1 MB      : ~%s%n", conSeparador(paraUnMega));
            System.out.printf("  Ahorro con los 5 del prototipo  : ~%d bytes%n", 5 * ahorroPorSensor);
        }
        System.out.println();
        System.out.println("  Conclusion: con la escala actual del prototipo el ahorro es");
        System.out.println("  irrelevante. El patron se justifica cuando la finca instrumentada");
        System.out.println("  supera el orden de las decenas de miles de sensores.");
    }

    // ------------------------------------------------------------------
    //  Las dos variantes que se comparan
    // ------------------------------------------------------------------

    /**
     * SIN compartir: cada sensor guarda su propia copia del tipo, la unidad
     * y los límites del rango.
     *
     * Las cadenas se crean con 'new String(...)' a propósito. Java interna
     * los literales, así que si se escribieran directamente todos los
     * sensores compartirían la misma instancia y la medición saldría falsa.
     * En un sistema real esos datos vendrían de un archivo o de una base de
     * datos y tampoco estarían internados.
     */
    private static long medirSinCompartir(int n) {
        long base = memoriaUsada();
        SensorSinCompartir[] sensores = new SensorSinCompartir[n];
        for (int i = 0; i < n; i++) {
            sensores[i] = new SensorSinCompartir("S-" + i,
                    new String("HUMEDAD"), new String("%"), 10.0, 90.0);
        }
        long usada = memoriaUsada() - base;
        retenido = sensores;
        return usada;
    }

    /**
     * CON flyweight: un único objeto con el estado intrínseco, compartido
     * por todos los sensores. Cada sensor solo guarda su id (su estado
     * extrínseco) y una referencia.
     */
    private static long medirConFlyweight(int n) {
        long base = memoriaUsada();
        TipoSensorCompartido humedad = new TipoSensorCompartido("HUMEDAD", "%", 10.0, 90.0);
        SensorConFlyweight[] sensores = new SensorConFlyweight[n];
        for (int i = 0; i < n; i++) {
            sensores[i] = new SensorConFlyweight("S-" + i, humedad);
        }
        long usada = memoriaUsada() - base;
        retenido = sensores;
        return usada;
    }

    // ------------------------------------------------------------------
    //  Utilidades de medición
    // ------------------------------------------------------------------

    /** Pide varias recolecciones antes de leer, para reducir el ruido. */
    private static long memoriaUsada() {
        Runtime rt = Runtime.getRuntime();
        for (int i = 0; i < 4; i++) {
            rt.gc();
            try { Thread.sleep(40); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        return rt.totalMemory() - rt.freeMemory();
    }

    private static String conSeparador(long valor) {
        return String.format("%,d", valor);
    }
}

/** Variante actual: cada instancia carga con su propio estado intrínseco. */
class SensorSinCompartir {
    private final String id;        // extrínseco: propio de cada sensor
    private final String tipo;      // intrínseco: repetido en cada instancia
    private final String unidad;    // intrínseco
    private final double rangoMin;  // intrínseco
    private final double rangoMax;  // intrínseco

    SensorSinCompartir(String id, String tipo, String unidad, double rangoMin, double rangoMax) {
        this.id = id; this.tipo = tipo; this.unidad = unidad;
        this.rangoMin = rangoMin; this.rangoMax = rangoMax;
    }
}

/** El FLYWEIGHT hipotético: estado intrínseco, inmutable y compartido. */
class TipoSensorCompartido {
    private final String tipo;
    private final String unidad;
    private final double rangoMin;
    private final double rangoMax;

    TipoSensorCompartido(String tipo, String unidad, double rangoMin, double rangoMax) {
        this.tipo = tipo; this.unidad = unidad;
        this.rangoMin = rangoMin; this.rangoMax = rangoMax;
    }
}

/** El CONTEXTO: solo su estado extrínseco más una referencia al flyweight. */
class SensorConFlyweight {
    private final String id;                 // extrínseco
    private final TipoSensorCompartido tipo; // compartido con los demás

    SensorConFlyweight(String id, TipoSensorCompartido tipo) {
        this.id = id; this.tipo = tipo;
    }
}
