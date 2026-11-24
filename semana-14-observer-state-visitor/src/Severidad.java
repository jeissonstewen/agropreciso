/* ---------------------------------------------------------------
 * COPIA de la Semana 12 (patrón Chain of Responsibility).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-12-chain-command/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * Gravedad de una incidencia. Es lo que cada eslabón de la cadena mira
 * para decidir si la incidencia es de su competencia.
 */
public enum Severidad {
    LEVE,
    MEDIA,
    CRITICA
}
