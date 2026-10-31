# Guion del video — Semana 12: Chain of Responsibility y Command (≈2 min)

Grabar pantalla con audio. Preparar antes: el editor con `src/` abierto y una terminal en
`semana-12-chain-command/`. Compilar una vez **antes** de grabar.

## Parte 1 — Chain of Responsibility (0:00 – 0:55)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | `TipoIncidencia.java` | "Semana 12, los dos primeros patrones de comportamiento. Una incidencia en la finca tiene una severidad: humedad baja es leve, un umbral mal configurado es media, un sensor averiado es crítica. Y cada nivel lo atiende una persona distinta." |
| 0:12–0:30 | `ManejadorIncidencia.java`, señalar `siguiente` y `manejar()` | "Chain of Responsibility: cada manejador guarda una referencia al siguiente, y esa auto-referencia es la cadena. `manejar()` es final a propósito: pregunta si puede atender, y si puede, atiende y se detiene. Si no, escala. Si nadie puede, lo informa." |
| 0:30–0:42 | Terminal: pruebas 1 a 3 | "En ejecución se ve el recorrido: la leve no pasa del operario; la media escala una vez; la crítica atraviesa la cadena entera hasta el administrador." |
| 0:42–0:55 | Terminal: pruebas 4 y 5 | "Y dos casos que importan: si la cadena está incompleta la incidencia queda sin atender y queda constancia. Y si reordeno los enlaces cambia el recorrido, sin tocar una sola línea de los manejadores." |

## Parte 2 — Command (0:55 – 1:45)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:55–1:08 | `Orden.java` | "Segundo patrón: Command. Convierte una petición en un objeto, y eso permite tres cosas que una llamada a un método no permite: pasarla como parámetro, encolarla y deshacerla." |
| 1:08–1:20 | `OrdenCambiarUmbralHumedad.java`, señalar `valorAnterior` | "Aquí está lo que hace posible el deshacer: la orden guarda el valor anterior antes de aplicar el cambio. Sin ese guardado no hay undo." |
| 1:20–1:32 | `OrdenRegar.java`, señalar `esReversible() false` | "Y aquí está la decisión que más me gusta de la semana: regar no se puede deshacer, el agua ya está en el suelo. En vez de fingir una reversión, la orden lo declara, y el panel la rechaza pero la conserva en la bitácora, porque la operación sí ocurrió." |
| 1:32–1:45 | Terminal: pruebas 7 a 10 | "Deshacemos el sensor instalado, deshacemos el umbral, y al intentar deshacer el riego el panel lo rechaza. Después ejecuta las órdenes programadas en orden de llegada e imprime la bitácora." |

## Cierre — La integración (1:45 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:45–1:54 | `ManejadorIncidencia.atender()` y la prueba 6 | "Los dos juntos: la cadena decide quién se hace cargo y qué hay que hacer, pero devuelve la orden en vez de ejecutarla. Quien ejecuta, registra y deshace es el panel. Por eso la cadena se puede probar sin efectos secundarios." |
| 1:54–2:00 | Terminal: `./pruebas.sh` | "Veinticinco pruebas unitarias, todas en verde." |

## Comandos exactos

```
cd semana-12-chain-command/src && javac *.java && java MainDemoChainCommand
```

```
cd semana-12-chain-command && ./pruebas.sh
```

## Recomendaciones

- El plano imprescindible es el 0:12–0:30: la auto-referencia `siguiente` y el `manejar()` final
  son todo el patrón. Si falta tiempo, recortar el cierre.
- Si preguntan por la diferencia con el Decorator, la frase es: **en el Decorator todos los
  eslabones actúan; en la cadena actúa uno solo y el recorrido termina ahí.**
- Terminal con fuente grande y `clear` antes de cada ejecución.
