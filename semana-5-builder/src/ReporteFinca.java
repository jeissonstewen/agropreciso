import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PRODUCTO del patrón Builder.
 *
 * Un reporte de estado de la finca que se imprime por consola. Está hecho de
 * partes OPCIONALES: encabezado, lecturas de sensores, alertas, inventario y
 * resumen. Un reporte para el operario del riego puede llevar solo lecturas y
 * alertas; uno gerencial lleva todo.
 *
 * El objeto es inmutable: sus campos son final y se fijan una sola vez desde
 * el builder. Ninguna otra clase puede llamar al constructor (es de paquete),
 * así que la única forma de obtener un ReporteFinca es a través del builder.
 */
public class ReporteFinca {

    private final String titulo;
    private final String finca;
    private final String responsable;
    private final List<Lectura> lecturas;
    private final List<String>  alertas;
    private final List<String>  inventario;
    private final boolean       conResumen;

    /** Solo el builder construye reportes; por eso no es public. */
    ReporteFinca(String titulo, String finca, String responsable,
                 List<Lectura> lecturas, List<String> alertas,
                 List<String> inventario, boolean conResumen) {
        this.titulo      = titulo;
        this.finca       = finca;
        this.responsable = responsable;
        this.lecturas    = Collections.unmodifiableList(new ArrayList<>(lecturas));
        this.alertas     = Collections.unmodifiableList(new ArrayList<>(alertas));
        this.inventario  = Collections.unmodifiableList(new ArrayList<>(inventario));
        this.conResumen  = conResumen;
    }

    public String        getTitulo()     { return titulo; }
    public List<Lectura> getLecturas()   { return lecturas; }
    public List<String>  getAlertas()    { return alertas; }
    public List<String>  getInventario() { return inventario; }

    /** Cuántas secciones lleva este reporte (sirve para las pruebas). */
    public int contarSecciones() {
        int n = 1; // el título siempre está
        if (finca != null)          n++;
        if (!lecturas.isEmpty())    n++;
        if (!alertas.isEmpty())     n++;
        if (!inventario.isEmpty())  n++;
        if (conResumen)             n++;
        return n;
    }

    /** Imprime solo las secciones que el builder decidió incluir. */
    public void imprimir() {
        String linea = "====================================================";
        System.out.println(linea);
        System.out.println("  " + titulo.toUpperCase());
        System.out.println(linea);

        if (finca != null) {
            System.out.println("Finca       : " + finca);
            System.out.println("Responsable : " + responsable);
            System.out.println();
        }
        if (!lecturas.isEmpty()) {
            System.out.println("--- Lecturas de sensores ---");
            for (Lectura l : lecturas) System.out.println("  " + l);
            System.out.println();
        }
        if (!alertas.isEmpty()) {
            System.out.println("--- Alertas ---");
            for (String a : alertas) System.out.println("  ! " + a);
            System.out.println();
        }
        if (!inventario.isEmpty()) {
            System.out.println("--- Inventario ---");
            for (String i : inventario) System.out.println("  - " + i);
            System.out.println();
        }
        if (conResumen) {
            System.out.println("--- Resumen ---");
            System.out.println("  Sensores leidos : " + lecturas.size());
            System.out.println("  Alertas activas : " + alertas.size());
            System.out.println("  Items inventario: " + inventario.size());
            System.out.println();
        }
        System.out.println("Secciones incluidas: " + contarSecciones());
        System.out.println(linea);
        System.out.println();
    }
}
