import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón ITERATOR.
 *
 * Comprueban que el recorrido esté encapsulado en el iterador —dos órdenes
 * distintos sobre el mismo árbol— y que cada iterador lleve su propio
 * estado.
 */
@DisplayName("Iterator: recorridos del árbol de la finca")
class RecorridoFincaTest {

    private ZonaCultivo finca;

    /**
     * Árbol de TRES niveles. Con uno plano, los dos recorridos darían el
     * mismo orden y la prueba no demostraría nada.
     *
     *   Finca ── Invernadero ── Sector A ── Cafe-Norte, Cafe-Sur
     *         │               └─ Cafe-Alto
     *         └─ Campo abierto ── Maiz-Este
     */
    @BeforeEach
    void armarFinca() {
        finca = new ZonaCultivo("Finca")
                .agregar(new ZonaCultivo("Invernadero")
                        .agregar(new ZonaCultivo("Sector A")
                                .agregar(lote("Cafe-Norte", 1.5))
                                .agregar(lote("Cafe-Sur", 2.0)))
                        .agregar(lote("Cafe-Alto", 1.2)))
                .agregar(new ZonaCultivo("Campo abierto")
                        .agregar(lote("Maiz-Este", 6.0)));
    }

    private Lote lote(String nombre, double area) {
        return new Lote(nombre, "cafe", area, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo(nombre + "-H", "HUMEDAD", 64.0, "%"));
    }

    private List<String> nombres(Iterator<Lote> it) {
        List<String> salida = new ArrayList<>();
        while (it.hasNext()) salida.add(it.next().getNombre());
        return salida;
    }

    @Test
    @DisplayName("el recorrido en profundidad baja hasta las hojas de cada rama")
    void recorridoEnProfundidad() {
        List<String> orden = nombres(finca.iterador(TipoRecorrido.PROFUNDIDAD));

        assertEquals(List.of("Cafe-Norte", "Cafe-Sur", "Cafe-Alto", "Maiz-Este"), orden);
    }

    @Test
    @DisplayName("el recorrido por niveles devuelve antes los lotes más cercanos a la raíz")
    void recorridoPorNiveles() {
        List<String> orden = nombres(finca.iterador(TipoRecorrido.NIVELES));

        assertEquals(List.of("Cafe-Alto", "Maiz-Este", "Cafe-Norte", "Cafe-Sur"), orden);
    }

    @Test
    @DisplayName("los dos recorridos visitan los mismos lotes, en distinto orden")
    void mismosLotesDistintoOrden() {
        List<String> profundidad = nombres(finca.iterador(TipoRecorrido.PROFUNDIDAD));
        List<String> niveles     = nombres(finca.iterador(TipoRecorrido.NIVELES));

        assertNotSame(profundidad, niveles);
        assertEquals(4, profundidad.size());
        assertTrue(profundidad.containsAll(niveles), "deben ser los mismos cuatro lotes");
        assertFalse(profundidad.equals(niveles), "el orden debe diferir");
    }

    @Test
    @DisplayName("dos iteradores simultáneos no se estorban")
    void iteradoresIndependientes() {
        Iterator<Lote> a = finca.iterador(TipoRecorrido.PROFUNDIDAD);
        Iterator<Lote> b = finca.iterador(TipoRecorrido.PROFUNDIDAD);

        a.next();
        a.next();                          // 'a' va por el tercero

        assertEquals("Cafe-Norte", b.next().getNombre(), "'b' debe empezar desde el principio");
        assertEquals("Cafe-Alto",  a.next().getNombre());
    }

    @Test
    @DisplayName("el for-each recorre la finca porque es Iterable")
    void forEachFunciona() {
        double total = 0;
        int cuantos = 0;

        for (Lote lote : finca) {
            total += lote.getAreaHectareas();
            cuantos++;
        }

        assertEquals(4, cuantos);
        assertEquals(finca.getAreaHectareas(), total, 0.001,
                "recorrer lote por lote debe dar lo mismo que preguntarle al Composite");
    }

    @Test
    @DisplayName("una zona vacía no produce ningún lote")
    void zonaVacia() {
        ZonaCultivo baldio = new ZonaCultivo("En barbecho");

        assertFalse(baldio.iterator().hasNext());
    }

    @Test
    @DisplayName("una zona que solo contiene zonas vacías tampoco produce lotes")
    void zonasAnidadasVacias() {
        ZonaCultivo raiz = new ZonaCultivo("Raiz")
                .agregar(new ZonaCultivo("Vacia 1"))
                .agregar(new ZonaCultivo("Vacia 2"));

        assertFalse(raiz.iterator().hasNext());
    }

    @Test
    @DisplayName("pedir un lote de más lanza NoSuchElementException")
    void seAgotaElRecorrido() {
        Iterator<Lote> it = new ZonaCultivo("Vacia").iterator();

        assertThrows(java.util.NoSuchElementException.class, it::next);
    }

    @Test
    @DisplayName("agregar un lote cambia lo que devuelve el siguiente recorrido")
    void elRecorridoReflejaElArbol() {
        finca.agregar(lote("Papa-Oeste", 3.0));

        assertEquals(5, nombres(finca.iterator()).size());
    }

    @Test
    @DisplayName("el iterador necesita una zona raíz")
    void iteradorSinRaiz() {
        assertThrows(NullPointerException.class, () -> new IteradorEnProfundidad(null));
        assertThrows(NullPointerException.class, () -> new IteradorPorNiveles(null));
    }
}
