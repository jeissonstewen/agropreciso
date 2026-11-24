import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN TEMPLATE METHOD — la CLASE ABSTRACTA con el método plantilla.
 *
 * Toda predicción de cosecha de la finca sigue el mismo procedimiento, y
 * ese procedimiento es una política de la empresa, no del modelo:
 *
 *   1. Validar el histórico       (igual para todos — paso fijo)
 *   2. Calcular la producción base(CADA MODELO LO HACE DISTINTO)
 *   3. Ajustar por clima          (opcional — gancho / hook)
 *   4. Aplicar la merma poscosecha(igual para todos — paso fijo)
 *
 * El método `estimar()` es FINAL: fija ese orden y nadie lo puede
 * cambiar. Una subclase no puede saltarse la validación, ni olvidarse de
 * la merma, ni invertir los pasos. Solo puede rellenar los huecos.
 *
 * Si en vez de esto cada modelo fuera una clase independiente, los cuatro
 * pasos se copiarían en cada una, y el día que la finca cambiara el
 * porcentaje de merma habría que acordarse de editarlas todas.
 *
 * DIFERENCIA CON STRATEGY (que esta misma semana implementa en el módulo
 * de riego, en EstrategiaRiego):
 *
 *   - Strategy usa COMPOSICIÓN: el objeto tiene una estrategia y la puede
 *     CAMBIAR en ejecución. Varía el algoritmo COMPLETO.
 *   - Template Method usa HERENCIA: el algoritmo se elige al INSTANCIAR
 *     y ya no cambia. Varían solo los PASOS que la plantilla deja
 *     abiertos; el esqueleto es inmutable.
 *
 * Dicho corto: Strategy cambia el QUÉ se hace; Template Method fija el
 * ORDEN y deja variar partes del CÓMO.
 */
public abstract class ModeloPrediccion {

    /**
     * Pérdida de poscosecha de la finca (manipulación, transporte,
     * selección). Es un dato de la empresa y por eso vive en la plantilla
     * y no en los modelos: si cambia, cambia en un solo lugar.
     */
    protected static final double MERMA_POSCOSECHA = 0.08;

    /**
     * MÉTODO PLANTILLA. Es `final` a propósito: define el esqueleto del
     * algoritmo y el orden de los pasos, y eso no se negocia.
     *
     * @param lote     lote sobre el que se estima
     * @param historico producción de las cosechas anteriores, en toneladas
     */
    public final Prediccion estimar(Lote lote, double[] historico) {
        List<String> traza = new ArrayList<>();

        // --- Paso 1: fijo, igual para todos los modelos ---
        validarEntrada(lote, historico);
        traza.add(String.format("1. Validacion: %d cosechas historicas de %s",
                historico.length, lote.getNombre()));

        // --- Paso 2: variable, cada modelo tiene su fórmula (abstracto) ---
        double base = calcularBase(historico);
        traza.add(String.format("2. Base (%s): %.2f t", getNombre(), base));

        // --- Paso 3: gancho (hook). Por defecto no hace nada ---
        double ajustada = ajustarPorClima(base);
        if (Math.abs(ajustada - base) < 0.0001) {
            // Puede ser que el modelo no sobreescriba el gancho, o que lo
            // sobreescriba y el clima resulte neutro: en los dos casos el
            // esqueleto siguió igual.
            traza.add(String.format("3. Ajuste por clima: sin cambio (%.2f t)", base));
        } else {
            traza.add(String.format("3. Ajuste por clima: %.2f t -> %.2f t", base, ajustada));
        }

        // --- Paso 4: fijo, política de la finca ---
        double neta = aplicarMerma(ajustada);
        traza.add(String.format("4. Merma poscosecha %.0f %%: %.2f t -> %.2f t",
                MERMA_POSCOSECHA * 100, ajustada, neta));

        return new Prediccion(getNombre(), lote.getNombre(), base, ajustada, neta, traza);
    }

    // ----------------------------------------------------------------
    //  Pasos que la subclase DEBE rellenar
    // ----------------------------------------------------------------

    /**
     * PASO ABSTRACTO: la producción esperada según el histórico.
     * Es el único punto que todos los modelos están obligados a definir,
     * y es justamente lo que diferencia a un modelo de otro.
     */
    protected abstract double calcularBase(double[] historico);

    /** Nombre del modelo, para la traza y los reportes. */
    public abstract String getNombre();

    // ----------------------------------------------------------------
    //  Gancho (hook): la subclase PUEDE rellenarlo
    // ----------------------------------------------------------------

    /**
     * GANCHO con implementación por defecto: no ajusta nada.
     *
     * Un gancho se distingue de un paso abstracto en que la subclase no
     * está obligada a sobreescribirlo. Aquí solo lo sobreescribe
     * PrediccionPorEstacion, que es la única que consulta el clima; las
     * otras dos lo heredan y no escriben una línea.
     */
    protected double ajustarPorClima(double base) {
        return base;
    }

    // ----------------------------------------------------------------
    //  Pasos invariantes: privados para que nadie los pueda cambiar
    // ----------------------------------------------------------------

    /**
     * Validación común. Es `private`, así que una subclase no puede
     * sobreescribirla para relajarla.
     */
    private void validarEntrada(Lote lote, double[] historico) {
        if (lote == null) {
            throw new IllegalArgumentException("Se necesita un lote para estimar la cosecha");
        }
        if (historico == null || historico.length == 0) {
            throw new IllegalArgumentException("El historico de cosechas esta vacio");
        }
        for (double t : historico) {
            if (t < 0) {
                throw new IllegalArgumentException("El historico tiene una cosecha negativa: " + t);
            }
        }
    }

    /** Descuento invariante de poscosecha. También privado. */
    private double aplicarMerma(double toneladas) {
        return toneladas * (1 - MERMA_POSCOSECHA);
    }

    // --- utilidad compartida por las subclases ---

    /** Promedio de un tramo del histórico; la usan dos de los tres modelos. */
    protected static double promedio(double[] valores, int desde) {
        double suma = 0;
        for (int i = desde; i < valores.length; i++) suma += valores[i];
        return suma / (valores.length - desde);
    }
}
