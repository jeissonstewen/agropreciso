import java.util.Objects;

/**
 * PATRÓN MEDIATOR — el MEDIADOR CONCRETO.
 *
 * Es el único objeto que conoce a todos los colegas, y por eso es el único
 * que sabe qué sigue después de cada hecho:
 *
 *   anomalía detectada  -> que el gestor escale la incidencia
 *   orden generada      -> que operaciones la ejecute
 *   orden ejecutada     -> que la bitácora lo registre
 *   nadie pudo atender  -> que la bitácora lo registre
 *
 * SIN el patrón, la estación necesitaría una referencia al gestor, el
 * gestor otra a operaciones y operaciones otra a la bitácora; y cada
 * colega nuevo multiplicaría los enlaces. Con cuatro colegas serían hasta
 * doce referencias cruzadas; aquí son cuatro, todas hacia el centro.
 *
 * Se mantiene deliberadamente pequeño: solo ENRUTA. Las reglas de negocio
 * siguen donde estaban —la de quién atiende, en la cadena; la de qué hace
 * cada acción, en las órdenes—. Un mediador que empieza a decidir reglas
 * se convierte en un God Object, que es el riesgo conocido del patrón.
 */
public class CentroDeOperaciones implements Mediador {

    private GestorIncidencias gestor;
    private OperacionesCampo  operaciones;
    private Bitacora          bitacora;

    private int avisosRecibidos = 0;

    // --- registro de colegas ---

    public void registrarGestor(GestorIncidencias gestor) {
        this.gestor = Objects.requireNonNull(gestor);
    }

    public void registrarOperaciones(OperacionesCampo operaciones) {
        this.operaciones = Objects.requireNonNull(operaciones);
    }

    public void registrarBitacora(Bitacora bitacora) {
        this.bitacora = Objects.requireNonNull(bitacora);
    }

    // --- el enrutamiento: el corazón del patrón ---

    @Override
    public void notificar(Colega origen, Evento evento, Object dato) {
        avisosRecibidos++;
        System.out.println("  [CENTRO] " + origen.getNombre() + " informa: " + evento);

        switch (evento) {
            case LECTURA_ANOMALA:
                exigir(gestor, "un gestor de incidencias");
                gestor.escalar((Lote) dato);
                break;

            case ORDEN_GENERADA:
                exigir(operaciones, "un equipo de operaciones");
                operaciones.ejecutar((Orden) dato);
                break;

            case ORDEN_EJECUTADA:
                registrar("Ejecutada: " + ((Orden) dato).getDescripcion());
                break;

            case INCIDENCIA_SIN_ATENDER:
                registrar("Sin atender: " + ((Lote) dato).getNombre());
                break;

            default:
                throw new IllegalArgumentException("Evento no contemplado: " + evento);
        }
    }

    /** Cuántos avisos ha enrutado, para la demo y las pruebas. */
    public int getAvisosRecibidos() { return avisosRecibidos; }

    // --- detalles internos ---

    /**
     * La bitácora es opcional: si no hay ninguna registrada, el sistema
     * sigue funcionando sin dejar constancia. Es justo lo que permite
     * agregar o quitar un colega sin tocar a los demás.
     */
    private void registrar(String texto) {
        if (bitacora != null) {
            bitacora.registrar(texto);
        }
    }

    private void exigir(Object colega, String cual) {
        if (colega == null) {
            throw new IllegalStateException("El centro no tiene registrado " + cual);
        }
    }
}
