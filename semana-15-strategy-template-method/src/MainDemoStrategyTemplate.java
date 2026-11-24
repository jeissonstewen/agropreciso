/**
 * DEMOSTRACIÓN de la Semana 15 — Strategy y Template Method.
 *
 * Diez pruebas de escritorio. Las seis primeras son del STRATEGY en el
 * módulo de riego; las cuatro últimas, del TEMPLATE METHOD en el módulo
 * de predicción.
 *
 * La idea que hay que ver en la salida es esta: en el Strategy, el MISMO
 * lote con la MISMA lectura aplica cantidades de agua distintas según la
 * política que tenga puesta; en el Template Method, modelos distintos
 * recorren el MISMO esqueleto de pasos.
 */
public class MainDemoStrategyTemplate {

    public static void main(String[] args) {

        separador("PARTE 1 - STRATEGY: cuanta agua aplicar");

        // --- Montaje de la finca (Composite de la Semana 8) ---
        Lote cafe = new Lote("Cafe-Norte", "Cafe", 2.5, new UmbralesLote(45, 30))
                .agregarSensor(new SensorFijo("S-H1", "HUMEDAD", 30.0, "%"))
                .agregarSensor(new SensorFijo("S-T1", "TEMPERATURA", 26.0, "C"))
                .setActuador(new RiegoGoteo());

        Lote maiz = new Lote("Maiz-Sur", "Maiz", 4.0, new UmbralesLote(40, 32))
                .agregarSensor(new SensorFijo("S-H2", "HUMEDAD", 25.0, "%"))
                .setActuador(new RiegoAspersor());

        ZonaCultivo finca = new ZonaCultivo("Finca La Esperanza")
                .agregar(cafe)
                .agregar(maiz);

        // Los dos lotes se siembran: el State exige que haya cultivo para regar.
        cafe.sembrar();
        maiz.sembrar();

        titulo("PRUEBA 1 - El arbol de la finca, con su politica de riego");
        System.out.print(finca.describir(0));
        System.out.println("  (cada lote arranca con la estrategia conservadora)");

        titulo("PRUEBA 2 - Estrategia por defecto: AHORRO DE AGUA");
        System.out.println("  Humedad 30 %, umbral 45 % -> deficit de 15 puntos");
        cafe.monitorear();

        titulo("PRUEBA 3 - La MISMA lectura con estrategia INTENSIVA");
        cafe.setEstrategiaRiego(new EstrategiaIntensiva());
        cafe.monitorear();
        System.out.println("  El sensor no cambio: cambio la politica. El lote no se modifico.");

        titulo("PRUEBA 4 - La MISMA lectura consultando el servicio de clima");
        ProveedorClima climaNorte = new AdaptadorClimaExterno(new ServicioClimaExterno(), "EST-NORTE");
        cafe.setEstrategiaRiego(new EstrategiaSegunClima(climaNorte));
        System.out.printf("  Clima: %.1f C y %.1f %% de humedad ambiente%n",
                climaNorte.obtenerTemperatura().getValor(), climaNorte.obtenerHumedad().getValor());
        cafe.monitorear();
        System.out.println("  Hace calor (>29 C): la misma estrategia riega un 50 % mas.");

        titulo("PRUEBA 5 - Un dia lluvioso: la estrategia decide NO regar");
        ProveedorClima climaLluvia = new ClimaFijo("EST-LLUVIA", 22.0, 78.0);
        cafe.setEstrategiaRiego(new EstrategiaSegunClima(climaLluvia));
        cafe.monitorear();
        System.out.println("  El suelo sigue seco, pero regar ahora seria desperdiciar agua.");

        titulo("PRUEBA 6 - El ESTADO manda sobre la ESTRATEGIA");
        cafe.setEstrategiaRiego(new EstrategiaIntensiva());
        cafe.madurar();
        cafe.cosechar();                       // queda en barbecho
        System.out.println("  Estrategia puesta: INTENSIVA. Estado: " + cafe.getEstado().getNombre());
        cafe.monitorear();
        System.out.println("  El State decide SI se riega; la Strategy, solo CUANTO.");

        titulo("PRUEBA 7 - Comparativa: un mismo lote, cuatro politicas");
        comparar(maiz, new EstrategiaAhorroAgua());
        comparar(maiz, new EstrategiaIntensiva());
        comparar(maiz, new EstrategiaSegunClima(climaNorte));
        comparar(maiz, new EstrategiaSegunClima(climaLluvia));

        separador("PARTE 2 - TEMPLATE METHOD: predecir la cosecha");

        double[] historico = { 8.0, 9.5, 11.0, 12.5 };   // toneladas, cuatro cosechas
        ProveedorClima climaSeco = new ClimaFijo("EST-SECA", 31.0, 30.0);

        titulo("PRUEBA 8 - Tres modelos, el MISMO esqueleto de cuatro pasos");
        System.out.println("  Historico del lote Maiz-Sur: 8.0, 9.5, 11.0, 12.5 t");
        ModeloPrediccion[] modelos = {
                new PrediccionPromedioMovil(3),
                new PrediccionTendenciaLineal(),
                new PrediccionPorEstacion(climaSeco, 1.10)
        };
        for (ModeloPrediccion modelo : modelos) {
            System.out.println("\n    > " + modelo.getNombre());
            Prediccion p = modelo.estimar(maiz, historico);
            p.imprimirTraza();
            System.out.printf("      = %.2f t netas%n", p.getToneladasNetas());
        }
        System.out.println("\n  Los cuatro pasos aparecen en el mismo orden en los tres modelos.");
        System.out.println("  Lo unico que cambia es el paso 2, que es el hueco abstracto.");

        titulo("PRUEBA 9 - El GANCHO: solo un modelo lo usa");
        ModeloPrediccion porEstacionSeco   = new PrediccionPorEstacion(climaSeco, 1.10);
        ModeloPrediccion porEstacionHumedo = new PrediccionPorEstacion(new ClimaFijo("EST-HUMEDA", 24.0, 70.0), 1.10);
        System.out.println("  " + porEstacionSeco.estimar(maiz, historico));
        System.out.println("  " + porEstacionHumedo.estimar(maiz, historico));
        System.out.println("  " + new PrediccionPromedioMovil(3).estimar(maiz, historico));
        System.out.println("  Mismo modelo y mismo historico: el gancho castiga la sequia (x0.90)");
        System.out.println("  y premia la temporada humeda (x1.05). El promedio movil no lo usa.");

        titulo("PRUEBA 10 - La plantilla protege sus pasos fijos");
        intentar("historico vacio",    () -> new PrediccionPromedioMovil(3).estimar(maiz, new double[0]));
        intentar("cosecha negativa",   () -> new PrediccionPromedioMovil(3).estimar(maiz, new double[]{8.0, -2.0}));
        System.out.println("  Ningun modelo puede saltarse la validacion ni la merma: estan en");
        System.out.println("  metodos privados y estimar() es final.");

        System.out.println("\n  Un modelo nuevo, escrito al final de este archivo, solo define el paso 2:");
        Prediccion optimista = new ModeloOptimista().estimar(maiz, historico);
        optimista.imprimirTraza();
        System.out.printf("      = %.2f t netas  (y la merma se le aplico igual)%n",
                optimista.getToneladasNetas());

        separador("FIN DE LA DEMOSTRACION");
    }

