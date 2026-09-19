/**
 * PATRÓN ABSTRACT FACTORY — la FÁBRICA ABSTRACTA.
 *
 * Declara un método de creación por cada producto de la familia. Una
 * fábrica concreta devuelve SIEMPRE productos de la misma familia, así que
 * es imposible equipar un lote con un sensor de invernadero y un aspersor
 * de campo abierto: la coherencia la garantiza el tipo, no una convención.
 *
 * Diferencia con el Factory Method de la Semana 4: allí cada creador
 * fabricaba UN producto; aquí una fábrica produce una FAMILIA completa de
 * productos que deben ir juntos.
 */
public interface FabricaEquipamiento {

    Sensor crearSensorHumedad(String id);

    Sensor crearSensorTemperatura(String id);

    ActuadorRiego crearActuadorRiego();

    /** Nombre de la familia, para los reportes y las pruebas. */
    String getZona();
}
