# Guion del video — Semana 14: Observer, State y Visitor (≈2:30)

Son tres patrones, así que el video es algo más largo. Grabar pantalla con audio; compilar una vez
**antes** de grabar.

## Parte 1 — State (0:00 – 0:50)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | Terminal: pruebas 1 y 2 | "Semana 14, tres patrones de comportamiento. Miren: el mismo lote, el mismo sensor marcando 64 por ciento. En preparación no se riega; después de sembrar, sí. Lo que cambió no es la lectura: es el estado." |
| 0:12–0:28 | `EstadoBase.java` | "Eso es State. Cada estado es una clase, y esta base rechaza todas las transiciones por defecto. Cada estado concreto sobreescribe solo la que sí permite, así que olvidarse de prohibir algo es imposible: lo prohibido es el valor por defecto." |
| 0:28–0:40 | `Sembrado.java` (cinco líneas) | "Un estado concreto ocupa esto: qué nombre tiene, si admite riego, y a cuál transita. Nada más." |
| 0:40–0:50 | Terminal: pruebas 3 y 4 | "Una transición inválida se rechaza nombrando el lote y su estado, y no deja el objeto a medias. Y el ciclo se cierra: sembrar, madurar, cosechar, preparar suelo, sin un solo if." |

## Parte 2 — Observer (0:50 – 1:35)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:50–1:05 | `MonitorDeAlertas.java`, señalar la lista y `publicar()` | "Segundo patrón: Observer. El sujeto guarda una lista de INTERFACES. No sabe cuántos observadores hay ni quiénes son: solo recorre y avisa." |
| 1:05–1:20 | Terminal: pruebas 5 y 6 | "Tres suscriptores reciben la misma alerta. Quito el panel: deja de imprimir, pero el historial sigue acumulando. Nadie tuvo que modificar el monitor." |
| 1:20–1:35 | Terminal: prueba 7 | "Y sin ningún suscrito el monitor detecta igual las dos anomalías: simplemente nadie las escucha. Si preguntan la diferencia con el Mediator de la semana pasada: allí el centro conocía a cada colega y decidía a quién le tocaba; aquí el sujeto no conoce a nadie y difunde lo mismo a todos." |

## Parte 3 — Visitor (1:35 – 2:20)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:35–1:50 | `ComponenteFinca.java`, señalar las cinco operaciones | "Tercer patrón, y el problema está en nuestro propio código. Esta interfaz empezó con tres operaciones en la Semana 8 y hoy tiene cinco. Cada una obligó a tocar tres archivos: la interfaz, ZonaCultivo y Lote." |
| 1:50–2:05 | `VisitanteFinca.java` y `VisitanteCostoRiego.java` | "El Visitor saca las operaciones afuera. Agregamos aceptar una sola vez al árbol, y de ahí en adelante una operación nueva es solo una clase nueva. Este calcula el costo del riego, y fíjense que pregunta si el lote admite riego: ahí se engancha con el State." |
| 2:05–2:20 | Terminal: pruebas 10 y 11 | "El CSV lo produce un visitante que escribimos al final, sin tocar nada. Y aquí está la integración de los tres: antes de cosechar, 190 litros estimados; después de cosechar —que deja el lote en barbecho— el mismo visitante sobre el mismo árbol estima 40." |

## Cierre (2:20 – 2:30)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 2:20–2:30 | Terminal: `./pruebas.sh` | "Veintiséis pruebas en verde, incluida una que define un visitante dentro de la propia prueba y funciona sin que el árbol sepa que existe." |

## Comandos exactos

```
cd semana-14-observer-state-visitor/src && javac *.java && java MainDemoObserverStateVisitor
```

```
cd semana-14-observer-state-visitor && ./pruebas.sh
```

## Recomendaciones

- Si hay que recortar, la Parte 2 es la más prescindible: Observer es el patrón que el docente ya
  conoce de memoria. Los dos planos que no se tocan son el 0:12–0:28 (lo prohibido por defecto) y
  el 1:35–1:50 (las cinco operaciones acumuladas).
- La frase que cierra la semana: **State cambia, Observer avisa, Visitor reporta.**
- Terminal con fuente grande y `clear` antes de cada ejecución.
