import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón VISITOR.
 *
 * Comprueban que cada visitante produzca su resultado sobre el mismo árbol
 * y —lo más importante— que un visitante escrito en la propia prueba
 * funcione sin tocar ni ZonaCultivo ni Lote.
 */
@DisplayName("Visitor: operaciones sobre la finca")
class VisitantesFincaTest {

    private static final double DELTA = 0.001;

    private final PrintStream salidaOriginal = System.out;
    private ZonaCultivo finca;
    private Lote cafeNorte;

    @BeforeEach
    void armarFincaYSilenciarSalida() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(new SensorFijo("CN-H", "HUMEDAD", 64.0, "%"))
                .setActuador(new RiegoGoteo());
        Lote maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 52.0, "%"))
                .setActuador(new RiegoAspersor());
        cafeNorte.sembrar();
        maizEste.sembrar();

        finca = new ZonaCultivo("Finca")
                .agregar(new ZonaCultivo("Invernadero").agregar(cafeNorte))
                .agregar(new ZonaCultivo("Campo abierto").agregar(maizEste));
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("el inventario cuenta zonas, lotes, sensores y hectáreas")
    void inventario() {
        VisitanteInventario inventario = new VisitanteInventario();

        finca.aceptar(inventario);

        assertEquals(3, inventario.getZonas(), "la raiz tambien es una zona");
        assertEquals(2, inventario.getLotes());
        assertEquals(2, inventario.getSensores());
        assertEquals(7.5, inventario.getHectareas(), DELTA);
    }

    @Test
    @DisplayName("el inventario agrupa los lotes por estado")
    void inventarioPorEstado() {
        cafeNorte.madurar();
        VisitanteInventario inventario = new VisitanteInventario();

        finca.aceptar(inventario);

        assertEquals(1, inventario.enEstado("maduro"));
        assertEquals(1, inventario.enEstado("sembrado"));
    }

    @Test
    @DisplayName("el costo de riego suma según el caudal de cada actuador")
    void costoDeRiego() {
        VisitanteCostoRiego costo = new VisitanteCostoRiego();

        finca.aceptar(costo);

        // goteo 2 L/min + aspersor 15 L/min, 10 minutos cada uno
        assertEquals(170.0, costo.getLitros(), DELTA);
        assertEquals(2, costo.getLotesRegables());
    }

    @Test
    @DisplayName("un lote cuyo estado no admite riego queda fuera del costo")
    void elEstadoFiltraElCosto() {
        cafeNorte.madurar();
        cafeNorte.cosechar();                  // en barbecho
        VisitanteCostoRiego costo = new VisitanteCostoRiego();

        finca.aceptar(costo);

        assertEquals(150.0, costo.getLitros(), DELTA, "solo queda el aspersor");
        assertEquals(1, costo.getLotesOmitidos());
    }

    @Test
    @DisplayName("el CSV incluye una fila por zona y por lote, con cabecera")
    void exportarCsv() {
        VisitanteExportarCsv csv = new VisitanteExportarCsv();

        finca.aceptar(csv);

        assertEquals(5, csv.cuantasFilas(), "3 zonas + 2 lotes");
        assertTrue(csv.getResultado().startsWith("tipo,nombre,hectareas,sensores,estado"),
                csv.getResultado());
        assertTrue(csv.getResultado().contains("lote,Cafe-Norte,1.5,1,sembrado"),
                csv.getResultado());
    }

    @Test
    @DisplayName("la visita recorre el árbol completo, no solo la raíz")
    void recorreTodoElArbol() {
        VisitanteInventario inventario = new VisitanteInventario();

        finca.aceptar(inventario);

        assertEquals(finca.contarSensores(), inventario.getSensores(),
                "debe coincidir con lo que reporta el Composite");
        assertEquals(finca.getAreaHectareas(), inventario.getHectareas(), DELTA);
    }

    /**
     * VISITANTE CONCRETO escrito dentro de la prueba: el código de
     * producción no lo conoce.
     */
    private static class VisitanteNombres implements VisitanteFinca {
        private final StringBuilder nombres = new StringBuilder();

        @Override public void visitarZona(ZonaCultivo zona) { agregar(zona.getNombre()); }
        @Override public void visitarLote(Lote lote)        { agregar(lote.getNombre()); }
        @Override public String getResultado()              { return nombres.toString(); }

        private void agregar(String nombre) {
            if (nombres.length() > 0) nombres.append(" > ");
            nombres.append(nombre);
        }
    }

    @Test
    @DisplayName("un visitante nuevo funciona SIN tocar ZonaCultivo ni Lote")
    void admiteUnVisitanteNuevo() {
        VisitanteNombres nuevo = new VisitanteNombres();

        finca.aceptar(nuevo);

        assertEquals("Finca > Invernadero > Cafe-Norte > Campo abierto > Maiz-Este",
                nuevo.getResultado());
    }

    @Test
    @DisplayName("visitar una zona vacía no produce nada")
    void zonaVacia() {
        VisitanteInventario inventario = new VisitanteInventario();

        new ZonaCultivo("En barbecho").aceptar(inventario);

        assertEquals(1, inventario.getZonas());
        assertEquals(0, inventario.getLotes());
    }
}
