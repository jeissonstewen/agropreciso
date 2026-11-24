/**
 * OBSERVADOR CONCRETO: muestra las alertas por consola.
 * Es el suscriptor más simple: solo imprime.
 */
public class PanelDeAlertas implements ObservadorAlerta {

    @Override
    public void alPublicarse(Alerta alerta) {
        System.out.println("    [PANEL] " + alerta);
    }

    @Override
    public String getNombre() { return "PanelDeAlertas"; }
}
