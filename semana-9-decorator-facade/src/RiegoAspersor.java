/* ---------------------------------------------------------------
 * COPIA de la Semana 6 (patrones Abstract Factory y Prototype).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-6-abstract-factory-prototype/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PRODUCTO CONCRETO, familia CAMPO ABIERTO.
 * Riego por aspersión: caudal alto que cubre toda la superficie.
 */
public class RiegoAspersor implements ActuadorRiego {

    private static final double LITROS_POR_MINUTO = 15.0;

    @Override
    public void regar(int minutos) {
        System.out.printf("[ASPERSOR] Riego por aspersion %d min -> %.1f L aplicados%n",
                minutos, minutos * LITROS_POR_MINUTO);
    }

    @Override
    public String getTipo() { return "ASPERSOR"; }
}
