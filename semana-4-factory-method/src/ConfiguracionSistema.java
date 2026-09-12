/* ---------------------------------------------------------------
 * COPIA de la Semana 3 (patrón Singleton).
 * Se incluye aquí para que esta entrega compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-3-singleton/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PATRÓN SINGLETON (variante de inicialización temprana / eager).
 *
 * Guarda los parámetros globales de la finca que consultan varios módulos
 * (riego, inventario, predicción). Al ser una sola instancia, si un módulo
 * cambia un umbral, todos los demás ven el cambio inmediatamente.
 */
public final class ConfiguracionSistema {

    // Instancia creada al cargar la clase: no requiere synchronized.
    private static final ConfiguracionSistema INSTANCIA = new ConfiguracionSistema();

    private String nombreFinca      = "Finca La Esperanza";
    private double umbralHumedadMin = 35.0;  // % por debajo del cual se riega
    private double tempMaxCadenaFrio = 8.0;  // °C máximo para inventario refrigerado
    private String unidadTemperatura = "C";

    private ConfiguracionSistema() { }

    public static ConfiguracionSistema getInstancia() {
        return INSTANCIA;
    }

    public String getNombreFinca()          { return nombreFinca; }
    public double getUmbralHumedadMin()     { return umbralHumedadMin; }
    public double getTempMaxCadenaFrio()    { return tempMaxCadenaFrio; }
    public String getUnidadTemperatura()    { return unidadTemperatura; }

    public void setUmbralHumedadMin(double valor) {
        this.umbralHumedadMin = valor;
        System.out.println("[CONFIG] Umbral de humedad actualizado a " + valor + "%");
    }
}
