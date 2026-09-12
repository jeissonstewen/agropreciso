/* ---------------------------------------------------------------
 * COPIA de la Semana 4 (patrón Factory Method).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-4-factory-method/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/** CREADOR CONCRETO: fabrica sensores de humedad. */
public class CreadorHumedad extends CreadorSensor {

    @Override
    protected Sensor crearSensor(String id) {
        return new SensorHumedad(id);
    }
}
