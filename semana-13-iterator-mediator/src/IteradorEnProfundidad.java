import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * PATRÓN ITERATOR — iterador concreto, recorrido EN PROFUNDIDAD.
 *
 * Devuelve los lotes bajando hasta el fondo de cada rama antes de pasar a
 * la siguiente. Para la finca del ejemplo, el orden es el que un humano
 * leería en el árbol impreso, de arriba abajo.
 *
 * Implementa java.util.Iterator a propósito: el rol "Iterator" del GoF ya
 * existe en la biblioteca estándar de Java. Reutilizarlo es lo correcto y
 * además permite el for-each.
 *
 * Usa una PILA. El estado del recorrido vive aquí, no en la estructura:
 * por eso pueden existir varios iteradores a la vez sobre el mismo árbol
 * sin estorbarse.
 */
public class IteradorEnProfundidad implements Iterator<Lote> {

    private final Deque<ComponenteFinca> pila = new ArrayDeque<>();

    public IteradorEnProfundidad(ZonaCultivo raiz) {
        pila.push(Objects.requireNonNull(raiz, "El recorrido necesita una zona raiz"));
    }

    /**
     * Avanza hasta dejar un lote en la cima, o hasta vaciar la pila.
     * Las zonas se expanden; los lotes se quedan esperando a next().
     */
    @Override
    public boolean hasNext() {
        while (!pila.isEmpty() && !(pila.peek() instanceof Lote)) {
            ComponenteFinca zona = pila.pop();
            List<ComponenteFinca> hijos = ((ZonaCultivo) zona).getHijos();
            // Se apilan al revés para que salgan en el orden en que se agregaron.
            for (int i = hijos.size() - 1; i >= 0; i--) {
                pila.push(hijos.get(i));
            }
        }
        return !pila.isEmpty();
    }

    @Override
    public Lote next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No quedan lotes por recorrer");
        }
        return (Lote) pila.pop();
    }
}
