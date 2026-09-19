/**
 * FÁBRICA CONCRETA: produce la familia de equipamiento de CAMPO ABIERTO.
 * Sensores de rango amplio y riego por aspersión.
 */
public class FabricaCampoAbierto implements FabricaEquipamiento {

    @Override
    public Sensor crearSensorHumedad(String id) {
        return new SensorHumedadCampo(id);
    }

    @Override
    public Sensor crearSensorTemperatura(String id) {
        return new SensorTemperaturaCampo(id);
    }

    @Override
    public ActuadorRiego crearActuadorRiego() {
        return new RiegoAspersor();
    }

    @Override
    public String getZona() { return "CAMPO ABIERTO"; }
}
