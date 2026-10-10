/**
 * PATRÓN DECORATOR — decorador abstracto para el servicio de clima.
 *
 * En la Semana 8 aplicamos este mismo patrón a los sensores. Aquí se aplica
 * a OTRA interfaz del sistema, ProveedorClima, para mostrar que el
 * mecanismo es independiente del dominio: lo único que hace falta es una
 * interfaz y la voluntad de añadirle responsabilidades sin modificarla.
 *
 * Igual que allá: ES un ProveedorClima y TIENE un ProveedorClima. Lo
 * primero le permite ocupar el lugar del original en cualquier parte del
 * sistema —incluida la fachada de esta misma semana—; lo segundo, añadir
 * comportamiento alrededor de la consulta.
 */
public abstract class ProveedorClimaDecorador implements ProveedorClima {

    /** El objeto envuelto: el adaptador real u otro decorador. */
    protected final ProveedorClima proveedorEnvuelto;

    protected ProveedorClimaDecorador(ProveedorClima proveedorEnvuelto) {
        if (proveedorEnvuelto == null) {
            throw new IllegalArgumentException("Un decorador necesita un proveedor que envolver");
        }
        this.proveedorEnvuelto = proveedorEnvuelto;
    }

    @Override
    public Lectura obtenerTemperatura() {
        return proveedorEnvuelto.obtenerTemperatura();
    }

    @Override
    public Lectura obtenerHumedad() {
        return proveedorEnvuelto.obtenerHumedad();
    }

    @Override
    public String getOrigen() {
        return proveedorEnvuelto.getOrigen();
    }

    /** Cadena de decoradores aplicados, para la demo y las pruebas. */
    public abstract String getDescripcion();

    /** Si lo envuelto es otro decorador, encadena su descripción. */
    static String descripcionDe(ProveedorClima proveedor) {
        return (proveedor instanceof ProveedorClimaDecorador)
                ? ((ProveedorClimaDecorador) proveedor).getDescripcion()
                : proveedor.getOrigen();
    }
}
