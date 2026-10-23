/**
 * SUJETO del patrón Proxy: el contrato del sistema AgroPrecisión.
 *
 * Se extrae de la FachadaAgroPrecision de la Semana 9 para que la fachada
 * real y el proxy de protección sean intercambiables. El cliente programa
 * contra esta interfaz y no sabe cuál de las dos tiene delante.
 *
 * DECISIÓN DELIBERADA: esta interfaz NO declara getFinca().
 *
 * La fachada de la Semana 9 expone el subsistema a propósito —"simplificar
 * no es encerrar"—, pero ese mismo método abriría un agujero en el control
 * de acceso: quien obtuviera el árbol podría regar o cambiar umbrales
 * saltándose el proxy. Los dos patrones tiran en direcciones opuestas en el
 * mismo punto del código, y aquí se resuelve así: quien sostiene esta
 * interfaz no alcanza el subsistema; el acceso directo queda solo para quien
 * tenga la clase concreta de la fachada.
 */
public interface SistemaAgroPrecision {

    boolean iniciarJornada(String usuario, String clave);

    void monitorearFinca();

    void consultarClima();

    void emitirReporteEstado(String formato);

    void emitirReporteAlertas(String formato);

    void cerrarJornada();
}
