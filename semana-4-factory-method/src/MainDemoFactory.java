import java.util.Random;

/**
 * Demostración del patrón FACTORY METHOD en el módulo de sensores.
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoFactory
 */
public class MainDemoFactory {

    public static void main(String[] args) {

        System.out.println("=== PRUEBA 1: cada creador fabrica su propio tipo de sensor ===");
        CreadorSensor cHumedad = new CreadorHumedad();
        CreadorSensor cTemp    = new CreadorTemperatura();
        CreadorSensor cPH      = new CreadorPH();

        Lectura l1 = cHumedad.tomarLectura("S-01");
        Lectura l2 = cTemp.tomarLectura("S-02");
        Lectura l3 = cPH.tomarLectura("S-03");

        System.out.println("Tipo devuelto por CreadorHumedad     : " + l1.getTipo());
        System.out.println("Tipo devuelto por CreadorTemperatura : " + l2.getTipo());
        System.out.println("Tipo devuelto por CreadorPH          : " + l3.getTipo());

        System.out.println("\n=== PRUEBA 2: el cliente trabaja sin conocer las clases concretas ===");
        // El arreglo es del tipo abstracto. No hay if/else ni 'new SensorX' aquí.
        CreadorSensor[] estacion = { cHumedad, cTemp, cPH };
        monitorearCultivo(estacion);

        System.out.println("\n=== PRUEBA 3: las lecturas son aleatorias dentro del rango ===");
        for (int i = 1; i <= 3; i++) {
            System.out.print("Lectura " + i + " -> ");
            cHumedad.tomarLectura("S-01");
        }

        System.out.println("\n=== PRUEBA 4: la lectura se compara con la configuración global ===");
        ConfiguracionSistema cfg = ConfiguracionSistema.getInstancia();
        Lectura humedad = cHumedad.tomarLectura("S-01");
        System.out.println("Umbral de riego configurado: " + cfg.getUmbralHumedadMin() + "%");
        if (humedad.getValor() < cfg.getUmbralHumedadMin()) {
            System.out.println("[RIEGO] Humedad por debajo del umbral: se activa el riego.");
        } else {
            System.out.println("[RIEGO] Humedad suficiente: no se riega.");
        }

        System.out.println("\n=== PRUEBA 5: agregar un sensor nuevo no obliga a tocar el cliente ===");
        // Se suma un cuarto tipo de sensor que no existía cuando se escribió
        // monitorearCultivo(): el método sigue funcionando sin ninguna modificación.
        CreadorSensor[] estacionAmpliada = { cHumedad, cTemp, cPH, new CreadorLuminosidad() };
        monitorearCultivo(estacionAmpliada);
    }

    /**
     * Código cliente: recorre la estación y lee todos los sensores.
     * Solo conoce CreadorSensor y Lectura. Por eso no cambia nunca,
     * por más tipos de sensor que se agreguen al sistema.
     */
    private static void monitorearCultivo(CreadorSensor[] creadores) {
        int n = 1;
        for (CreadorSensor creador : creadores) {
            creador.tomarLectura("S-0" + n);
            n++;
        }
    }
}

/* ------------------------------------------------------------------
   Extensión de demostración: el cuarto tipo de sensor.
   Fíjese que para agregarlo solo hubo que escribir clases NUEVAS;
   no se modificó ninguna clase existente (principio abierto/cerrado).
   ------------------------------------------------------------------ */

/** PRODUCTO CONCRETO nuevo: mide luminosidad en lux. */
class SensorLuminosidad implements Sensor {

    private final String id;
    private final Random aleatorio = new Random();

    public SensorLuminosidad(String id) {
        this.id = id;
    }

    @Override
    public Lectura leer() {
        double valor = 200 + aleatorio.nextDouble() * 1800;
        return new Lectura(id, getTipo(), valor, "lux");
    }

    @Override
    public String getTipo() { return "LUMINOSIDAD"; }
}

/** CREADOR CONCRETO nuevo, para el sensor de luminosidad. */
class CreadorLuminosidad extends CreadorSensor {

    @Override
    protected Sensor crearSensor(String id) {
        return new SensorLuminosidad(id);
    }
}
