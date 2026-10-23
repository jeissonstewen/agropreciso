/* ---------------------------------------------------------------
 * COPIA de la Semana 9 (patrones Decorator y Facade).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-9-decorator-facade/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * DECORADOR CONCRETO: tolera fallos pasajeros del servicio externo.
 *
 * Una API de terceros se cae a ratos. En vez de propagar el error al
 * módulo de riego, se reintenta un número acotado de veces; solo si todos
 * los intentos fallan se deja pasar la excepción.
 *
 * Este decorador tampoco altera el valor: cambia QUÉ PASA CUANDO FALLA.
 */
public class ClimaConReintento extends ProveedorClimaDecorador {

    private final int maxIntentos;
    private int reintentos = 0;        // intentos extra que hubo que hacer

    public ClimaConReintento(ProveedorClima proveedorEnvuelto, int maxIntentos) {
        super(proveedorEnvuelto);
        if (maxIntentos < 1) {
            throw new IllegalArgumentException("Se necesita al menos un intento");
        }
        this.maxIntentos = maxIntentos;
    }

    @Override
    public Lectura obtenerTemperatura() {
        return conReintento(proveedorEnvuelto::obtenerTemperatura);
    }

    @Override
    public Lectura obtenerHumedad() {
        return conReintento(proveedorEnvuelto::obtenerHumedad);
    }

    /**
     * Ejecuta la consulta hasta maxIntentos veces. Devuelve la primera
     * respuesta buena; si se agotan los intentos, relanza el último error.
     */
    private Lectura conReintento(java.util.function.Supplier<Lectura> consulta) {
        RuntimeException ultimoError = null;
        for (int intento = 1; intento <= maxIntentos; intento++) {
            try {
                return consulta.get();
            } catch (RuntimeException e) {
                ultimoError = e;
                reintentos++;
                System.out.println("    [REINTENTO] intento " + intento + " fallido: "
                        + e.getMessage());
            }
        }
        throw new IllegalStateException(
                "El servicio de clima fallo tras " + maxIntentos + " intentos", ultimoError);
    }

    /** Cuántos intentos fallidos hubo que absorber. */
    public int getReintentos() { return reintentos; }

    @Override
    public String getDescripcion() {
        return descripcionDe(proveedorEnvuelto) + " + reintento(" + maxIntentos + ")";
    }
}
