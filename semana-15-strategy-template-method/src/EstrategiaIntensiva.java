/**
 * ESTRATEGIA CONCRETA — riego intensivo.
 *
 * No busca el umbral: busca un MARGEN por encima de él, para que el
 * cultivo aguante varios días sin volver a regar. Es la política de un
 * cultivo en plena floración o de un lote al que no se puede volver a
 * diario.
 *
 * Mismo problema que la estrategia de ahorro, misma interfaz, resultado
 * distinto. Eso es exactamente lo que el patrón permite: intercambiar la
 * política sin tocar el lote.
 */
public class EstrategiaIntensiva implements EstrategiaRiego {

    private static final double RECUPERACION_POR_MINUTO = 3.0;

    /** Puntos de humedad POR ENCIMA del umbral que se busca dejar. */
    private static final double MARGEN = 10.0;

    /** Riego mínimo: si se decide regar, se riega de verdad. */
    private static final int MINIMO_MINUTOS = 10;

    private static final int MAXIMO_MINUTOS = 30;

    @Override
    public int calcularMinutos(Lectura humedad, UmbralesLote umbrales) {
        double objetivo = umbrales.getHumedadMin() + MARGEN;
        double deficit  = objetivo - humedad.getValor();
        if (deficit <= 0) return 0;

        int minutos = (int) Math.ceil(deficit / RECUPERACION_POR_MINUTO);
        return Math.max(MINIMO_MINUTOS, Math.min(minutos, MAXIMO_MINUTOS));
    }

    @Override
    public String getNombre() { return "INTENSIVA"; }
}
