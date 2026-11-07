import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * COLEGA: deja constancia de lo que ocurre en la jornada.
 *
 * Es el colega más pasivo —solo escucha— y por eso es el que mejor muestra
 * la ventaja del patrón: se agregó al final, y ni la estación, ni el
 * gestor, ni las operaciones tuvieron que cambiar una línea. Solo el
 * mediador supo de su existencia.
 */
public class Bitacora extends Colega {

    private final List<String> entradas = new ArrayList<>();

    public Bitacora(Mediador mediador) {
        super(mediador, "Bitacora");
    }

    /** Lo llama el mediador para dejar registro. */
    public void registrar(String texto) {
        entradas.add(texto);
        System.out.println("    [BITACORA] " + texto);
    }

    public List<String> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    public int cuantasEntradas() { return entradas.size(); }
}
