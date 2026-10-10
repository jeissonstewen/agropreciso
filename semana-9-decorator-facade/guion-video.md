# Guion del video — Semana 9: Decorator y Facade (≈2 min)

Grabar pantalla con audio. Preparar antes: el editor con `src/` abierto y una terminal en
`semana-9-decorator-facade/`. Compilar una vez **antes** de grabar.

## Parte 1 — Decorator sobre el clima (0:00 – 0:50)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | `AdaptadorClimaExterno.java` (de la Semana 7) | "Semana 9. El adaptador del clima funciona, pero el servicio es una API de terceros: cada llamada cuesta y a veces se cae. Esas dos cosas no son problema del adaptador: su trabajo es traducir unidades." |
| 0:12–0:24 | `ProveedorClimaDecorador.java` | "Así que usamos Decorator otra vez, pero sobre otra interfaz. En la Semana 8 decoramos sensores; aquí, el proveedor de clima. Misma estructura: es un ProveedorClima y tiene un ProveedorClima." |
| 0:24–0:38 | `ClimaConCache.java` y `ClimaConReintento.java` | "Y aquí está la diferencia con la semana pasada: allá los decoradores alteraban el dato. Estos dos no tocan el valor. El caché cambia cuántas veces se pide; el reintento, qué pasa cuando falla. Decorator no es para transformar datos: es para añadir responsabilidades." |
| 0:38–0:50 | Terminal: pruebas 1 a 3 | "Sin caché, cuatro llamadas reales al servicio. Con caché, dos. Y el reintento absorbe dos caídas seguidas y devuelve el dato igual." |

## Parte 2 — Facade (0:50 – 1:45)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:50–1:05 | Diagrama `diagramas/uml-facade.png` | "Segundo patrón: Facade. Mírenlo: a la izquierda el cliente con una flecha, a la derecha ocho patrones de ocho semanas. Hacer la jornada de la finca exigía conocer todo eso: unas treinta líneas de ensamblaje." |
| 1:05–1:20 | `FachadaAgroPrecision.java`, los métodos públicos | "La fachada expone seis operaciones que hablan de jornadas, fincas y reportes, no de builders ni de salidas. Por dentro orquesta los Singleton, el árbol Composite, los sensores decorados, el Adapter del clima y el Bridge de los reportes." |
| 1:20–1:32 | Terminal: prueba 6 | "Una jornada completa: iniciar, consultar clima, monitorear la finca y emitir el reporte. Cuatro llamadas." |
| 1:32–1:45 | `getFinca()` en el código, luego terminal prueba 8 | "Y algo importante que el GoF subraya: la fachada no encierra el subsistema. getFinca devuelve el árbol para quien necesite más. Simplificar no es amurallar." |

## Cierre — Integración y pruebas (1:45 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:45–1:54 | Señalar el constructor de la fachada en la prueba 6 | "Los dos patrones juntos: la fachada recibe el clima ya decorado con caché y reintento, y no lo sabe. Para ella es la misma interfaz de la Semana 7." |
| 1:54–2:00 | Terminal: `./pruebas.sh` | "Veintidós pruebas unitarias, todas en verde." |

## Comandos exactos

```
cd semana-9-decorator-facade/src && javac *.java && java MainDemoDecoratorFacade
```

```
cd semana-9-decorator-facade && ./pruebas.sh
```

## Recomendaciones

- El plano que no se puede recortar es el 0:24–0:38: es lo que distingue este Decorator del de la
  Semana 8 y evita que parezca repetido.
- En la parte de Facade, el diagrama vende el patrón mejor que el código. Mostrarlo primero.
- Terminal con fuente grande y `clear` antes de cada ejecución.
