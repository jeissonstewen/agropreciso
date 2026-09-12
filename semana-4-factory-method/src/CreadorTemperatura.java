/** CREADOR CONCRETO: fabrica sensores de temperatura. */
public class CreadorTemperatura extends CreadorSensor {

    @Override
    protected Sensor crearSensor(String id) {
        return new SensorTemperatura(id);
    }
}
