/* ---------------------------------------------------------------
 * COPIA de la Semana 6 (patrón Abstract Factory).
 * Se incluye aquí para que esta semana compile y se ejecute de forma
 * independiente. El original y su explicación están en
 * semana-6-abstract-factory-prototype/. No se modificó nada de su contenido.
 * --------------------------------------------------------------- */
/**
 * PRODUCTO CONCRETO, familia INVERNADERO.
 * Riego por goteo: caudal bajo y localizado en la raíz.
 */
public class RiegoGoteo implements ActuadorRiego {

    private static final double LITROS_POR_MINUTO = 2.0;

    @Override
    public void regar(int minutos) {
        System.out.printf("[GOTEO]    Riego localizado %d min -> %.1f L aplicados%n",
                minutos, minutos * LITROS_POR_MINUTO);
    }

    @Override
    public String getTipo() { return "GOTEO"; }
}
