/* ---------------------------------------------------------------
 * COPIA de la Semana 13 (patrón Iterator).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-13-iterator-mediator/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

/**
 * PATRÓN ITERATOR — iterador concreto, recorrido POR NIVELES.
 *
 * Devuelve los lotes en orden de cercanía a la raíz: primero los que
 * cuelgan directamente de la finca, después los de las subzonas. Sirve,
 * por ejemplo, para inspeccionar antes los lotes más accesibles.
 *
 * La única diferencia con el recorrido en profundidad es la estructura que
 * guarda lo pendiente: aquí una COLA en vez de una pila. Ese cambio de una
 * línea produce un orden distinto, y ni ZonaCultivo ni Lote se enteran.
 */
public class IteradorPorNiveles implements Iterator<Lote> {

    private final Queue<ComponenteFinca> cola = new ArrayDeque<>();

    public IteradorPorNiveles(ZonaCultivo raiz) {
        cola.add(Objects.requireNonNull(raiz, "El recorrido necesita una zona raiz"));
    }

    @Override
    public boolean hasNext() {
        while (!cola.isEmpty() && !(cola.peek() instanceof Lote)) {
            ZonaCultivo zona = (ZonaCultivo) cola.poll();
            cola.addAll(zona.getHijos());
        }
        return !cola.isEmpty();
    }

    @Override
    public Lote next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No quedan lotes por recorrer");
        }
        return (Lote) cola.poll();
    }
}
