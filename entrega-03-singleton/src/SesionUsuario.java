/**
 * PATRÓN SINGLETON — Módulo de sesión de AgroPrecisión.
 *
 * Garantiza que exista UNA sola sesión de usuario activa en toda la aplicación
 * y ofrece un punto de acceso global a ella.
 *
 * Elementos clave del patrón:
 *  1. Atributo estático privado que guarda la única instancia.
 *  2. Constructor privado: nadie puede hacer "new SesionUsuario()".
 *  3. Método estático público getInstancia() que crea o devuelve la instancia.
 */
public final class SesionUsuario {

    // 1. La única instancia (inicialización perezosa)
    private static SesionUsuario instancia;

    // Estado de la sesión
    private String usuario;
    private String rol;
    private boolean activa;

    // Usuarios dummy
    private static final String[][] USUARIOS_DUMMY = {
        {"admin",    "1234", "ADMINISTRADOR"},
        {"ingeniero","agro", "AGRONOMO"},
        {"operario", "riego","OPERARIO"}
    };

    // 2. Constructor privado: bloquea la creación externa de instancias
    private SesionUsuario() {
        this.activa = false;
    }

    // 3. Punto de acceso global. 'synchronized' evita dos instancias con hilos.
    public static synchronized SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    /** Login simulado contra la lista de usuarios dummy. */
    public boolean iniciarSesion(String usuario, String clave) {
        for (String[] u : USUARIOS_DUMMY) {
            if (u[0].equals(usuario) && u[1].equals(clave)) {
                this.usuario = u[0];
                this.rol     = u[2];
                this.activa  = true;
                System.out.println("[SESION] Bienvenido " + this.usuario + " (" + this.rol + ")");
                return true;
            }
        }
        System.out.println("[SESION] Credenciales incorrectas.");
        return false;
    }

    public void cerrarSesion() {
        System.out.println("[SESION] Sesión cerrada para " + this.usuario);
        this.usuario = null;
        this.rol     = null;
        this.activa  = false;
    }

    public boolean estaActiva()  { return activa; }
    public String  getUsuario()  { return usuario; }
    public String  getRol()      { return rol; }

    /** Control de permisos usado por los demás módulos. */
    public boolean tienePermiso(String rolRequerido) {
        return activa && (rol.equals("ADMINISTRADOR") || rol.equals(rolRequerido));
    }
}
