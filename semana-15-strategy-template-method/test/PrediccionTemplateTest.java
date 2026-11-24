import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón TEMPLATE METHOD en el módulo de predicción.
 *
 * Lo que se verifica es el esqueleto, no las fórmulas: que todos los
 * modelos ejecuten los mismos cuatro pasos en el mismo orden, que ninguno
 * pueda saltarse los pasos fijos, y que un modelo nuevo solo tenga que
 * rellenar el hueco abstracto.
 */
@DisplayName("Template Method: modelos de predicción")
class PrediccionTemplateTest {

    /** Cuatro cosechas que crecen 1.5 t cada vez: promedio 10.25, tendencia clara. */
    private static final double[] HISTORICO = { 8.0, 9.5, 11.0, 12.5 };

    private static final double TOLERANCIA = 0.001;

    private Lote lote() {
        return new Lote("Maiz-Sur", "Maiz", 4.0, new UmbralesLote(40.0, 32.0));
    }

    // --- el esqueleto ---

    @Test
    @DisplayName("LA PRUEBA DEL PATRÓN: los tres modelos ejecutan los mismos cuatro pasos en el mismo orden")
    void elEsqueletoEsElMismo() {
        ModeloPrediccion[] modelos = {
                new PrediccionPromedioMovil(3),
                new PrediccionTendenciaLineal(),
                new PrediccionPorEstacion(new ClimaFijo("EST", 25.0, 45.0), 1.10)
        };
        for (ModeloPrediccion modelo : modelos) {
            List<String> traza = modelo.estimar(lote(), HISTORICO).getTraza();
            assertEquals(4, traza.size(), "todo modelo ejecuta cuatro pasos");
            assertTrue(traza.get(0).startsWith("1. Validacion"),   () -> traza.toString());
            assertTrue(traza.get(1).startsWith("2. Base"),         () -> traza.toString());
            assertTrue(traza.get(2).startsWith("3. Ajuste"),       () -> traza.toString());
            assertTrue(traza.get(3).startsWith("4. Merma"),        () -> traza.toString());
        }
    }

    @Test
    @DisplayName("el método plantilla es final: ninguna subclase puede cambiar el orden")
    void laPlantillaEsFinal() throws NoSuchMethodException {
        Method estimar = ModeloPrediccion.class.getDeclaredMethod("estimar", Lote.class, double[].class);
        assertTrue(Modifier.isFinal(estimar.getModifiers()),
                "estimar() debe ser final para que el esqueleto no se pueda alterar");
    }

    @Test
    @DisplayName("los pasos invariantes son privados: no se pueden sobreescribir")
    void losPasosFijosSonPrivados() throws NoSuchMethodException {
        Method validar = ModeloPrediccion.class.getDeclaredMethod("validarEntrada", Lote.class, double[].class);
        Method merma   = ModeloPrediccion.class.getDeclaredMethod("aplicarMerma", double.class);
        assertTrue(Modifier.isPrivate(validar.getModifiers()));
        assertTrue(Modifier.isPrivate(merma.getModifiers()));
    }

    @Test
    @DisplayName("la merma del 8 % se aplica siempre, en todos los modelos")
    void lamermaSeAplicaSiempre() {
        ModeloPrediccion[] modelos = {
                new PrediccionPromedioMovil(3),
                new PrediccionTendenciaLineal(),
                new PrediccionPorEstacion(new ClimaFijo("EST", 25.0, 45.0), 1.10),
                new ModeloMaximo()
        };
        for (ModeloPrediccion modelo : modelos) {
            Prediccion p = modelo.estimar(lote(), HISTORICO);
            assertEquals(p.getToneladasAjustadas() * 0.92, p.getToneladasNetas(), TOLERANCIA,
                    "la neta debe ser el 92 % de la ajustada en " + modelo.getNombre());
        }
    }

    // --- el paso abstracto: cada modelo lo rellena distinto ---

    @Test
    @DisplayName("promedio móvil de 3: promedia solo las tres últimas cosechas")
    void promedioMovil() {
        // (9.5 + 11.0 + 12.5) / 3 = 11.0
        assertEquals(11.0, new PrediccionPromedioMovil(3).estimar(lote(), HISTORICO).getToneladasBase(),
                TOLERANCIA);
    }

    @Test
    @DisplayName("promedio móvil con ventana mayor que el histórico usa todo el histórico")
    void ventanaMayorQueElHistorico() {
        // (8.0 + 9.5 + 11.0 + 12.5) / 4 = 10.25
        assertEquals(10.25, new PrediccionPromedioMovil(10).estimar(lote(), HISTORICO).getToneladasBase(),
                TOLERANCIA);
    }

    @Test
    @DisplayName("tendencia lineal: proyecta el crecimiento y supera al promedio")
    void tendenciaLineal() {
        double tendencia = new PrediccionTendenciaLineal().estimar(lote(), HISTORICO).getToneladasBase();
        double promedio  = new PrediccionPromedioMovil(3).estimar(lote(), HISTORICO).getToneladasBase();
        // La serie crece 1.5 t por cosecha: la siguiente proyectada es 14.0.
        assertEquals(14.0, tendencia, TOLERANCIA);
        assertTrue(tendencia > promedio, "con serie creciente la tendencia debe superar al promedio");
    }

