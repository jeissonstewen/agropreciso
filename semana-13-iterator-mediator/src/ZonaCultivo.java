/* ---------------------------------------------------------------
 * COPIA ADAPTADA de la Semana 8 (patrón Composite).
 * El original, con su explicación, está en
 * semana-8-composite-decorator/ y NO fue modificado.
 *
 * Único cambio hecho aquí, y solo aquí, para la Semana 13:
 *   - Declara 'implements Iterable<Lote>' y agrega iterator() e
 *     iterador(TipoRecorrido). Con eso la zona pasa a ser el
 *     AGREGADO del patrón Iterator. El recorrido NO vive aquí:
 *     vive en las clases iteradoras.
 * --------------------------------------------------------------- */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;

/**
 * COMPUESTO del patrón Composite.
 *
 * Agrupa otros ComponenteFinca, que pueden ser lotes (hojas) u otras zonas.
 * Esa recursión es lo que permite que la finca completa sea, simplemente, la
 * zona de la raíz: no hace falta una clase Finca aparte.
 *
 * Sus operaciones no calculan nada por sí mismas: delegan en los hijos y
 * combinan el resultado. Por eso agregar un nivel más de anidamiento no
 * obliga a tocar una sola línea de esta clase.
 */
public class ZonaCultivo implements ComponenteFinca, Iterable<Lote> {

    private final String nombre;
    private final List<ComponenteFinca> hijos = new ArrayList<>();

    public ZonaCultivo(String nombre) {
        this.nombre = nombre;
    }

    // --- gestión de hijos: solo en el compuesto (variante de seguridad) ---

    public ZonaCultivo agregar(ComponenteFinca componente) {
        hijos.add(componente);
        return this;                       // permite encadenar al armar el árbol
    }

    public boolean quitar(ComponenteFinca componente) {
        return hijos.remove(componente);
    }

    public List<ComponenteFinca> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    // --- patrón ITERATOR: el agregado crea sus iteradores ---

    /**
     * Permite recorrer los lotes de la finca con un for-each:
     *
     *     for (Lote lote : finca) { ... }
     *
     * Por defecto recorre en profundidad.
     */
    @Override
    public Iterator<Lote> iterator() {
        return iterador(TipoRecorrido.PROFUNDIDAD);
    }

    /**
     * Devuelve el iterador del recorrido pedido. Es, de paso, un FACTORY
     * METHOD como el de la Semana 4: el cliente pide un recorrido y recibe
     * un Iterator, sin conocer la clase concreta que lo implementa.
     */
    public Iterator<Lote> iterador(TipoRecorrido tipo) {
        return tipo == TipoRecorrido.NIVELES
                ? new IteradorPorNiveles(this)
                : new IteradorEnProfundidad(this);
    }

    // --- operaciones del componente: recursivas ---

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getAreaHectareas() {
        double total = 0;
        for (ComponenteFinca hijo : hijos) total += hijo.getAreaHectareas();
        return total;
    }

    @Override
    public int contarSensores() {
        int total = 0;
        for (ComponenteFinca hijo : hijos) total += hijo.contarSensores();
        return total;
    }

    @Override
    public void monitorear() {
        System.out.println("### Zona " + nombre
                + " (" + contar(hijos.size(), "elemento", "elementos") + ")");
        for (ComponenteFinca hijo : hijos) hijo.monitorear();
    }

    @Override
    public String describir(int nivel) {
        StringBuilder sb = new StringBuilder();
        sb.append(sangria(nivel))
          .append(String.format("+ %s  [%.1f ha, %s]%n", nombre, getAreaHectareas(),
                  contar(contarSensores(), "sensor", "sensores")));
        for (ComponenteFinca hijo : hijos) sb.append(hijo.describir(nivel + 1));
        return sb.toString();
    }

    /** Concuerda el número con el sustantivo: "1 sensor" / "3 sensores". */
    static String contar(int cantidad, String singular, String plural) {
        return cantidad + " " + (cantidad == 1 ? singular : plural);
    }

    /** Dos espacios por nivel de profundidad. */
    static String sangria(int nivel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nivel; i++) sb.append("  ");
        return sb.toString();
    }
}
