/* ---------------------------------------------------------------
 * COPIA ADAPTADA de la Semana 13, que venía de la Semana 8
 * (Composite) y originalmente de la Semana 6. Ninguna de esas
 * versiones fue modificada.
 *
 * Cambios para la Semana 14:
 *   - VISITOR : se agrega aceptar(VisitanteFinca).
 *   - STATE   : el lote pasa a ser el CONTEXTO. Guarda un EstadoLote,
 *               delega en él sembrar/madurar/cosechar/prepararSuelo,
 *               y monitorear() respeta admiteRiego().
 *   - describir() muestra además el estado actual.
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

    /** PATRÓN STATE: el lote es el CONTEXTO y delega en su estado actual. */
    private EstadoLote estado = new EnPreparacion();

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
        if (!estado.admiteRiego()) {
            // El estado manda sobre la lectura: un lote en barbecho no se riega
            // aunque el sensor diga que está seco.
            System.out.println("    Esta " + estado.getNombre() + ": no corresponde regar");
        } else if (regar && actuador != null) {
            System.out.print("    ");
            actuador.regar(10);
        } else {
            System.out.println("    Humedad suficiente: no se riega");
        }
    }

    @Override
    public String describir(int nivel) {
        return ZonaCultivo.sangria(nivel)
                + String.format("- %s (%s)  [%.1f ha, %s, riego %s] %s%n",
                        nombre, cultivo, areaHectareas,
                        ZonaCultivo.contar(sensores.size(), "sensor", "sensores"),
                        actuador == null ? "sin equipar" : actuador.getTipo(),
                        estado.getNombre());
    }

    // --- patrón VISITOR ---

    /** Una hoja no propaga nada: solo se deja visitar. */
    @Override
    public void aceptar(VisitanteFinca visitante) {
        visitante.visitarLote(this);
    }

    // --- patrón STATE: el contexto delega en su estado ---

    public void sembrar()       { estado.sembrar(this); }
    public void madurar()       { estado.madurar(this); }
    public void cosechar()      { estado.cosechar(this); }
    public void prepararSuelo() { estado.prepararSuelo(this); }

    /** ¿El estado actual admite riego? No dice cuánto: eso es de Strategy. */
    public boolean admiteRiego() { return estado.admiteRiego(); }

    public EstadoLote getEstado() { return estado; }

    /**
     * Solo los estados cambian el estado: por eso es de paquete y no
     * público. El cliente provoca transiciones llamando a sembrar(),
     * madurar(), cosechar() o prepararSuelo().
     */
    void cambiarEstado(EstadoLote nuevo) {
        this.estado = nuevo;
    }

    // --- getters ---

    public List<Sensor> getSensores() { return sensores; }

    /** Restaurado para la Semana 12: el receptor de una OrdenRegar. */
    public ActuadorRiego getActuador() { return actuador; }
    public UmbralesLote getUmbrales() { return umbrales; }
}
