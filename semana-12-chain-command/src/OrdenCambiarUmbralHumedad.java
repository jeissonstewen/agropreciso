import java.util.Objects;

/**
 * ORDEN CONCRETA: ajusta el umbral de humedad de un lote.
 *
 * Es el caso de orden REVERSIBLE. Al ejecutarse guarda el valor anterior,
 * y deshacer() lo restaura. Guardar ese valor es lo mínimo que necesita un
 * Command para revertirse; cuando el estado a restaurar es grande o
 * complejo, el patrón indicado para capturarlo es Memento.
 */
public class OrdenCambiarUmbralHumedad implements Orden {

    private final UmbralesLote umbrales;   // el receptor
    private final String nombreLote;       // solo para el texto del historial
    private final double valorNuevo;

    private double valorAnterior;
    private boolean ejecutada = false;

    public OrdenCambiarUmbralHumedad(Lote lote, double valorNuevo) {
        Objects.requireNonNull(lote, "La orden necesita un lote");
        if (valorNuevo < 0 || valorNuevo > 100) {
            throw new IllegalArgumentException("El umbral de humedad va de 0 a 100 %");
        }
        this.umbrales   = lote.getUmbrales();
        this.nombreLote = lote.getNombre();
        this.valorNuevo = valorNuevo;
    }

    @Override
    public void ejecutar() {
        valorAnterior = umbrales.getHumedadMin();     // ← lo que permite deshacer
        umbrales.setHumedadMin(valorNuevo);
        ejecutada = true;
        System.out.printf("    [UMBRAL] %s: humedad minima %.1f %% -> %.1f %%%n",
                nombreLote, valorAnterior, valorNuevo);
    }

    @Override
    public void deshacer() {
        if (!ejecutada) {
            throw new IllegalStateException("No se puede deshacer una orden que no se ejecuto");
        }
        umbrales.setHumedadMin(valorAnterior);
        ejecutada = false;
        System.out.printf("    [DESHACER] %s: humedad minima vuelve a %.1f %%%n",
                nombreLote, valorAnterior);
    }

    @Override
    public boolean esReversible() { return true; }

    @Override
    public String getDescripcion() {
        return String.format("Cambiar umbral de humedad de %s a %.1f %%", nombreLote, valorNuevo);
    }
}
