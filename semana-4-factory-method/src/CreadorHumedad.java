/** CREADOR CONCRETO: fabrica sensores de humedad. */
public class CreadorHumedad extends CreadorSensor {

    @Override
    protected Sensor crearSensor(String id) {
        return new SensorHumedad(id);
    }
}