    @Test
    @DisplayName("tendencia lineal: una serie decreciente nunca proyecta una cosecha negativa")
    void tendenciaNoNegativa() {
        double[] caida = { 10.0, 6.0, 2.0 };            // la recta proyectaría -2.0
        assertEquals(0.0, new PrediccionTendenciaLineal().estimar(lote(), caida).getToneladasBase(),
                TOLERANCIA);
    }

    @Test
    @DisplayName("con una sola cosecha la tendencia devuelve ese valor y no divide por cero")
    void historicoDeUnSoloDato() {
        assertEquals(9.0, new PrediccionTendenciaLineal().estimar(lote(), new double[]{9.0}).getToneladasBase(),
                TOLERANCIA);
    }

    // --- el gancho (hook) ---

    @Test
    @DisplayName("el gancho por defecto no cambia nada: base y ajustada son iguales")
    void ganchoPorDefecto() {
        Prediccion p = new PrediccionPromedioMovil(3).estimar(lote(), HISTORICO);
        assertEquals(p.getToneladasBase(), p.getToneladasAjustadas(), TOLERANCIA);
        assertTrue(p.getTraza().get(2).contains("sin cambio"), () -> p.getTraza().toString());
    }

    @Test
    @DisplayName("el gancho sobreescrito castiga la sequía y premia la temporada húmeda")
    void ganchoSobreescrito() {
        double base = 10.25 * 1.10;                     // 11.275
        Prediccion seco   = new PrediccionPorEstacion(new ClimaFijo("SECA", 31.0, 30.0), 1.10)
                .estimar(lote(), HISTORICO);
        Prediccion humedo = new PrediccionPorEstacion(new ClimaFijo("HUMEDA", 24.0, 70.0), 1.10)
                .estimar(lote(), HISTORICO);

        assertEquals(base * 0.90, seco.getToneladasAjustadas(), TOLERANCIA);
        assertEquals(base * 1.05, humedo.getToneladasAjustadas(), TOLERANCIA);
        assertTrue(seco.getToneladasNetas() < humedo.getToneladasNetas());
    }

    @Test
    @DisplayName("un clima neutro deja la base intacta aunque el gancho esté sobreescrito")
    void ganchoNeutro() {
        Prediccion p = new PrediccionPorEstacion(new ClimaFijo("EST", 25.0, 45.0), 1.00)
                .estimar(lote(), HISTORICO);
        assertEquals(p.getToneladasBase(), p.getToneladasAjustadas(), TOLERANCIA);
    }

    // --- los pasos fijos protegen al algoritmo ---

    @Test
    @DisplayName("la validación rechaza un histórico vacío o nulo en cualquier modelo")
    void historicoInvalido() {
        ModeloPrediccion modelo = new PrediccionPromedioMovil(3);
        Lote lote = lote();
        assertThrows(IllegalArgumentException.class, () -> modelo.estimar(lote, new double[0]));
        assertThrows(IllegalArgumentException.class, () -> modelo.estimar(lote, null));
    }

    @Test
    @DisplayName("la validación rechaza una cosecha negativa y lo dice en el mensaje")
    void cosechaNegativa() {
        ModeloPrediccion modelo = new PrediccionTendenciaLineal();
        Lote lote = lote();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> modelo.estimar(lote, new double[]{8.0, -2.0}));
        assertTrue(e.getMessage().contains("-2.0"), e::getMessage);
    }

    @Test
    @DisplayName("la validación exige un lote, y el modelo nuevo la hereda sin escribirla")
    void sinLote() {
        assertThrows(IllegalArgumentException.class, () -> new ModeloMaximo().estimar(null, HISTORICO));
    }

    @Test
    @DisplayName("un modelo escrito en la prueba solo define el paso 2 y hereda los otros tres")
    void modeloNuevo() {
        Prediccion p = new ModeloMaximo().estimar(lote(), HISTORICO);
        assertEquals(12.5, p.getToneladasBase(), TOLERANCIA);       // la mejor cosecha histórica
        assertEquals(11.5, p.getToneladasNetas(), TOLERANCIA);      // 12.5 x 0.92
        assertEquals(4, p.getTraza().size());                        // el mismo esqueleto
    }

    @Test
    @DisplayName("la ventana del promedio móvil debe ser de al menos una cosecha")
    void ventanaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new PrediccionPromedioMovil(0));
    }

    @Test
    @DisplayName("el modelo por estación exige un proveedor de clima")
    void modeloSinClima() {
        assertThrows(IllegalArgumentException.class, () -> new PrediccionPorEstacion(null, 1.0));
    }

    @Test
    @DisplayName("la traza es inmutable: el resultado no se puede retocar")
    void trazaInmutable() {
        List<String> traza = new PrediccionPromedioMovil(3).estimar(lote(), HISTORICO).getTraza();
        assertThrows(UnsupportedOperationException.class, () -> traza.add("5. paso inventado"));
    }

    /**
     * MODELO NUEVO definido en la prueba: el código de producción no lo
     * conoce. Son seis líneas y hereda validación, gancho y merma.
     */
    static class ModeloMaximo extends ModeloPrediccion {

        @Override
        protected double calcularBase(double[] historico) {
            double maximo = 0;
            for (double t : historico) maximo = Math.max(maximo, t);
            return maximo;
        }

        @Override
        public String getNombre() { return "MAXIMO HISTORICO"; }
    }
}
