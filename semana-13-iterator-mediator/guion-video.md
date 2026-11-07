# Guion del video — Semana 13: Iterator y Mediator (≈2 min)

Grabar pantalla con audio. Preparar antes: el editor con `src/` abierto y una terminal en
`semana-13-iterator-mediator/`. Compilar una vez **antes** de grabar.

## Parte 1 — Iterator (0:00 – 0:55)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | Terminal: prueba 1 (el árbol impreso) | "Semana 13. Desde la Semana 8 la finca es un árbol: zonas dentro de zonas, y lotes al final. Para operar sobre todos los lotes hay que recorrerlo." |
| 0:12–0:24 | `ZonaCultivo.monitorear()` de la Semana 8 | "Hasta ahora el recorrido vivía dentro de la estructura, en este bucle recursivo. Funciona, pero el orden lo decide la estructura y es siempre el mismo. Eso es, de hecho, un iterador interno." |
| 0:24–0:40 | `IteradorEnProfundidad.java` y `IteradorPorNiveles.java` lado a lado | "El patrón Iterator saca el recorrido afuera. Estos dos iteradores se diferencian en una sola cosa: uno guarda lo pendiente en una pila y el otro en una cola. Nada más." |
| 0:40–0:55 | Terminal: pruebas 2, 3 y 5 | "Y el resultado es otro orden sobre el mismo árbol. Fíjense que ni ZonaCultivo ni Lote cambiaron. Y la prueba 5: dos recorridos avanzando a la vez sin estorbarse, porque cada uno lleva su propio estado." |

## Parte 2 — Mediator (0:55 – 1:45)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:55–1:08 | Diagrama `diagramas/uml-mediator.png` | "Segundo patrón: Mediator. Miren el diagrama: todas las flechas de los colegas apuntan al centro, y ninguna apunta a otro colega. Lo importante de este dibujo es lo que no está." |
| 1:08–1:20 | `CentroDeOperaciones.notificar()` | "Aquí está el enrutamiento. Cada colega informa de un hecho —una anomalía, una orden ejecutada— y el centro decide a quién le toca seguir. Los colegas informan; el mediador decide." |
| 1:20–1:35 | Terminal: prueba 6 | "En ejecución se lee el flujo entero: la estación avisa, el centro manda escalar, la cadena de la semana pasada atiende, el centro manda ejecutar, y al final manda registrar en bitácora. Nadie llamó a nadie directamente." |
| 1:35–1:45 | Terminal: prueba 9 | "Y aquí quito la bitácora del centro: el resto sigue funcionando igual. Agregar o quitar un colega no toca a los demás." |

## Cierre — Integración y pruebas (1:45 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:45–1:54 | `EstacionSensores.inspeccionar()` | "Los dos patrones se encuentran en este método: la estación recorre la finca sin saber cómo está organizada, y avisa sin saber quién atenderá. Dos ignorancias a propósito, una por patrón." |
| 1:54–2:00 | Terminal: `./pruebas.sh` | "Veintiún pruebas en verde, incluida una que verifica por reflexión que ningún colega guarda referencia a otro." |

## Comandos exactos

```
cd semana-13-iterator-mediator/src && javac *.java && java MainDemoIteratorMediator
```

```
cd semana-13-iterator-mediator && ./pruebas.sh
```

## Recomendaciones

- El plano imprescindible es el 0:24–0:40: pila contra cola es toda la idea del Iterator.
- Si preguntan la diferencia con la fachada de la Semana 9: **la fachada es unidireccional y el
  subsistema no la conoce; el mediador es bidireccional y los colegas sí lo conocen.**
- Terminal con fuente grande y `clear` antes de cada ejecución.
