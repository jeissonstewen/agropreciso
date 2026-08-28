/**
 * Demostración del patrón Singleton en AgroPrecisión.
 * Compilar y ejecutar:
 *    javac *.java
 *    java MainDemoSingleton
 */
public class MainDemoSingleton {

    public static void main(String[] args) {

        System.out.println("=== PRUEBA 1: una sola instancia de SesionUsuario ===");
        SesionUsuario s1 = SesionUsuario.getInstancia();
        SesionUsuario s2 = SesionUsuario.getInstancia();
        System.out.println("s1 == s2 ? " + (s1 == s2));            // esperado: true
        System.out.println("hashCode s1: " + s1.hashCode());
        System.out.println("hashCode s2: " + s2.hashCode());        // esperado: igual

        System.out.println("\n=== PRUEBA 2: login e impacto global ===");
        s1.iniciarSesion("ingeniero", "agro");
        System.out.println("Desde s2 el usuario es: " + s2.getUsuario()); // esperado: ingeniero

        System.out.println("\n=== PRUEBA 3: otro módulo lee la sesión sin recibirla por parámetro ===");
        ModuloRiego riego = new ModuloRiego();
        riego.ejecutarRiego();

        System.out.println("\n=== PRUEBA 4: configuración global compartida ===");
        ConfiguracionSistema c1 = ConfiguracionSistema.getInstancia();
        ConfiguracionSistema c2 = ConfiguracionSistema.getInstancia();
        System.out.println("Finca: " + c1.getNombreFinca());
        System.out.println("Umbral inicial (c2): " + c2.getUmbralHumedadMin());
        c1.setUmbralHumedadMin(45.0);
        System.out.println("Umbral leído por c2: " + c2.getUmbralHumedadMin()); // esperado: 45.0

        System.out.println("\n=== PRUEBA 5: cierre de sesión y permisos ===");
        s2.cerrarSesion();
        System.out.println("¿Sesión activa según s1? " + s1.estaActiva());      // esperado: false
        riego.ejecutarRiego();                                                  // esperado: acceso denegado
    }
}

/** Módulo cliente: nunca recibe la sesión por parámetro, la obtiene del Singleton. */
class ModuloRiego {

    public void ejecutarRiego() {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        ConfiguracionSistema cfg = ConfiguracionSistema.getInstancia();

        if (!sesion.tienePermiso("OPERARIO")) {
            System.out.println("[RIEGO] Acceso denegado: no hay sesión válida.");
            return;
        }

        double humedadDummy = 28.5; // lectura simulada del sensor IoT
        System.out.println("[RIEGO] Operación solicitada por: " + sesion.getUsuario());
        System.out.println("[RIEGO] Humedad medida: " + humedadDummy + "% | Umbral: "
                           + cfg.getUmbralHumedadMin() + "%");

        if (humedadDummy < cfg.getUmbralHumedadMin()) {
            System.out.println("[RIEGO] Activando riego en el lote 1.");
        } else {
            System.out.println("[RIEGO] Humedad suficiente, no se riega.");
        }
    }
}
