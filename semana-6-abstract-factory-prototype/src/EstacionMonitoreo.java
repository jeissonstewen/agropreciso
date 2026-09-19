import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLIENTE del Abstract Factory.
 *
 * Arma la estación de un lote pidiéndole a la fábrica cada pieza. Solo
 * conoce las interfaces FabricaEquipamiento, Sensor y ActuadorRiego: no
 * hay un solo 'new' de clase concreta ni un if/else por zona. Cambiar de
 * invernadero a campo abierto es cambiar la fábrica que se le pasa.
 */
public class EstacionMonitoreo {

    private final String zona;
    private final List<Sensor> sensores;
    private final ActuadorRiego actuador;

    public EstacionMonitoreo(FabricaEquipamiento fabrica, String prefijoId) {
        this.zona = fabrica.getZona();
        List<Sensor> lista = new ArrayList<>();
        lista.add(fabrica.crearSensorHumedad(prefijoId + "-H"));
        lista.add(fabrica.crearSensorTemperatura(prefijoId + "-T"));
        this.sensores = Collections.unmodifiableList(lista);
        this.actuador = fabrica.crearActuadorRiego();
    }

    public String        getZona()     { return zona; }
    public List<Sensor>  getSensores() { return sensores; }
    public ActuadorRiego getActuador() { return actuador; }

    /** Lee todos los sensores e imprime la lectura. */
    public List<Lectura> tomarLecturas() {
        List<Lectura> lecturas = new ArrayList<>();
        for (Sensor s : sensores) {
            Lectura l = s.leer();
            System.out.println("  [" + zona + "] " + l);
            lecturas.add(l);
        }
        return lecturas;
    }
}
