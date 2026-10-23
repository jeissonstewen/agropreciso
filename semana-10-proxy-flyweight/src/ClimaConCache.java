/* ---------------------------------------------------------------
 * COPIA de la Semana 9 (patrones Decorator y Facade).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-9-decorator-facade/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * DECORADOR CONCRETO: evita llamar dos veces al servicio externo.
 *
 * Consultar una API meteorológica cuesta tiempo y, en la vida real, dinero
 * por petición. Durante una misma jornada de monitoreo el dato no cambia,
 * así que la primera consulta se guarda y las siguientes se responden con
 * lo guardado.
 *
 * Este decorador NO altera el valor: cambia CUÁNTAS VECES se pide.
 */
public class ClimaConCache extends ProveedorClimaDecorador {

    private Lectura temperaturaGuardada;
    private Lectura humedadGuardada;
    private int aciertos = 0;          // consultas resueltas sin ir al origen

    public ClimaConCache(ProveedorClima proveedorEnvuelto) {
        super(proveedorEnvuelto);
    }

    @Override
    public Lectura obtenerTemperatura() {
        if (temperaturaGuardada == null) {
            temperaturaGuardada = proveedorEnvuelto.obtenerTemperatura();
        } else {
            aciertos++;
        }
        return temperaturaGuardada;
    }

    @Override
    public Lectura obtenerHumedad() {
        if (humedadGuardada == null) {
            humedadGuardada = proveedorEnvuelto.obtenerHumedad();
        } else {
            aciertos++;
        }
        return humedadGuardada;
    }

    /** Cuántas consultas se resolvieron sin llamar al origen. */
    public int getAciertos() { return aciertos; }

    /** Olvida lo guardado: se usa al empezar una jornada nueva. */
    public void invalidar() {
        temperaturaGuardada = null;
        humedadGuardada     = null;
    }

    @Override
    public String getDescripcion() {
        return descripcionDe(proveedorEnvuelto) + " + cache";
    }
}
