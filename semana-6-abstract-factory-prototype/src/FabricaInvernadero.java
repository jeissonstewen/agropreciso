/**
 * FÁBRICA CONCRETA: produce la familia de equipamiento de INVERNADERO.
 * Todos sus productos son coherentes entre sí: sensores de rango estrecho
 * y riego por goteo.
 */
public class FabricaInvernadero implements FabricaEquipamiento {

    @Override
    public Sensor crearSensorHumedad(String id) {
        return new SensorHumedadInvernadero(id);
    }

    @Override
    public Sensor crearSensorTemperatura(String id) {
        return new SensorTemperaturaInvernadero(id);
    }

    @Override
    public ActuadorRiego crearActuadorRiego() {
        return new RiegoGoteo();
    }

    @Override
    public String getZona() { return "INVERNADERO"; }
}
