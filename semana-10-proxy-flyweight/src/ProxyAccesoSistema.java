/**
 * PATRÓN PROXY — proxy de PROTECCIÓN sobre el sistema AgroPrecisión.
 *
 * Implementa la misma interfaz que la fachada real (SistemaAgroPrecision)
 * y la envuelve. Antes de delegar cada operación comprueba que el usuario
 * de la sesión tenga el rol necesario; si no lo tiene, la llamada ni
 * siquiera llega al subsistema.
 *
 * DIFERENCIA CON EL DECORATOR (semanas 8 y 9): la estructura es idéntica
 * —ambos implementan la interfaz que envuelven— pero la intención es otra.
 * Un decorador AÑADE comportamiento al resultado; un proxy CONTROLA si la
 * llamada se ejecuta. `ClimaConCache` devolvía siempre un dato; este proxy
 * puede no devolver nada en absoluto.
 *
 * DIFERENCIA CON EL FACADE (semana 9): la fachada ofrece una interfaz
 * DISTINTA y más simple que la del subsistema. El proxy conserva EXACTAMENTE
 * la misma interfaz del objeto al que representa: el cliente no nota que
 * está ahí, salvo cuando le niega el paso.
 *
 * Los roles vienen del Singleton de la Semana 3. Nótese que no hubo que
 * tocar SesionUsuario: su método tienePermiso() ya concede todo al
 * ADMINISTRADOR, así que la jerarquía de permisos ya estaba codificada.
 */
public class ProxyAccesoSistema implements SistemaAgroPrecision {

    /** Roles mínimos exigidos por cada operación. */
    private static final String ROL_MONITOREO = "OPERARIO";
    private static final String ROL_REPORTES  = "AGRONOMO";
    private static final String ROL_ADMIN     = "ADMINISTRADOR";

    private final SistemaAgroPrecision sistemaReal;
    private int accesosDenegados = 0;

    public ProxyAccesoSistema(SistemaAgroPrecision sistemaReal) {
        if (sistemaReal == null) {
            throw new IllegalArgumentException("El proxy necesita un sistema al que representar");
        }
        this.sistemaReal = sistemaReal;
    }

    /**
     * Iniciar la jornada es, justamente, autenticarse: no puede exigir una
     * sesión previa. El proxy delega sin comprobar nada.
     */
    @Override
    public boolean iniciarJornada(String usuario, String clave) {
        return sistemaReal.iniciarJornada(usuario, clave);
    }

    @Override
    public void monitorearFinca() {
        exigir(ROL_MONITOREO, "monitorear la finca");
        sistemaReal.monitorearFinca();
    }

    @Override
    public void consultarClima() {
        exigir(ROL_MONITOREO, "consultar el clima");
        sistemaReal.consultarClima();
    }

    @Override
    public void emitirReporteEstado(String formato) {
        exigir(ROL_REPORTES, "emitir el reporte de estado");
        sistemaReal.emitirReporteEstado(formato);
    }

    @Override
    public void emitirReporteAlertas(String formato) {
        exigir(ROL_REPORTES, "emitir el reporte de alertas");
        sistemaReal.emitirReporteAlertas(formato);
    }

    @Override
    public void cerrarJornada() {
        exigir(ROL_ADMIN, "cerrar la jornada");
        sistemaReal.cerrarJornada();
    }

    /** Cuántas llamadas bloqueó el proxy, para auditoría. */
    public int getAccesosDenegados() { return accesosDenegados; }

    /**
     * El control de acceso del patrón. Si el rol no alcanza, la llamada
     * termina aquí: el subsistema nunca se entera de que existió.
     */
    private void exigir(String rolRequerido, String operacion) {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        if (!sesion.estaActiva()) {
            accesosDenegados++;
            throw new SecurityException("No hay una jornada iniciada para " + operacion);
        }
        if (!sesion.tienePermiso(rolRequerido)) {
            accesosDenegados++;
            throw new SecurityException(String.format(
                    "El rol %s no puede %s (se requiere %s)",
                    sesion.getRol(), operacion, rolRequerido));
        }
    }
}