    // ------------------------------------------------------------------
    //  Auxiliares de la demo
    // ------------------------------------------------------------------

    /** Aplica una estrategia al lote y reporta los litros resultantes. */
    private static void comparar(Lote lote, EstrategiaRiego estrategia) {
        lote.setEstrategiaRiego(estrategia);
        Lectura humedad = null;
        for (Sensor s : lote.getSensores()) {
            if (s.getTipo().equals("HUMEDAD")) humedad = s.leer();
        }
        int minutos = estrategia.calcularMinutos(humedad, lote.getUmbrales());
        System.out.printf("  %-44s -> %2d min%n", estrategia.getNombre(), minutos);
    }

    /** Ejecuta algo que debe fallar y muestra el mensaje de la plantilla. */
    private static void intentar(String caso, Runnable accion) {
        try {
            accion.run();
            System.out.printf("  %-20s -> NO fallo (inesperado)%n", caso);
        } catch (IllegalArgumentException e) {
            System.out.printf("  %-20s -> rechazado: %s%n", caso, e.getMessage());
        }
    }

    private static void titulo(String texto) {
        System.out.println("\n" + texto);
        System.out.println("-".repeat(texto.length()));
    }

    private static void separador(String texto) {
        System.out.println("\n" + "=".repeat(68));
        System.out.println("  " + texto);
        System.out.println("=".repeat(68));
    }

    /**
     * MODELO NUEVO escrito dentro de la demo, para mostrar lo que cuesta
     * extender un Template Method: una clase, un método, cinco líneas.
     *
     * No valida, no aplica merma y no consulta el clima, y aun así su
     * resultado pasa por la validación y por la merma, porque esos pasos
     * los pone la plantilla.
     */
    static class ModeloOptimista extends ModeloPrediccion {

        @Override
        protected double calcularBase(double[] historico) {
            double maximo = 0;
            for (double t : historico) maximo = Math.max(maximo, t);
            return maximo;                 // apuesta por la mejor cosecha histórica
        }

        @Override
        public String getNombre() { return "OPTIMISTA (nuevo)"; }
    }
}
