/**
 * MODELO CONCRETO — tendencia lineal.
 *
 * Ajusta una recta por mínimos cuadrados sobre todo el histórico y
 * proyecta la cosecha siguiente. A diferencia del promedio móvil, SÍ nota
 * si la finca viene mejorando o cayendo año tras año.
 *
 * Rellena exactamente el mismo hueco que el otro modelo, con una fórmula
 * completamente distinta, y la plantilla no se enteró.
 */
public class PrediccionTendenciaLineal extends ModeloPrediccion {

    @Override
    protected double calcularBase(double[] historico) {
        int n = historico.length;
        if (n == 1) return historico[0];       // con un solo dato no hay tendencia

        // Mínimos cuadrados con x = 0, 1, 2, ... n-1
        double sumaX = 0, sumaY = 0, sumaXY = 0, sumaXX = 0;
        for (int i = 0; i < n; i++) {
            sumaX  += i;
            sumaY  += historico[i];
            sumaXY += i * historico[i];
            sumaXX += (double) i * i;
        }
        double pendiente   = (n * sumaXY - sumaX * sumaY) / (n * sumaXX - sumaX * sumaX);
        double intercepto  = (sumaY - pendiente * sumaX) / n;

        double proyeccion = intercepto + pendiente * n;   // la cosecha siguiente
        return Math.max(0, proyeccion);                   // una cosecha no es negativa
    }

    @Override
    public String getNombre() { return "TENDENCIA LINEAL"; }
}
