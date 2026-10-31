/**
 * Catálogo de incidencias que el sistema sabe detectar.
 *
 * Cada tipo lleva su severidad asociada, de modo que no puede existir una
 * incidencia cuyo tipo y gravedad se contradigan.
 */
public enum TipoIncidencia {

    HUMEDAD_BAJA   (Severidad.LEVE,    "Humedad por debajo del umbral del lote"),
    UMBRAL_INADECUADO(Severidad.MEDIA, "El umbral configurado no corresponde al cultivo"),
    SENSOR_AVERIADO(Severidad.CRITICA, "El sensor entrega lecturas fisicamente imposibles");

    private final Severidad severidad;
    private final String    descripcion;

    TipoIncidencia(Severidad severidad, String descripcion) {
        this.severidad   = severidad;
        this.descripcion = descripcion;
    }

    public Severidad getSeveridad()  { return severidad; }
    public String    getDescripcion(){ return descripcion; }
}
