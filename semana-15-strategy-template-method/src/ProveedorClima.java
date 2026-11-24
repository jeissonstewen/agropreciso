/* ---------------------------------------------------------------
 * COPIA de la Semana 7 (patrones Adapter y Bridge).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-7-adapter-bridge/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * TARGET (la interfaz objetivo) del patrón Adapter.
 *
 * Es la forma en que NUESTRO sistema quiere consultar el clima: una
 * `Lectura` por magnitud, en °C y en %, igual que las que producen los
 * sensores propios. El código cliente (riego, reportes) programa contra
 * esta interfaz y no contra ningún proveedor concreto.
 */
public interface ProveedorClima {

    /** Temperatura del lote, en grados Celsius. */
    Lectura obtenerTemperatura();

    /** Humedad relativa del lote, en porcentaje de 0 a 100. */
    Lectura obtenerHumedad();

    /** Nombre del origen de los datos, para los reportes. */
    String getOrigen();
}
