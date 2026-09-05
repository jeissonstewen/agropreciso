/** CREADOR CONCRETO: fabrica sensores de pH. */
public class CreadorPH extends CreadorSensor {

    @Override
    protected Sensor crearSensor(String id) {
        return new SensorPH(id);
    }
}
