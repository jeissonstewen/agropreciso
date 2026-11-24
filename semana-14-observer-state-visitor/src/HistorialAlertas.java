import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OBSERVADOR CONCRETO: guarda todo lo publicado.
 * No imprime nada; acumula para consultarlo después.
 */
public class HistorialAlertas implements ObservadorAlerta {

    private final List<Alerta> alertas = new ArrayList<>();

    @Override
    public void alPublicarse(Alerta alerta) {
        alertas.add(alerta);
    }

    @Override
    public String getNombre() { return "HistorialAlertas"; }

    public List<Alerta> getAlertas() { return Collections.unmodifiableList(alertas); }

    public int cuantas() { return alertas.size(); }
}
