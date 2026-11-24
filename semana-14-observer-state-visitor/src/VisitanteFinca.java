/**
 * PATRÓN VISITOR — la interfaz del VISITANTE.
 *
 * El problema que resuelve está en nuestro propio código: ComponenteFinca
 * ya acumulaba CINCO operaciones —getNombre, getAreaHectareas,
 * contarSensores, monitorear, describir— y cada una obligó a tocar la
 * interfaz, ZonaCultivo y Lote. La sexta haría lo mismo.
 *
 * Con el visitante, una operación nueva es UNA CLASE NUEVA y cero cambios
 * en el árbol.
 *
 * El precio, que conviene decir sin maquillar:
 *   - Hay que tocar la jerarquía UNA VEZ para agregar aceptar().
 *   - El visitante necesita acceder a los datos del elemento, así que
 *     relaja el encapsulamiento.
 *   - Es rígido ante TIPOS nuevos: un tercer tipo de componente obligaría
 *     a tocar todos los visitantes.
 *
 * Por eso se usa cuando la jerarquía es estable y las operaciones no, que
 * es exactamente nuestro caso: ZonaCultivo y Lote no han cambiado desde la
 * Semana 8, pero las operaciones sobre ellos no paran de crecer.
 */
public interface VisitanteFinca {

    void visitarZona(ZonaCultivo zona);

    void visitarLote(Lote lote);

    /** Resultado de la visita, ya formateado. */
    String getResultado();
}
