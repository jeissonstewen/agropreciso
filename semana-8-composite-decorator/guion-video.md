# Guion del video — Semana 8: Composite y Decorator (≈2 min)

Grabar pantalla con audio. Preparar antes: el editor con `src/` abierto y una terminal en
`semana-8-composite-decorator/`. Compilar una vez **antes** de grabar.

## Parte 1 — Composite (0:00 – 0:55)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | Terminal: `java MainDemoCompositeDecorator`, prueba 1 (el árbol) | "Semana 8, dos patrones estructurales. Una finca no es una lista de lotes: es un árbol de zonas, y una zona puede contener otras. Esto es la Finca La Esperanza con su invernadero y su campo abierto." |
| 0:12–0:25 | `ComponenteFinca.java` | "Composite empieza con una interfaz común a las dos cosas del árbol: el lote individual y la zona que agrupa. Área, número de sensores, monitorear. Fíjense que no declara agregar ni quitar: eso es a propósito." |
| 0:25–0:40 | `ZonaCultivo.java`, señalar `getAreaHectareas()` y `contarSensores()` | "El compuesto no calcula nada: recorre sus hijos y combina. Y como un hijo puede ser otra zona, la recursión se propaga sola hasta las hojas. Agregar un nivel de sectores no cuesta una línea." |
| 0:40–0:55 | Terminal: pruebas 2 y 3 | "Los totales suben por el árbol: 9.5 hectáreas y 5 sensores en la finca. Y el mismo método recibe un lote suelto, una zona o la finca entera sin un solo if. Esa uniformidad es la promesa del patrón." |

## Parte 2 — Decorator (0:55 – 1:45)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:55–1:08 | `SensorDecorador.java`, señalar la declaración y el campo | "Segundo patrón: Decorator. Miren las dos relaciones: implementa Sensor, y además tiene un Sensor. Es un sensor y envuelve un sensor. Eso es todo el patrón." |
| 1:08–1:20 | Los tres decoradores, rápido | "Tres responsabilidades que no son del sensor sino de la instalación: calibración, que altera el valor; validación, que lo sustituye si es imposible; y registro, que no lo toca, solo deja constancia. Por herencia serían una clase por cada mezcla." |
| 1:20–1:32 | Terminal: prueba 6 | "Se apilan. Este sensor averiado devuelve −40 por ciento: validamos, calibramos y registramos, y sale un valor válido. La cadena completa se imprime abajo." |
| 1:32–1:45 | `test/SensorDecoradorTest.java`, prueba del orden | "Y el orden importa: validar y luego calibrar da 2 por ciento; al revés da 0. El patrón deja expresar esa diferencia eligiendo el orden, sin clases nuevas. Hay una prueba dedicada." |

## Cierre — Integración y pruebas (1:45 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:45–1:54 | Terminal: prueba 7 | "Los dos juntos: la finca monitorea en cascada y, al llegar al lote vigilado, aparecen el valor calibrado y el LOG. Ni la zona ni el lote saben que hay decoradores: para ellos sigue siendo un Sensor." |
| 1:54–2:00 | Terminal: `./pruebas.sh` | "Veinte pruebas unitarias con JUnit 5, todas en verde." |

## Comandos exactos

```
cd semana-8-composite-decorator/src && javac *.java && java MainDemoCompositeDecorator
```

```
cd semana-8-composite-decorator && ./pruebas.sh
```

## Recomendaciones

- Los dos planos que no se pueden recortar son el 0:25–0:40 (la recursión del compuesto) y el
  0:55–1:08 (la doble relación del decorador). Si falta tiempo, recortar el repaso de los tres
  decoradores.
- Terminal con fuente grande y `clear` antes de cada ejecución.
