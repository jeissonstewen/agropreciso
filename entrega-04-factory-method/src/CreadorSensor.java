/**
 * PATRÓN FACTORY METHOD — clase CREADORA (abstracta).
 *
 * Define el esqueleto del trabajo con un sensor (instalarlo, leerlo y
 * reportar la lectura) pero NO decide qué sensor concreto se construye:
 * esa decisión la delega en las subclases a través de crearSensor().
 *
 * Así el código de esta clase se escribe una sola vez y sirve para
 * cualquier sensor presente o futuro.
 */
public abstract class CreadorSensor {

    /**
     * EL FACTORY METHOD.
     * Es abstracto: cada subclase decide qué producto concreto devolver.
     */
    protected abstract Sensor crearSensor(String id);

    /**
     * Operación del sistema que USA el producto sin conocer su clase concreta.
     * Nótese que aquí no hay ningún 'new SensorHumedad()' ni ningún if/else
     * por tipo: solo se pide el sensor y se trabaja con la interfaz Sensor.
     */
    public Lectura tomarLectura(String id) {
        Sensor sensor = crearSensor(id);          // <- aquí actúa el Factory Method
        Lectura lectura = sensor.leer();
        System.out.println("[SENSOR] " + lectura);
        return lectura;
    }
}
