/**
 * PATRÓN MEDIATOR — la interfaz del MEDIADOR.
 *
 * Los colegas no se conocen entre sí: cuando pasa algo, se lo cuentan al
 * mediador y él decide a quién le toca actuar.
 *
 * DIFERENCIA CON EL FACADE (Semana 9): la fachada es unidireccional y el
 * subsistema NO la conoce —existe para que un cliente externo tenga un
 * camino corto—. Aquí la relación es bidireccional: cada colega guarda una
 * referencia al mediador y habla a través de él. Son dos formas distintas
 * de centralizar.
 */
public interface Mediador {

    /**
     * Un colega informa de un hecho.
     *
     * @param origen  quién avisa; el mediador lo usa para no devolverle el
     *                aviso a quien lo emitió
     * @param evento  qué pasó
     * @param dato    la carga del evento. Viaja como Object porque cada
     *                evento lleva algo distinto —un lote, una orden, una
     *                incidencia— y el mediador lo interpreta según el
     *                evento. Es el precio de tener una sola puerta de
     *                entrada, y queda acotado a un solo switch.
     */
    void notificar(Colega origen, Evento evento, Object dato);
}
