/* ---------------------------------------------------------------
 * COPIA ADAPTADA de la Semana 9 (patrón Facade).
 * El original, con su explicación, está en
 * semana-9-decorator-facade/ y NO fue modificado.
 *
 * Único cambio hecho aquí, y solo aquí, para la Semana 10:
 *   - Declara 'implements SistemaAgroPrecision', la interfaz que se
 *     extrajo para que la fachada y el proxy de proteccion sean
 *     intercambiables. El cuerpo de los metodos no cambio.
 * --------------------------------------------------------------- */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PATRÓN FACADE — la FACHADA de AgroPrecisión.
 *
 * Ofrece una interfaz única y sencilla a un subsistema que, a estas
 * alturas del proyecto, involucra ocho patrones y más de veinte clases:
 *
 *   - Singleton   : SesionUsuario y ConfiguracionSistema
 *   - Composite   : el árbol ZonaCultivo / Lote de la finca
 *   - Decorator   : los sensores calibrados, validados y registrados
 *   - Adapter     : AdaptadorClimaExterno sobre el servicio de un tercero
 *   - Decorator   : el clima con caché y reintento (Semana 9)
 *   - Bridge      : Reporte sobre SalidaConsola / SalidaMarkdown / SalidaCsv
 *
 * El cliente que quiera "hacer la jornada" ya no necesita conocer ese mapa:
 * llama a iniciarJornada(), monitorearFinca(), consultarClima() y
 * emitirReporteEstado(). Cuatro métodos en vez de treinta líneas de
 * ensamblaje.
 *
 * IMPORTANTE: la fachada NO prohíbe el acceso al subsistema. Quien necesite
 * algo que ella no ofrece puede pedir el árbol con getFinca() y trabajar
 * directamente con las clases de dentro. Simplificar no es encerrar: esa es
 * la diferencia entre una fachada y un muro.
 */
public class FachadaAgroPrecision implements SistemaAgroPrecision {

    private final ZonaCultivo finca;
    private final List<Lote>  lotes = new ArrayList<>();
    private final ProveedorClima clima;
    private final ConfiguracionSistema configuracion;

    /**
     * Arma el subsistema completo con valores de la finca. Todo este
     * ensamblaje es, justamente, lo que la fachada le ahorra al cliente.
     */
    public FachadaAgroPrecision(ProveedorClima clima) {
        this.clima         = clima;
        this.configuracion = ConfiguracionSistema.getInstancia();

        // Sensores decorados (Decorator de la Semana 8).
        Sensor humedadNorte = new SensorConRegistro(
                new SensorConCalibracion(
                        new SensorFijo("CN-H", "HUMEDAD", 66.0, "%"), -1.5));
        Sensor humedadSur = new SensorConValidacion(
                new SensorFijo("CS-H", "HUMEDAD", 120.0, "%"), 0.0, 100.0);

        Lote cafeNorte = new Lote("Cafe-Norte", "cafe", 1.5, new UmbralesLote(70.0, 26.0))
                .agregarSensor(humedadNorte)
                .agregarSensor(new SensorFijo("CN-T", "TEMPERATURA", 22.0, "C"))
                .setActuador(new RiegoGoteo());
        Lote cafeSur = new Lote("Cafe-Sur", "cafe", 2.0, new UmbralesLote(70.0, 26.0))
                .agregarSensor(humedadSur)
                .setActuador(new RiegoGoteo());
        Lote maizEste = new Lote("Maiz-Este", "maiz", 6.0, new UmbralesLote(40.0, 35.0))
                .agregarSensor(new SensorFijo("ME-H", "HUMEDAD", 31.0, "%"))
                .setActuador(new RiegoAspersor());
        lotes.addAll(Arrays.asList(cafeNorte, cafeSur, maizEste));

        // Árbol de la finca (Composite de la Semana 8).
        this.finca = new ZonaCultivo(configuracion.getNombreFinca())
                .agregar(new ZonaCultivo("Invernadero").agregar(cafeNorte).agregar(cafeSur))
                .agregar(new ZonaCultivo("Campo abierto").agregar(maizEste));
    }

    // ------------------------------------------------------------------
    //  Operaciones de negocio: la cara simple del subsistema
    // ------------------------------------------------------------------

    /** Autentica al operario (Singleton de sesión). */
    @Override
    public boolean iniciarJornada(String usuario, String clave) {
        return SesionUsuario.getInstancia().iniciarSesion(usuario, clave);
    }

    /** Recorre toda la finca leyendo sensores y regando donde haga falta. */
    @Override
    public void monitorearFinca() {
        exigirSesion();
        finca.monitorear();
    }

    /** Consulta el clima del servicio externo, ya adaptado y decorado. */
    @Override
    public void consultarClima() {
        exigirSesion();
        System.out.println("  Origen: " + clima.getOrigen());
        System.out.println("  " + clima.obtenerTemperatura());
        System.out.println("  " + clima.obtenerHumedad());
    }

    /**
     * Emite el reporte de estado de la finca.
     * @param formato "consola", "markdown" o "csv"
     */
    @Override
    public void emitirReporteEstado(String formato) {
        exigirSesion();
        new ReporteEstado(crearSalida(formato), configuracion.getNombreFinca(), leerTodo())
                .generar();
    }

    /** Emite el reporte de alertas derivadas de los umbrales globales. */
    @Override
    public void emitirReporteAlertas(String formato) {
        exigirSesion();
        new ReporteAlertas(crearSalida(formato), alertasActuales()).generar();
    }

    @Override
    public void cerrarJornada() {
        SesionUsuario.getInstancia().cerrarSesion();
    }

    /**
     * Acceso directo al subsistema para quien necesite más que lo que la
     * fachada ofrece. Una fachada simplifica, no encierra.
     */
    public ZonaCultivo getFinca() { return finca; }
    // No forma parte de SistemaAgroPrecision: ver la nota de esa interfaz.

    // ------------------------------------------------------------------
    //  Detalles internos: nada de esto le interesa al cliente
    // ------------------------------------------------------------------

    private void exigirSesion() {
        if (!SesionUsuario.getInstancia().estaActiva()) {
            throw new IllegalStateException("No hay una jornada iniciada");
        }
    }

    private SalidaReporte crearSalida(String formato) {
        switch (formato.toLowerCase()) {
            case "consola":  return new SalidaConsola();
            case "markdown": return new SalidaMarkdown();
            case "csv":      return new SalidaCsv();
            default:
                throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
    }

    private List<Lectura> leerTodo() {
        List<Lectura> lecturas = new ArrayList<>();
        for (Lote lote : lotes) {
            for (Sensor sensor : lote.getSensores()) lecturas.add(sensor.leer());
        }
        return lecturas;
    }

    private List<String> alertasActuales() {
        List<String> alertas = new ArrayList<>();
        for (Lote lote : lotes) {
            for (Sensor sensor : lote.getSensores()) {
                Lectura l = sensor.leer();
                if (l.getTipo().equals("HUMEDAD")
                        && l.getValor() < lote.getUmbrales().getHumedadMin()) {
                    alertas.add(String.format("Humedad baja en %s (%s): %.1f %%",
                            lote.getNombre(), l.getIdSensor(), l.getValor()));
                }
            }
        }
        return alertas;
    }
}
