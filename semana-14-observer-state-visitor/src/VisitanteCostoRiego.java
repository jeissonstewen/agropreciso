/**
 * VISITANTE CONCRETO: estima el gasto de una jornada de riego.
 *
 * Aquí se nota la integración con el patrón State: solo cuenta los lotes
 * que su estado admite regar. Un lote en barbecho no suma litros aunque
 * tenga actuador instalado.
 */
public class VisitanteCostoRiego implements VisitanteFinca {

    /** Minutos de riego que se asumen por lote en una jornada. */
    private static final int MINUTOS_POR_JORNADA = 10;

    /** Precio del agua, en pesos por litro. */
    private static final double PESOS_POR_LITRO = 12.0;

    private double litros = 0;
    private int lotesRegables = 0;
    private int lotesOmitidos = 0;

    @Override
    public void visitarZona(ZonaCultivo zona) {
        // Una zona no riega: solo agrupa.
    }

    @Override
    public void visitarLote(Lote lote) {
        if (!lote.admiteRiego() || lote.getActuador() == null) {
            lotesOmitidos++;
            return;
        }
        lotesRegables++;
        litros += caudalPorMinuto(lote.getActuador()) * MINUTOS_POR_JORNADA;
    }

    /** Caudal según el tipo de actuador instalado. */
    private double caudalPorMinuto(ActuadorRiego actuador) {
        return "GOTEO".equals(actuador.getTipo()) ? 2.0 : 15.0;
    }

    @Override
    public String getResultado() {
        return String.format("  Lotes regables: %d · omitidos por su estado: %d%n"
                           + "  Agua estimada : %.1f L · costo aproximado: $ %,.0f%n",
                lotesRegables, lotesOmitidos, litros, litros * PESOS_POR_LITRO);
    }

    public double getLitros()      { return litros; }
    public int getLotesRegables()  { return lotesRegables; }
    public int getLotesOmitidos()  { return lotesOmitidos; }
}
