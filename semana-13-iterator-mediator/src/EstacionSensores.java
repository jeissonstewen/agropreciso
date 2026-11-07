import java.util.Iterator;

/**
 * COLEGA: la estación que inspecciona la finca.
 *
 * Aquí se encuentran los dos patrones de la semana. Recorre la finca con
 * un ITERADOR —sin saber si está organizada en una, dos o tres capas de
 * zonas— y, por cada lectura fuera de rango, avisa al MEDIADOR. No sabe
 * quién atenderá el aviso ni qué se hará con él.
 */
public class EstacionSensores extends Colega {

    public EstacionSensores(Mediador mediador) {
        super(mediador, "EstacionSensores");
    }

    /**
     * Pasada de inspección sobre toda la finca.
     *
     * @param finca    la zona raíz
     * @param recorrido en qué orden visitar los lotes
     * @return cuántas lecturas anómalas se encontraron
     */
    public int inspeccionar(ZonaCultivo finca, TipoRecorrido recorrido) {
        System.out.println("  [" + getNombre() + "] inspeccion en " + recorrido);
        int anomalas = 0;
        Iterator<Lote> lotes = finca.iterador(recorrido);
        while (lotes.hasNext()) {
            Lote lote = lotes.next();
            if (tieneLecturaAnomala(lote)) {
                anomalas++;
                avisar(Evento.LECTURA_ANOMALA, lote);    // ← no sabe a quién llega
            }
        }
        return anomalas;
    }

    /** Humedad por debajo del umbral propio del lote. */
    private boolean tieneLecturaAnomala(Lote lote) {
        for (Sensor sensor : lote.getSensores()) {
            Lectura lectura = sensor.leer();
            if (lectura.getTipo().equals("HUMEDAD")
                    && lectura.getValor() < lote.getUmbrales().getHumedadMin()) {
                System.out.printf("    anomalia en %-12s %.1f %% (umbral %.1f %%)%n",
                        lote.getNombre(), lectura.getValor(), lote.getUmbrales().getHumedadMin());
                return true;
            }
        }
        return false;
    }
}
