/**
 * MODELO CONCRETO — por estación, y el único que usa el GANCHO.
 *
 * Hace dos cosas:
 *
 *   1. Rellena el paso OBLIGATORIO `calcularBase()`: promedio del
 *      histórico corregido por un factor de estación (una cosecha de
 *      verano no rinde como una de invierno).
 *
 *   2. Sobreescribe el GANCHO `ajustarPorClima()`, que los otros dos
 *      modelos heredan sin tocar. Consulta el ProveedorClima —el ADAPTER
 *      de la Semana 7— y castiga la estimación si el ambiente está seco.
 *
 * Esa es la prueba de que el gancho es opcional: tres modelos, uno solo
 * lo implementa, y la plantilla funciona igual con los tres.
 */
public class PrediccionPorEstacion extends ModeloPrediccion {

    /** Humedad ambiente por debajo de la cual hay estrés hídrico. */
    private static final double HUMEDAD_BAJA = 35.0;

    /** Humedad ambiente por encima de la cual la temporada ayuda. */
    private static final double HUMEDAD_ALTA = 60.0;

    private final ProveedorClima clima;
    private final double factorEstacional;

    public PrediccionPorEstacion(ProveedorClima clima, double factorEstacional) {
        if (clima == null) throw new IllegalArgumentException("Este modelo necesita un proveedor de clima");
        this.clima            = clima;
        this.factorEstacional = factorEstacional;
    }

    @Override
    protected double calcularBase(double[] historico) {
        return promedio(historico, 0) * factorEstacional;
    }

    /** GANCHO SOBREESCRITO: el clima real corrige la estimación. */
    @Override
    protected double ajustarPorClima(double base) {
        double humedadAmbiente = clima.obtenerHumedad().getValor();
        if (humedadAmbiente < HUMEDAD_BAJA) return base * 0.90;   // estrés hídrico
        if (humedadAmbiente >= HUMEDAD_ALTA) return base * 1.05;  // temporada favorable
        return base;
    }

    @Override
    public String getNombre() {
        return String.format("POR ESTACION x%.2f", factorEstacional);
    }
}
