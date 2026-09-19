import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN PROTOTYPE — la clase PROTOTIPO.
 *
 * Un lote de cultivo ya equipado: cultivo, área, umbrales propios, sensores
 * instalados y actuador de riego. Armarlo desde cero exige pasar por la
 * fábrica de equipamiento y fijar cada umbral; como una finca tiene muchos
 * lotes casi iguales, se arma UNO como plantilla y los demás se CLONAN.
 *
 * Implementa Cloneable y sobreescribe clone(). super.clone() hace una copia
 * SUPERFICIAL: los campos primitivos y String se copian bien, pero los
 * objetos anidados (umbrales, lista de sensores) quedarían COMPARTIDOS entre
 * el prototipo y el clon. Por eso, después de super.clone(), se copian a
 * mano las partes mutables: eso es la copia PROFUNDA.
 */
public class Lote implements Cloneable {

    private String        nombre;
    private final String  cultivo;
    private double        areaHectareas;
    private UmbralesLote  umbrales;   // mutable            -> copia profunda
    private List<Sensor>  sensores;   // lista mutable      -> lista nueva
    private ActuadorRiego actuador;   // sin estado mutable -> se comparte

    public Lote(String nombre, String cultivo, double areaHectareas, UmbralesLote umbrales) {
        this.nombre        = nombre;
        this.cultivo       = cultivo;
        this.areaHectareas = areaHectareas;
        this.umbrales      = umbrales;
        this.sensores      = new ArrayList<>();
    }

    /** Equipa el lote con una familia completa de dispositivos (Abstract Factory). */
    public void equipar(FabricaEquipamiento fabrica) {
        EstacionMonitoreo estacion = new EstacionMonitoreo(fabrica, cultivo.toUpperCase());
        this.sensores = new ArrayList<>(estacion.getSensores());
        this.actuador = estacion.getActuador();
    }

    /**
     * EL MÉTODO DEL PATRÓN. Devuelve un lote independiente del original.
     * Devolver Lote (y no Object) evita el cast en el cliente.
     *
     * Criterio de la copia profunda: se duplica todo lo que tenga estado
     * mutable (umbrales y la lista de sensores). Los Sensor y el
     * ActuadorRiego no tienen estado que cambie después de creados, así
     * que compartir esas instancias es seguro y evita objetos de más.
     */
    @Override
    public Lote clone() {
        try {
            Lote copia = (Lote) super.clone();          // 1. copia superficial
            copia.umbrales = this.umbrales.copiar();    // 2. objeto mutable: copia aparte
            copia.sensores = new ArrayList<>(this.sensores); // 3. lista nueva (misma instancia de cada sensor)
            return copia;
        } catch (CloneNotSupportedException e) {
            // Imposible: la clase implementa Cloneable.
            throw new AssertionError("Lote debe ser clonable", e);
        }
    }

    // --- operaciones del lote ---

    public void agregarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    /** Lee los sensores y riega si la humedad está por debajo del umbral propio. */
    public void monitorear() {
        System.out.println("Lote " + nombre + " (" + cultivo + ", " + areaHectareas + " ha) | "
                + umbrales);
        boolean regar = false;
        for (Sensor s : sensores) {
            Lectura l = s.leer();
            System.out.println("  " + l);
            if (l.getTipo().equals("HUMEDAD") && l.getValor() < umbrales.getHumedadMin()) regar = true;
            if (l.getTipo().equals("TEMPERATURA") && l.getValor() > umbrales.getTemperaturaMax()) {
                System.out.println("  ! Temperatura por encima del maximo del lote");
            }
        }
        if (regar && actuador != null) {
            System.out.print("  ");
            actuador.regar(10);
        } else {
            System.out.println("  Humedad suficiente: no se riega");
        }
    }

    // --- getters y setters ---

    public String        getNombre()        { return nombre; }
    public String        getCultivo()       { return cultivo; }
    public double        getAreaHectareas() { return areaHectareas; }
    public UmbralesLote  getUmbrales()      { return umbrales; }
    public List<Sensor>  getSensores()      { return sensores; }
    public ActuadorRiego getActuador()      { return actuador; }

    public void setNombre(String nombre)        { this.nombre = nombre; }
    public void setAreaHectareas(double area)   { this.areaHectareas = area; }

    @Override
    public String toString() {
        return String.format("%-12s %-6s %5.1f ha  %d sensores  riego %s  [%s]",
                nombre, cultivo, areaHectareas, sensores.size(),
                actuador == null ? "sin equipar" : actuador.getTipo(), umbrales);
    }
}
