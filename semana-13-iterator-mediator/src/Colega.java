import java.util.Objects;

/**
 * COLEGA del patrón Mediator.
 *
 * Cada colega sabe hacer una cosa y conoce al mediador, pero NO conoce a
 * los demás colegas. Esa es la restricción que el patrón impone y la que
 * hay que poder señalar en el código: en ninguna subclase de Colega hay un
 * campo que apunte a otro colega.
 */
public abstract class Colega {

    protected final Mediador mediador;
    private final String nombre;

    protected Colega(Mediador mediador, String nombre) {
        this.mediador = Objects.requireNonNull(mediador, "Un colega necesita un mediador");
        this.nombre   = Objects.requireNonNull(nombre,   "Un colega necesita un nombre");
    }

    public String getNombre() { return nombre; }

    /** Atajo para avisar al mediador. */
    protected void avisar(Evento evento, Object dato) {
        mediador.notificar(this, evento, dato);
    }
}
