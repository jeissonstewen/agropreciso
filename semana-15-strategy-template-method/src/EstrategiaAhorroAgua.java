/**
 * ESTRATEGIA CONCRETA — ahorro de agua.
 *
 * Riega lo mínimo necesario para que la humedad vuelva a cruzar el umbral
 * del lote, y ni un minuto más. Es la política por defecto del sistema:
 * en una finca el agua se paga.
 *
 * Modelo simulado: cada minuto de riego recupera RECUPERACION_POR_MINUTO
 * puntos de humedad. Es un dato *dummy*, igual que el resto del prototipo;
 * lo importante del patrón es que la fórmula viva aquí y no en Lote.
 */
public class EstrategiaAhorroAgua implements EstrategiaRiego {

    /** Puntos de humedad que recupera el suelo por cada minuto de riego. */
    private static final double RECUPERACION_POR_MINUTO = 3.0;

    /** Tope de seguridad: nunca más de un cuarto de hora seguido. */
    private static final int MAXIMO_MINUTOS = 15;

    @Override
    public int calcularMinutos(Lectura humedad, UmbralesLote umbrales) {
        double deficit = umbrales.getHumedadMin() - humedad.getValor();
        if (deficit <= 0) return 0;                     // ya está por encima del umbral

        int minutos = (int) Math.ceil(deficit / RECUPERACION_POR_MINUTO);
        return Math.min(minutos, MAXIMO_MINUTOS);
    }

    @Override
    public String getNombre() { return "AHORRO DE AGUA"; }
}
