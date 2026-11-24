/* ---------------------------------------------------------------
 * COPIA ADAPTADA de la Semana 14, que venía de la Semana 13, de la
 * Semana 8 (Composite) y originalmente de la Semana 6. Ninguna de
 * esas versiones fue modificada.
 *
 * Cambios para la Semana 15:
 *   - STRATEGY: el lote guarda una EstrategiaRiego y le pregunta
 *               cuántos minutos regar. Desaparece el `regar(10)` que
 *               estaba quemado desde la Semana 6.
 *   - Se quita aceptar(VisitanteFinca): el patrón Visitor es de la
 *     Semana 14 y esta semana no lo usa, así que no se arrastran sus
 *     clases. El State, en cambio, SÍ se conserva, porque es el que
 *     decide si corresponde regar antes de preguntar cuánto.
 * --------------------------------------------------------------- */
import java.util.ArrayList;
import java.util.List;

/**
 * HOJA del patrón Composite: un lote de cultivo concreto.
 *
 * Es el elemento que sí hace el trabajo real —tiene área, sensores y un
 * actuador de riego— mientras que las zonas solo agrupan y combinan. No
 * tiene hijos, y por eso sus operaciones no son recursivas.
 *
 * En esta semana es además el CONTEXTO del patrón Strategy: tiene una
 * política de riego, la puede cambiar en ejecución y no conoce ninguna
 * implementación concreta de esa política.
 */
public class Lote implements ComponenteFinca {

    private final String  nombre;
    private final String  cultivo;
    private final double  areaHectareas;
    private final UmbralesLote  umbrales;
    private final List<Sensor>  sensores = new ArrayList<>();
    private ActuadorRiego actuador;

    /** PATRÓN STATE (Semana 14): el lote delega en su estado actual. */
    private EstadoLote estado = new EnPreparacion();

    /**
     * PATRÓN STRATEGY: la política de riego, intercambiable en ejecución.
     * Arranca con la más conservadora, que es la que le conviene a la finca.
     */
    private EstrategiaRiego estrategiaRiego = new EstrategiaAhorroAgua();

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

    // --- patrón STRATEGY: cambiar la política sin tocar esta clase ---

    /**
     * Cambia la política de riego. Se puede llamar en cualquier momento,
     * incluso entre dos monitoreos: es la diferencia con el Template
     * Method, donde el algoritmo queda fijado al instanciar la clase.
     */
    public Lote setEstrategiaRiego(EstrategiaRiego estrategia) {
        if (estrategia == null) throw new IllegalArgumentException("La estrategia de riego no puede ser nula");
        this.estrategiaRiego = estrategia;
        return this;
    }

    public EstrategiaRiego getEstrategiaRiego() { return estrategiaRiego; }

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
        Lectura humedad = null;
        for (Sensor s : sensores) {
            Lectura l = s.leer();
            System.out.println("    " + l);
            if (l.getTipo().equals("HUMEDAD")) humedad = l;
            if (l.getTipo().equals("TEMPERATURA") && l.getValor() > umbrales.getTemperaturaMax()) {
                System.out.println("    ! Temperatura por encima del maximo del lote");
            }
        }

        // Tres decisiones, tres responsables distintos:
        if (!estado.admiteRiego()) {
            // 1. El ESTADO manda sobre la lectura: un lote en barbecho no se
            //    riega aunque el sensor diga que está seco.
            System.out.println("    Esta " + estado.getNombre() + ": no corresponde regar");
            return;
        }
        if (humedad == null || actuador == null) {
            System.out.println("    Sin sensor de humedad o sin actuador: no se puede decidir el riego");
            return;
        }

        // 2. La ESTRATEGIA decide cuánta agua.
        int minutos = estrategiaRiego.calcularMinutos(humedad, umbrales);
        if (minutos == 0) {
            System.out.printf("    Estrategia %s: no riega ahora%n", estrategiaRiego.getNombre());
        } else {
            // 3. El ACTUADOR solo aplica el agua.
            System.out.printf("    Estrategia %s -> %d min  ", estrategiaRiego.getNombre(), minutos);
            actuador.regar(minutos);
        }
    }

    @Override
    public String describir(int nivel) {
        return ZonaCultivo.sangria(nivel)
                + String.format("- %s (%s)  [%.1f ha, %s, riego %s] %s | %s%n",
                        nombre, cultivo, areaHectareas,
                        ZonaCultivo.contar(sensores.size(), "sensor", "sensores"),
                        actuador == null ? "sin equipar" : actuador.getTipo(),
                        estado.getNombre(), estrategiaRiego.getNombre());
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

    public List<Sensor> getSensores()   { return sensores; }
    public ActuadorRiego getActuador()  { return actuador; }
    public UmbralesLote getUmbrales()   { return umbrales; }
    public String getCultivo()          { return cultivo; }
}
