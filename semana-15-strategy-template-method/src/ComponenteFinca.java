/* ---------------------------------------------------------------
 * COPIA de la Semana 8 (patrones Composite y Decorator).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-8-composite-decorator/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PATRÓN COMPOSITE — el COMPONENTE.
 *
 * Interfaz común a las dos cosas que forman la estructura de la finca: un
 * lote individual (la HOJA) y una zona que agrupa otros elementos (el
 * COMPUESTO). Gracias a ella, el cliente pregunta el área o manda monitorear
 * exactamente igual a un lote suelto que a la finca entera.
 *
 * Nótese que NO declara agregar() ni quitar(): esa es la variante de
 * SEGURIDAD del patrón. La gestión de hijos vive solo en ZonaCultivo, así
 * que el compilador impide agregarle un hijo a un lote. La alternativa
 * —declararlos aquí, la variante de TRANSPARENCIA— haría que todo se tratara
 * igual, pero obligaría a las hojas a rechazar esas llamadas en ejecución.
 */
public interface ComponenteFinca {

    String getNombre();

    /** Área propia si es una hoja; suma de los hijos si es un compuesto. */
    double getAreaHectareas();

    /** Sensores propios si es una hoja; total recursivo si es un compuesto. */
    int contarSensores();

    /** Lee los sensores y riega si hace falta; en un compuesto, en cascada. */
    void monitorear();

    /**
     * Representación en texto del árbol, con sangría según la profundidad.
     * @param nivel 0 para la raíz; cada hijo recibe nivel + 1
     */
    String describir(int nivel);
}
