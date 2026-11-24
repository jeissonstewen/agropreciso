import java.util.Collections;
import java.util.List;

/**
 * Resultado de una predicción de cosecha.
 *
 * Es un objeto de valor inmutable: lo produce el método plantilla y nadie
 * lo modifica después. Guarda los tres valores intermedios —base, ajustada
 * y neta— y no solo el final, porque al sustentar interesa ver en qué paso
 * del esqueleto cambió el número.
 *
 * La TRAZA es la lista de pasos que ejecutó la plantilla. Sirve para
 * demostrar lo que afirma el patrón: que todos los modelos recorren el
 * MISMO esqueleto, en el mismo orden, aunque den resultados distintos.
 */
public class Prediccion {

    private final String modelo;
    private final String lote;
    private final double toneladasBase;
    private final double toneladasAjustadas;
    private final double toneladasNetas;
    private final List<String> traza;

    public Prediccion(String modelo, String lote, double toneladasBase,
                      double toneladasAjustadas, double toneladasNetas, List<String> traza) {
        this.modelo             = modelo;
        this.lote               = lote;
        this.toneladasBase      = toneladasBase;
        this.toneladasAjustadas = toneladasAjustadas;
        this.toneladasNetas     = toneladasNetas;
        this.traza              = Collections.unmodifiableList(traza);
    }

    public String getModelo()             { return modelo; }
    public String getLote()               { return lote; }
    public double getToneladasBase()      { return toneladasBase; }
    public double getToneladasAjustadas() { return toneladasAjustadas; }
    public double getToneladasNetas()     { return toneladasNetas; }

    /** Los pasos que ejecutó la plantilla, en orden. Lista inmutable. */
    public List<String> getTraza()        { return traza; }

    /** Imprime la traza con sangría, para la demo. */
    public void imprimirTraza() {
        for (String paso : traza) System.out.println("      " + paso);
    }

    @Override
    public String toString() {
        return String.format("%-22s %s: base %.2f t -> ajustada %.2f t -> neta %.2f t",
                modelo, lote, toneladasBase, toneladasAjustadas, toneladasNetas);
    }
}
