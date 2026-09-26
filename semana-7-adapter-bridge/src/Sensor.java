/* ---------------------------------------------------------------
 * COPIA de la Semana 4 (patrón Factory Method).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-4-factory-method/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PRODUCTO del patrón Factory Method.
 *
 * Interfaz común a todos los sensores del sistema. El código cliente
 * (riego, reportes, predicción) trabaja siempre contra esta interfaz y
 * nunca contra las clases concretas.
 */
public interface Sensor {

    /** Toma una medición simulada del cultivo. */
    Lectura leer();

    /** Tipo de magnitud que mide: HUMEDAD, TEMPERATURA o PH. */
    String getTipo();
}
