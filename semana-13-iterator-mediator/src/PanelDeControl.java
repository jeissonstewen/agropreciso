/* ---------------------------------------------------------------
 * COPIA de la Semana 12 (patrones Chain of Responsibility y Command).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-12-chain-command/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * INVOCADOR del patrón Command.
 *
 * No sabe qué hace cada orden ni sobre qué objeto actúa: solo las ejecuta,
 * las guarda y, cuando se puede, las deshace. Ese desconocimiento es lo que
 * permite que el panel funcione igual con órdenes que todavía no existen.
 *
 * Mantiene dos colecciones, y la diferencia importa:
 *   - historial : PILA (LIFO). Se deshace la última ejecutada.
 *   - pendientes: COLA (FIFO). Se ejecutan en el orden en que se programaron.
 */
public class PanelDeControl {

    private final Deque<Orden> historial  = new ArrayDeque<>();
    private final Queue<Orden> pendientes = new ArrayDeque<>();

    /** Ejecuta la orden de inmediato y la deja registrada. */
    public void ejecutar(Orden orden) {
        Objects.requireNonNull(orden, "No se puede ejecutar una orden nula");
        orden.ejecutar();
        historial.push(orden);
    }

    /** Guarda la orden para ejecutarla después (riego nocturno, mantenimiento). */
    public void programar(Orden orden) {
        pendientes.add(Objects.requireNonNull(orden, "No se puede programar una orden nula"));
    }

    /**
     * Ejecuta todas las órdenes pendientes en el orden en que se programaron.
     * @return cuántas se ejecutaron
     */
    public int ejecutarPendientes() {
        int ejecutadas = 0;
        while (!pendientes.isEmpty()) {
            ejecutar(pendientes.poll());
            ejecutadas++;
        }
        return ejecutadas;
    }

    /**
     * Deshace la última orden ejecutada, si es reversible.
     *
     * Una orden irreversible NO se saca del historial: la operación
     * realmente ocurrió y el registro debe conservarla.
     *
     * @return true si se deshizo
     */
    public boolean deshacerUltima() {
        if (historial.isEmpty()) {
            System.out.println("    [PANEL] No hay nada que deshacer");
            return false;
        }
        Orden ultima = historial.peek();
        if (!ultima.esReversible()) {
            System.out.println("    [PANEL] No se puede deshacer: " + ultima.getDescripcion());
            return false;
        }
        historial.pop().deshacer();
        return true;
    }

    public int cuantasEjecutadas() { return historial.size(); }
    public int cuantasPendientes() { return pendientes.size(); }

    /** Bitácora de lo ejecutado, de la más reciente a la más antigua. */
    public List<String> getHistorial() {
        List<String> lineas = new ArrayList<>();
        for (Orden o : historial) lineas.add(o.getDescripcion());
        return lineas;
    }

    public void imprimirHistorial() {
        System.out.println("  Bitacora (de la mas reciente a la mas antigua):");
        if (historial.isEmpty()) {
            System.out.println("    (vacia)");
            return;
        }
        int n = historial.size();
        for (Orden o : historial) {
            System.out.printf("    %d. %-46s %s%n", n--, o.getDescripcion(),
                    o.esReversible() ? "[reversible]" : "[definitiva]");
        }
    }
}
