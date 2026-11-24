import java.util.Objects;

/**
 * Lo que el sujeto publica a sus observadores.
 *
 * Es inmutable: la misma instancia llega a todos los suscriptores, así que
 * ninguno puede alterarla para los demás.
 */
public class Alerta {

    private final String    lote;
    private final Severidad severidad;
    private final String    mensaje;

    public Alerta(String lote, Severidad severidad, String mensaje) {
        this.lote      = Objects.requireNonNull(lote);
        this.severidad = Objects.requireNonNull(severidad);
        this.mensaje   = Objects.requireNonNull(mensaje);
    }

    public String    getLote()      { return lote; }
    public Severidad getSeveridad() { return severidad; }
    public String    getMensaje()   { return mensaje; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", severidad, lote, mensaje);
    }
}
