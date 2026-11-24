/**
 * PATRÓN OBSERVER — la interfaz del OBSERVADOR.
 *
 * Quien quiera enterarse de las alertas implementa esto y se suscribe. El
 * sujeto no sabe cuántos hay, ni quiénes son, ni qué hacen con el aviso:
 * solo que cumplen este contrato.
 */
public interface ObservadorAlerta {

    /** Llamado por el sujeto cada vez que publica una alerta. */
    void alPublicarse(Alerta alerta);

    /** Nombre del suscriptor, para la demo y las pruebas. */
    String getNombre();
}
