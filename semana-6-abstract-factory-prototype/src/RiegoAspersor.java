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
