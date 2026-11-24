/**
 * MODELO CONCRETO — promedio móvil.
 *
 * Rellena el único paso obligatorio de la plantilla: la base es el
 * promedio de las últimas N cosechas. Es el modelo más conservador y el
 * que usa la finca por defecto.
 *
 * Nótese lo que esta clase NO tiene: validación, merma ni ajuste por
 * clima. Todo eso lo pone la plantilla. Son doce líneas de fórmula y nada
 * más, y es imposible que se olvide un paso.
 */
public class PrediccionPromedioMovil extends ModeloPrediccion {

    private final int ventana;

    /** @param ventana cuántas cosechas recientes promediar */
    public PrediccionPromedioMovil(int ventana) {
        if (ventana < 1) throw new IllegalArgumentException("La ventana debe ser de al menos 1 cosecha");
        this.ventana = ventana;
    }

    @Override
    protected double calcularBase(double[] historico) {
        int desde = Math.max(0, historico.length - ventana);
        return promedio(historico, desde);
    }

    @Override
    public String getNombre() { return "PROMEDIO MOVIL " + ventana; }
}
