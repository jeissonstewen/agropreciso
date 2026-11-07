/**
 * Lo que un colega le comunica al centro de operaciones.
 *
 * Nótese que son HECHOS ("esto pasó"), no órdenes ("haz esto"). El colega
 * informa; quien decide qué hacer a continuación es el mediador.
 */
public enum Evento {

    /** Una lectura salió del rango aceptable para su lote. */
    LECTURA_ANOMALA,

    /** La cadena de incidencias produjo una orden. */
    ORDEN_GENERADA,

    /** El panel terminó de ejecutar una orden. */
    ORDEN_EJECUTADA,

    /** Ningún responsable de la cadena pudo hacerse cargo. */
    INCIDENCIA_SIN_ATENDER
}
