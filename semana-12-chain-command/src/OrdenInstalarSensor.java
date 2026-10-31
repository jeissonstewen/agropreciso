import java.util.Objects;

/**
 * ORDEN CONCRETA: instala un sensor de repuesto en un lote.
 *
 * También REVERSIBLE: deshacer() retira el sensor que esta misma orden
 * instaló. Guarda la referencia al sensor justamente para poder quitar ese
 * y no otro.
 */
public class OrdenInstalarSensor implements Orden {

    private final Lote   lote;      // el receptor
    private final Sensor sensor;

    private boolean instalado = false;

    public OrdenInstalarSensor(Lote lote, Sensor sensor) {
        this.lote   = Objects.requireNonNull(lote,   "La orden necesita un lote");
        this.sensor = Objects.requireNonNull(sensor, "La orden necesita un sensor");
    }

    @Override
    public void ejecutar() {
        lote.agregarSensor(sensor);
        instalado = true;
        System.out.printf("    [INSTALAR] %s: sensor %s instalado (%d en total)%n",
                lote.getNombre(), sensor.getTipo(), lote.contarSensores());
    }

    @Override
    public void deshacer() {
        if (!instalado) {
            throw new IllegalStateException("No se puede retirar un sensor que no se instalo");
        }
        lote.getSensores().remove(sensor);            // retira ESE sensor, no otro
        instalado = false;
        System.out.printf("    [DESHACER] %s: sensor retirado (%d en total)%n",
                lote.getNombre(), lote.contarSensores());
    }

    @Override
    public boolean esReversible() { return true; }

    @Override
    public String getDescripcion() {
        return String.format("Instalar sensor de %s en %s", sensor.getTipo(), lote.getNombre());
    }
}
