import java.util.List;

/**
 * ABSTRACCIÓN REFINADA: solo las alertas activas.
 *
 * Su contenido es distinto al del reporte de estado —no lleva tabla, y si
 * no hay alertas lo dice— pero usa exactamente los mismos implementadores.
 */
public class ReporteAlertas extends Reporte {

    private final List<String> alertas;

    public ReporteAlertas(SalidaReporte salida, List<String> alertas) {
        super(salida);
        this.alertas = alertas;
    }

    @Override
    public void generar() {
        salida.escribirTitulo("Alertas activas");
        if (alertas.isEmpty()) {
            salida.escribirLinea("Sin alertas");
        } else {
            for (String alerta : alertas) {
                salida.escribirLinea(alerta);
            }
        }
        salida.cerrar();
    }

    @Override
    public String getNombre() { return "Reporte de alertas"; }
}
