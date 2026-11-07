/**
 * COLEGA: ejecuta las órdenes usando el panel de control de la Semana 12.
 *
 * Tampoco conoce a nadie más. Recibe una orden del mediador, la ejecuta y
 * avisa de que terminó.
 */
public class OperacionesCampo extends Colega {

    private final PanelDeControl panel;

    public OperacionesCampo(Mediador mediador, PanelDeControl panel) {
        super(mediador, "OperacionesCampo");
        this.panel = panel;
    }

    /** Lo llama el mediador cuando hay una orden que ejecutar. */
    public void ejecutar(Orden orden) {
        panel.ejecutar(orden);
        avisar(Evento.ORDEN_EJECUTADA, orden);
    }

    public PanelDeControl getPanel() { return panel; }
}
