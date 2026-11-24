/**
 * PATRÓN STATE — la interfaz del ESTADO.
 *
 * Un lote pasa por un ciclo —preparación, siembra, maduración, cosecha,
 * barbecho— y lo que se le PUEDE hacer cambia con él: no se cosecha un
 * lote recién sembrado, no se riega uno en barbecho.
 *
 * Sin el patrón, cada operación de Lote tendría un condicional sobre un
 * campo 'estado', repetido en todos los métodos y creciendo con cada
 * estado nuevo. Aquí cada estado es una clase que sabe dos cosas: qué
 * permite y a cuál se transita.
 *
 * DIFERENCIA CON STRATEGY: una estrategia la ELIGE el cliente y no cambia
 * sola; un estado lo cambia el PROPIO OBJETO como consecuencia de lo que
 * le ocurre. Aquí nadie dice "pasa a Maduro": se llega ahí por haber
 * madurado.
 */
public interface EstadoLote {

    /** Nombre legible, para reportes y bitácoras. */
    String getNombre();

    /**
     * ¿Tiene sentido regar en este estado?
     *
     * Solo responde SI SE PUEDE, no CUÁNTO. La cantidad de agua la
     * decidirá el patrón Strategy en el módulo de riego.
     */
    boolean admiteRiego();

    // --- transiciones del ciclo ---

    void sembrar(Lote lote);

    void madurar(Lote lote);

    void cosechar(Lote lote);

    void prepararSuelo(Lote lote);
}
