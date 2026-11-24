/**
 * PATRÓN STRATEGY — la interfaz de la ESTRATEGIA.
 *
 * Decide CUÁNTOS MINUTOS de riego aplicar. Nada más: no lee sensores, no
 * mueve válvulas y no consulta el estado del lote.
 *
 * Esta interfaz cierra una deuda que el proyecto arrastraba desde la
 * Semana 6. Hasta ahora, cuando el lote detectaba humedad baja hacía:
 *
 *     actuador.regar(10);      // 10 minutos quemados en el código
 *
 * Ese 10 era igual para un cafetal en invernadero y para un maizal a
 * pleno sol, y cambiar la política de riego obligaba a editar Lote.
 *
 * REPARTO DE RESPONSABILIDADES en el módulo de riego, que conviene tener
 * claro porque son tres patrones distintos tocando lo mismo:
 *
 *   - STATE (Semana 14) : decide SI corresponde regar (un lote en
 *                         barbecho no se riega aunque esté seco).
 *   - STRATEGY (aquí)   : decide CUÁNTA agua, una vez que sí corresponde.
 *   - ActuadorRiego     : solo APLICA el agua durante esos minutos.
 *
 * Cada estrategia trae sus propias dependencias por constructor —
 * EstrategiaSegunClima compone un ProveedorClima— para que el método no
 * tenga que recibir parámetros que la mayoría de las estrategias ignora.
 */
public interface EstrategiaRiego {

    /**
     * Minutos de riego a aplicar.
     *
     * @param humedad  la última lectura de humedad del lote
     * @param umbrales los umbrales propios del lote
     * @return minutos de riego; 0 significa "no regar ahora"
     */
    int calcularMinutos(Lectura humedad, UmbralesLote umbrales);

    /** Nombre de la política, para la bitácora y los reportes. */
    String getNombre();
}
