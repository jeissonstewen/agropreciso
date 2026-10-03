import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón COMPOSITE.
 *
 * Verifican que las operaciones agregadas recorran el árbol completo y que
 * una hoja y un compuesto se comporten igual ante el mismo cliente.
 */
@DisplayName("Composite: estructura de la finca")
class CompositeFincaTest {

    private static final double DELTA = 0.001;

    private Lote cafeNorte;
    private Lote cafeSur;
    private Lote maizEste;
    private ZonaCultivo invernadero;
    private ZonaCultivo campoAbierto;
    private ZonaCultivo finca;

    /** Árbol de prueba: finca -> (invernadero -> 2 lotes, campo -> 1 lote). */
    @BeforeEach
    void armarArbol() {
        cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .agregarSensor(new SensorFijo("CN-T", "TEMPERATURA", 22.0, "C"));
        cafeSur = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CS-H", "HUMEDAD", 78.0, "%"));
        maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 31.0, "%"))
                .agregarSensor(new SensorFijo("ME-T", "TEMPERATURA", 37.0, "C"));

        invernadero  = new ZonaCultivo("Invernadero").agregar(cafeNorte).agregar(cafeSur);
        campoAbierto = new ZonaCultivo("Campo abierto").agregar(maizEste);
        finca        = new ZonaCultivo("Finca La Esperanza")
                .agregar(invernadero).agregar(campoAbierto);
    }

    @Test
    @DisplayName("el área de una zona es la suma de la de sus hijos")
    void areaDeUnaZona() {
        assertEquals(3.5, invernadero.getAreaHectareas(), DELTA);   // 1.5 + 2.0
    }

    @Test
    @DisplayName("el área de la finca suma todos los niveles del árbol")
    void areaRecursivaDeLaFinca() {
        assertEquals(9.5, finca.getAreaHectareas(), DELTA);         // 3.5 + 6.0
    }

    @Test
    @DisplayName("contarSensores() recorre el árbol completo")
    void contarSensoresEsRecursivo() {
        assertEquals(2, cafeNorte.contarSensores());
        assertEquals(3, invernadero.contarSensores());
        assertEquals(5, finca.contarSensores());
    }

    @Test
    @DisplayName("una zona vacía no aporta área ni sensores")
    void zonaVacia() {
        ZonaCultivo baldio = new ZonaCultivo("Lote en barbecho");

        assertEquals(0.0, baldio.getAreaHectareas(), DELTA);
        assertEquals(0, baldio.contarSensores());
        assertTrue(baldio.getHijos().isEmpty());
    }

    @Test
    @DisplayName("agregar un hijo actualiza los totales de todos sus ancestros")
    void agregarPropagaHaciaArriba() {
        campoAbierto.agregar(new Lote("Papa-Oeste", "papa", 3.0, new UmbralesLote(50.0, 20.0)));

        assertEquals(9.0, campoAbierto.getAreaHectareas(), DELTA);
        assertEquals(12.5, finca.getAreaHectareas(), DELTA);
    }

    @Test
    @DisplayName("quitar un hijo devuelve los totales a su valor anterior")
    void quitarRestaDelTotal() {
        Lote temporal = new Lote("Papa-Oeste", "papa", 3.0, new UmbralesLote(50.0, 20.0));
        campoAbierto.agregar(temporal);

        assertTrue(campoAbierto.quitar(temporal));
        assertEquals(9.5, finca.getAreaHectareas(), DELTA);
        assertFalse(campoAbierto.quitar(temporal), "quitar dos veces debe devolver false");
    }

    @Test
    @DisplayName("el cliente trata igual a una hoja y a un compuesto")
    void hojaYCompuestoSonIntercambiables() {
        // El mismo tipo estático para los dos: esa es la uniformidad del patrón.
        ComponenteFinca hoja     = cafeNorte;
        ComponenteFinca compuesto = finca;

        assertEquals("Cafe-Norte", hoja.getNombre());
        assertEquals("Finca La Esperanza", compuesto.getNombre());
        assertEquals(1.5, hoja.getAreaHectareas(), DELTA);
        assertEquals(9.5, compuesto.getAreaHectareas(), DELTA);
    }

    @Test
    @DisplayName("describir() sangra cada nivel y anida las hojas")
    void describirMuestraLaJerarquia() {
        String arbol = finca.describir(0);

        assertTrue(arbol.startsWith("+ Finca La Esperanza"), arbol);
        assertTrue(arbol.contains("  + Invernadero"), arbol);
        assertTrue(arbol.contains("    - Cafe-Norte"), arbol);
        assertTrue(arbol.contains("1 sensor,"), "debe concordar el singular: " + arbol);
    }

    @Test
    @DisplayName("una zona anidada dentro de otra sigue sumando bien")
    void anidamientoDeVariosNiveles() {
        ZonaCultivo sector = new ZonaCultivo("Sector A")
                .agregar(new Lote("Extra", "cafe", 0.5, new UmbralesLote(70.0, 26.0)));
        invernadero.agregar(sector);

        assertEquals(4.0, invernadero.getAreaHectareas(), DELTA);   // 3.5 + 0.5
        assertEquals(10.0, finca.getAreaHectareas(), DELTA);
    }
}
