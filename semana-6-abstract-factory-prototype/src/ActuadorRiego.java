/**
 * PRODUCTO ABSTRACTO de la familia de equipamiento.
 *
 * Dispositivo físico que aplica el agua sobre el lote. Solo ejecuta: la
 * DECISIÓN de cuándo regar no vive aquí (queda para el módulo de riego).
 * Cada familia de equipamiento tiene su propio actuador: goteo en
 * invernadero, aspersor en campo abierto.
 */
public interface ActuadorRiego {

    /** Aplica agua durante los minutos indicados. */
    void regar(int minutos);

    /** Nombre del mecanismo: GOTEO o ASPERSOR. */
    String getTipo();
}
