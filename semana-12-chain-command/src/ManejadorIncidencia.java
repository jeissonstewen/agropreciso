/**
 * PATRÓN CHAIN OF RESPONSIBILITY — el MANEJADOR abstracto.
 *
 * Cada eslabón guarda una referencia al siguiente y decide, mirando la
 * incidencia, si es de su competencia. Si lo es, la atiende y LA CADENA SE
 * DETIENE; si no, la pasa. Si nadie puede, el resultado lo dice.
 *
 * DIFERENCIA CON EL DECORATOR (semanas 8 y 9), que también encadena:
 * en el Decorator TODOS los eslabones actúan y el dato atraviesa la pila
 * entera. Aquí actúa UNO SOLO y el recorrido termina ahí.
 *
 * El emisor de la incidencia no sabe quién la atenderá —ni siquiera si
 * alguien lo hará—, y por eso la cadena se puede reordenar o ampliar sin
 * tocar a quien la dispara.
 *
 * Nótese que atender() DEVUELVE una orden en vez de ejecutarla: la cadena
 * decide quién se hace cargo y qué hay que hacer, pero no ejecuta nada.
 * De ejecutar se encarga el PanelDeControl (patrón Command).
 */
public abstract class ManejadorIncidencia {

    private final String rol;
    private ManejadorIncidencia siguiente;

    protected ManejadorIncidencia(String rol) {
        this.rol = rol;
    }

    /**
     * Engancha el siguiente eslabón y lo devuelve, para poder armar la
     * cadena encadenando llamadas:
     *
     *     operario.encadenar(agronomo).encadenar(administrador);
     *
     * Se conserva una referencia a 'operario', que es la cabeza por donde
     * entran todas las incidencias.
     */
    public ManejadorIncidencia encadenar(ManejadorIncidencia siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    /**
     * EL MÉTODO DEL PATRÓN. Es final a propósito: el recorrido de la cadena
     * es siempre el mismo y las subclases no deben alterarlo. Lo único que
     * cada una aporta es el criterio y la acción.
     */
    public final ResultadoAtencion manejar(Incidencia incidencia) {
        if (puedeAtender(incidencia)) {
            System.out.println("  [" + rol + "] me hago cargo: " + incidencia);
            return ResultadoAtencion.atendida(rol, atender(incidencia));
        }
        System.out.println("  [" + rol + "] no es de mi competencia, escalo");
        if (siguiente != null) {
            return siguiente.manejar(incidencia);
        }
        System.out.println("  [CADENA] nadie pudo atender: " + incidencia);
        return ResultadoAtencion.sinAtender();
    }

    public String getRol() { return rol; }

    /** ¿Es esta incidencia de mi competencia? */
    protected abstract boolean puedeAtender(Incidencia incidencia);

    /** Qué hay que hacer. Devuelve la orden; no la ejecuta. */
    protected abstract Orden atender(Incidencia incidencia);
}
