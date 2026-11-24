/**
 * ESTRATEGIA CONCRETA — según el clima.
 *
 * Es la que justifica el patrón. Las otras dos miran solo el sensor del
 * lote; esta además consulta el servicio meteorológico y puede decidir
 * NO REGAR (devolver 0) aunque el suelo esté seco, porque el ambiente
 * está saturado y va a llover.
 *
 * Lo interesante es lo que NO hizo falta para agregarla:
 *
 *   - Lote no cambió ni una línea.
 *   - ActuadorRiego no cambió.
 *   - Las otras dos estrategias no cambiaron.
 *
 * Y nótese que esta estrategia necesita una colaboración que las otras no
 * usan: el ProveedorClima. La recibe por CONSTRUCTOR, no por parámetro
 * del método. Si se pasara por parámetro, las dos estrategias que no
 * consultan el clima tendrían que recibirlo igual y ensuciarían la
 * interfaz. Esta clase se apoya además en el ADAPTER de la Semana 7: habla
 * con ProveedorClima y no sabe que detrás hay un servicio en Fahrenheit.
 */
public class EstrategiaSegunClima implements EstrategiaRiego {

    private static final double RECUPERACION_POR_MINUTO = 3.0;

    /** Humedad ambiente por encima de la cual se posterga el riego. */
    private static final double HUMEDAD_AMBIENTE_ALTA = 60.0;

    /** Temperatura por encima de la cual se riega un 50 % más. */
    private static final double TEMPERATURA_ALTA = 29.0;

    private static final int MAXIMO_MINUTOS = 20;

    private final ProveedorClima clima;   // colaboración propia de esta estrategia

    public EstrategiaSegunClima(ProveedorClima clima) {
        if (clima == null) throw new IllegalArgumentException("La estrategia necesita un proveedor de clima");
        this.clima = clima;
    }

    @Override
    public int calcularMinutos(Lectura humedad, UmbralesLote umbrales) {
        double deficit = umbrales.getHumedadMin() - humedad.getValor();
        if (deficit <= 0) return 0;

        // 1. Si el ambiente está saturado, se posterga: la lluvia hará el trabajo.
        if (clima.obtenerHumedad().getValor() >= HUMEDAD_AMBIENTE_ALTA) return 0;

        int minutos = (int) Math.ceil(deficit / RECUPERACION_POR_MINUTO);

        // 2. Con calor hay más evaporación, así que el mismo déficit pide más agua.
        if (clima.obtenerTemperatura().getValor() > TEMPERATURA_ALTA) {
            minutos = (int) Math.ceil(minutos * 1.5);
        }
        return Math.min(minutos, MAXIMO_MINUTOS);
    }

    @Override
    public String getNombre() { return "SEGUN CLIMA (" + clima.getOrigen() + ")"; }
}
