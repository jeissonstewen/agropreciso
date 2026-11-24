import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * PATRÓN OBSERVER — el SUJETO.
 *
 * Publica alertas a quien se haya suscrito. Su lista es de
 * ObservadorAlerta, es decir de una interfaz: por eso puede notificar a
 * un panel de consola, a un historial o a algo que todavía no existe sin
 * enterarse de la diferencia.
 *
 * DIFERENCIA CON EL MEDIATOR (Semana 13), que es el par más confundible:
 *
 *   Mediator : muchos a muchos. El centro CONOCE a cada colega concreto y
 *              decide a quién le toca actuar. Él registra a los colegas.
 *   Observer : uno a muchos. El sujeto NO conoce a sus observadores: solo
 *              tiene una lista de interfaces, y son ELLOS los que se
 *              suscriben. Difunde lo mismo a todos, sin decidir nada.
 *
 * Para revisar la finca usa el ITERADOR de la Semana 13: recorre los lotes
 * sin saber cómo está organizada.
 */
public class MonitorDeAlertas {

    private final List<ObservadorAlerta> observadores = new ArrayList<>();
    private int alertasPublicadas = 0;

    // --- suscripción: la controlan los observadores, no el sujeto ---

    public void suscribir(ObservadorAlerta observador) {
        Objects.requireNonNull(observador, "No se puede suscribir nada nulo");
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public boolean quitar(ObservadorAlerta observador) {
        return observadores.remove(observador);
    }

    public int cuantosObservadores() { return observadores.size(); }

    public int getAlertasPublicadas() { return alertasPublicadas; }

    // --- publicación ---

    /**
     * Recorre la finca y publica una alerta por cada lote cuya humedad
     * esté por debajo de su umbral.
     *
     * Un lote que su estado no admite regar no genera alerta de humedad:
     * no tiene sentido avisar de que está seco un lote en barbecho.
     *
     * @return cuántas alertas se publicaron
     */
    public int revisarFinca(ZonaCultivo finca) {
        int publicadas = 0;
        Iterator<Lote> lotes = finca.iterador(TipoRecorrido.PROFUNDIDAD);
        while (lotes.hasNext()) {
            Lote lote = lotes.next();
            if (!lote.admiteRiego()) {
                continue;
            }
            for (Sensor sensor : lote.getSensores()) {
                Lectura lectura = sensor.leer();
                if (lectura.getTipo().equals("HUMEDAD")
                        && lectura.getValor() < lote.getUmbrales().getHumedadMin()) {
                    publicar(new Alerta(lote.getNombre(), severidadDe(lectura, lote),
                            String.format("humedad %.1f %% bajo el umbral %.1f %%",
                                    lectura.getValor(), lote.getUmbrales().getHumedadMin())));
                    publicadas++;
                    break;                      // una alerta por lote basta
                }
            }
        }
        return publicadas;
    }

    /** Un cambio de estado también interesa a los suscriptores. */
    public void publicarCambioDeEstado(Lote lote, String estadoAnterior) {
        publicar(new Alerta(lote.getNombre(), Severidad.LEVE,
                "paso de '" + estadoAnterior + "' a '" + lote.getEstado().getNombre() + "'"));
    }

    /**
     * El corazón del patrón: avisar a todos, sin saber quiénes son.
     * Se recorre una copia para que un observador pueda desuscribirse
     * mientras recibe el aviso sin romper la iteración.
     */
    private void publicar(Alerta alerta) {
        alertasPublicadas++;
        for (ObservadorAlerta observador : new ArrayList<>(observadores)) {
            observador.alPublicarse(alerta);
        }
    }

    /** Cuanto más lejos del umbral, más grave. */
    private Severidad severidadDe(Lectura lectura, Lote lote) {
        double faltante = lote.getUmbrales().getHumedadMin() - lectura.getValor();
        if (faltante >= 20) return Severidad.CRITICA;
        if (faltante >= 10) return Severidad.MEDIA;
        return Severidad.LEVE;
    }
}
