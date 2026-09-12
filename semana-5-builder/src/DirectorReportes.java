import java.util.ArrayList;
import java.util.List;

/**
 * DIRECTOR del patrón Builder.
 *
 * Conoce las "recetas" de los reportes que la finca usa a diario y le dice
 * al builder, en orden, qué partes agregar. El Director NO sabe cómo se
 * arma cada parte ni qué clase concreta es el producto: solo habla con la
 * interfaz ReporteBuilder.
 *
 * Gracias a él, el código cliente pide "el reporte del operario" o "el
 * reporte gerencial" sin repetir la secuencia de pasos en cada sitio.
 */
public class DirectorReportes {

    private final ReporteBuilder builder;

    public DirectorReportes(ReporteBuilder builder) {
        this.builder = builder;
    }

    /** Receta corta: lo que necesita el operario de riego al empezar el turno. */
    public ReporteFinca reporteOperario(List<Lectura> lecturas) {
        return builder
                .conTitulo("Reporte de turno")
                .conLecturas(lecturas)
                .conAlertas(alertasDesde(lecturas))
                .construir();
    }

    /** Receta completa: todas las secciones, para la gerencia de la finca. */
    public ReporteFinca reporteGerencial(List<Lectura> lecturas, List<String> inventario) {
        ConfiguracionSistema cfg = ConfiguracionSistema.getInstancia();
        SesionUsuario sesion     = SesionUsuario.getInstancia();
        return builder
                .conTitulo("Reporte gerencial")
                .conEncabezado(cfg.getNombreFinca(), sesion.getUsuario())
                .conLecturas(lecturas)
                .conAlertas(alertasDesde(lecturas))
                .conInventario(inventario)
                .conResumen()
                .construir();
    }

    /**
     * Deriva las alertas comparando cada lectura con los umbrales globales
     * de la Semana 3 (ConfiguracionSistema). Las lecturas vienen de las
     * fabricas de la Semana 4.
     */
    private List<String> alertasDesde(List<Lectura> lecturas) {
        ConfiguracionSistema cfg = ConfiguracionSistema.getInstancia();
        List<String> alertas = new ArrayList<>();
        for (Lectura l : lecturas) {
            if (l.getTipo().equals("HUMEDAD") && l.getValor() < cfg.getUmbralHumedadMin()) {
                alertas.add(String.format("Humedad baja en %s: %.1f %% (umbral %.1f %%)",
                        l.getIdSensor(), l.getValor(), cfg.getUmbralHumedadMin()));
            }
            if (l.getTipo().equals("PH") && (l.getValor() < 5.5 || l.getValor() > 7.5)) {
                alertas.add(String.format("pH fuera de rango en %s: %.1f",
                        l.getIdSensor(), l.getValor()));
            }
        }
        return alertas;
    }
}
