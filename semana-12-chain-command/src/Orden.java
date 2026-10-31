/**
 * PATRÓN COMMAND — la interfaz ORDEN.
 *
 * Convierte una petición ("riega el lote 10 minutos") en un OBJETO. Eso es
 * todo el patrón, y es lo que permite tres cosas que una llamada directa a
 * un método no permite:
 *
 *   1. PARAMETRIZAR : una orden se puede pasar como argumento.
 *   2. ENCOLAR      : se puede guardar y ejecutar más tarde.
 *   3. DESHACER     : si guarda el estado anterior, puede revertirse.
 *
 * No todas las órdenes son reversibles, y el patrón no obliga a fingir que
 * lo son: por eso existe esReversible(). Regar no se puede deshacer — el
 * agua ya está en el suelo—, y es más honesto declararlo que inventar una
 * reversión falsa.
 */
public interface Orden {

    /** Aplica la orden sobre su receptor. */
    void ejecutar();

    /**
     * Revierte el efecto de ejecutar().
     * @throws UnsupportedOperationException si la orden no es reversible
     */
    void deshacer();

    /** true si deshacer() puede revertir realmente el efecto. */
    boolean esReversible();

    /** Texto para el historial y la bitácora. */
    String getDescripcion();
}
