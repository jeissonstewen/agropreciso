/**
 * Umbrales propios de un lote (un cafetal tolera menos sequía que un maizal).
 *
 * Es un objeto MUTABLE y anidado dentro de Lote: por eso es el mejor lugar
 * para demostrar la diferencia entre copia superficial y copia profunda.
 * Si el clon compartiera este objeto con el prototipo, cambiar el umbral
 * de uno cambiaría el del otro.
 */
public class UmbralesLote {

    private double humedadMin;   // % por debajo del cual se riega
    private double temperaturaMax; // °C por encima de la cual se alerta

    public UmbralesLote(double humedadMin, double temperaturaMax) {
        this.humedadMin     = humedadMin;
        this.temperaturaMax = temperaturaMax;
    }

    /** Copia independiente, para la copia profunda de Lote. */
    public UmbralesLote copiar() {
        return new UmbralesLote(humedadMin, temperaturaMax);
    }

    public double getHumedadMin()     { return humedadMin; }
    public double getTemperaturaMax() { return temperaturaMax; }
    public void   setHumedadMin(double v)     { this.humedadMin = v; }
    public void   setTemperaturaMax(double v) { this.temperaturaMax = v; }

    @Override
    public String toString() {
        return String.format("humedad min %.1f %%, temperatura max %.1f C", humedadMin, temperaturaMax);
    }
}
