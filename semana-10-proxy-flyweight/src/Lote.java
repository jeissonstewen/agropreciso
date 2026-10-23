/* ---------------------------------------------------------------
 * COPIA de la Semana 8 (patrones Composite y Decorator).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-8-composite-decorator/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
import java.util.ArrayList;
import java.util.List;

/**
 * HOJA del patrón Composite: un lote de cultivo concreto.
 *
 * Es el elemento que sí hace el trabajo real —tiene área, sensores y un
 * actuador de riego— mientras que las zonas solo agrupan y combinan. No
 * tiene hijos, y por eso sus operaciones no son recursivas.
 */
public class Lote implements ComponenteFinca {

    private final String  nombre;
    private final String  cultivo;
    private final double  areaHectareas;
    private final UmbralesLote  umbrales;
    private final List<Sensor>  sensores = new ArrayList<>();
    private ActuadorRiego actuador;

    public Lote(String nombre, String cultivo, double areaHectareas, UmbralesLote umbrales) {
        this.nombre        = nombre;
        this.cultivo       = cultivo;
        this.areaHectareas = areaHectareas;
        this.umbrales      = umbrales;
    }

    public Lote agregarSensor(Sensor sensor) {
        sensores.add(sensor);
        return this;                       // permite encadenar al armar el lote
    }

    public Lote setActuador(ActuadorRiego actuador) {
        this.actuador = actuador;
        return this;
    }

    // --- operaciones del componente: aquí NO hay recursión ---

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getAreaHectareas() { return areaHectareas; }

    @Override
    public int contarSensores() { return sensores.size(); }

    @Override
    public void monitorear() {
        System.out.println("  Lote " + nombre + " (" + cultivo + ", " + areaHectareas + " ha) | "
                + umbrales);
        boolean regar = false;
        for (Sensor s : sensores) {
            Lectura l = s.leer();
            System.out.println("    " + l);
            if (l.getTipo().equals("HUMEDAD") && l.getValor() < umbrales.getHumedadMin()) regar = true;
            if (l.getTipo().equals("TEMPERATURA") && l.getValor() > umbrales.getTemperaturaMax()) {
                System.out.println("    ! Temperatura por encima del maximo del lote");
            }
        }
        if (regar && actuador != null) {
            System.out.print("    ");
            actuador.regar(10);
        } else {
            System.out.println("    Humedad suficiente: no se riega");
        }
    }

    @Override
    public String describir(int nivel) {
        return ZonaCultivo.sangria(nivel)
                + String.format("- %s (%s)  [%.1f ha, %s, riego %s]%n",
                        nombre, cultivo, areaHectareas,
                        ZonaCultivo.contar(sensores.size(), "sensor", "sensores"),
                        actuador == null ? "sin equipar" : actuador.getTipo());
    }

    public List<Sensor> getSensores() { return sensores; }
    public UmbralesLote getUmbrales() { return umbrales; }
}
