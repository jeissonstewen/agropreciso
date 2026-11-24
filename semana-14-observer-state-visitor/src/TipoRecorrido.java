/* ---------------------------------------------------------------
 * COPIA de la Semana 13 (patrón Iterator).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-13-iterator-mediator/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * Los recorridos disponibles sobre el árbol de la finca.
 *
 * Que exista este enum es, en sí mismo, la idea del patrón Iterator: el
 * recorrido es algo que se ELIGE, no algo que la estructura impone.
 */
public enum TipoRecorrido {

    /** Baja hasta las hojas de una rama antes de pasar a la siguiente. */
    PROFUNDIDAD,

    /** Visita zona por zona, nivel a nivel, de arriba hacia abajo. */
    NIVELES
}
