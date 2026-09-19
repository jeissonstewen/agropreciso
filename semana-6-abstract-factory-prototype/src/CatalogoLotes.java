import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * REGISTRO DE PROTOTIPOS.
 *
 * Guarda las plantillas de lote bajo una clave ("cafe-invernadero",
 * "maiz-campo") y entrega copias listas para usar. El cliente nunca toca
 * la plantilla original: siempre recibe un clon con su propio nombre y área.
 */
public class CatalogoLotes {

    private final Map<String, Lote> plantillas = new LinkedHashMap<>();

    public void registrar(String clave, Lote plantilla) {
        plantillas.put(clave, plantilla);
    }

    /** Clona la plantilla indicada y la personaliza. */
    public Lote crear(String clave, String nombre, double areaHectareas) {
        Lote plantilla = plantillas.get(clave);
        if (plantilla == null) {
            throw new IllegalArgumentException("No existe la plantilla '" + clave + "'");
        }
        Lote lote = plantilla.clone();
        lote.setNombre(nombre);
        lote.setAreaHectareas(areaHectareas);
        return lote;
    }

    public Set<String> claves() {
        return plantillas.keySet();
    }
}
