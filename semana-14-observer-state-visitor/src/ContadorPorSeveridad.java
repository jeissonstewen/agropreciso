import java.util.EnumMap;
import java.util.Map;

/**
 * OBSERVADOR CONCRETO: lleva la cuenta por gravedad.
 *
 * Se agregó al final y no obligó a cambiar nada: ni el sujeto, ni los
 * otros dos observadores. Eso es lo que compra el patrón.
 */
public class ContadorPorSeveridad implements ObservadorAlerta {

    private final Map<Severidad, Integer> conteo = new EnumMap<>(Severidad.class);

    @Override
    public void alPublicarse(Alerta alerta) {
        conteo.merge(alerta.getSeveridad(), 1, Integer::sum);
    }

    @Override
    public String getNombre() { return "ContadorPorSeveridad"; }

    public int contar(Severidad severidad) { return conteo.getOrDefault(severidad, 0); }

    public int total() {
        return conteo.values().stream().mapToInt(Integer::intValue).sum();
    }
}
