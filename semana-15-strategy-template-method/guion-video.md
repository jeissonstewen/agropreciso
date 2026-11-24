# Guion del video — Semana 15: Strategy y Template Method (≈2:00)

Dos patrones. Grabar pantalla con audio; compilar una vez **antes** de grabar.

La idea que tiene que quedar: los dos resuelven "varias versiones de un algoritmo", pero con
mecanismos opuestos. Conviene cerrar el video con esa comparación.

## Parte 1 — Strategy (0:00 – 0:55)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | Terminal: pruebas 2 y 3 | "Semana 15. Miren el sensor: treinta por ciento de humedad, umbral cuarenta y cinco, en las dos corridas. La primera aplica diez litros; la segunda, veinte. La lectura no cambió: cambió la política de riego." |
| 0:12–0:28 | `EstrategiaRiego.java`, señalar el comentario del `regar(10)` | "Eso es Strategy. Desde la Semana 6 el lote tenía un diez quemado en el código: regaba diez minutos siempre, a un cafetal bajo invernadero y a un maizal a pleno sol. Ahora la decisión vive en esta interfaz, con un solo método: cuántos minutos." |
| 0:28–0:42 | `EstrategiaSegunClima.java` | "Tres implementaciones. Esta es la interesante: consulta el servicio de clima y puede decidir no regar, aunque el suelo esté seco, porque va a llover. Y fíjense que recibe el clima por constructor: las otras dos estrategias no lo necesitan y no tienen por qué cargarlo." |
| 0:42–0:55 | Terminal: pruebas 5 y 6 | "Ahí está: no riega ahora. Y esta es la frontera con el patrón de la semana pasada: con la estrategia intensiva puesta y el lote en barbecho, no se riega. El State decide SI; la Strategy, solo CUÁNTO; el actuador solo aplica. Tres patrones, tres preguntas distintas." |

## Parte 2 — Template Method (0:55 – 1:45)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:55–1:12 | `ModeloPrediccion.java`, señalar `estimar()` | "Segundo patrón: Template Method, en el módulo de predicción de cosecha. Este método es el esqueleto: validar, calcular la base, ajustar por clima, descontar la merma. Y es final. Ninguna subclase puede cambiar el orden ni saltarse un paso." |
| 1:12–1:24 | Señalar `calcularBase` y `ajustarPorClima` | "De los cuatro pasos, uno es abstracto —es el único que todo modelo está obligado a definir— y otro es un gancho: tiene implementación por defecto y se sobreescribe solo si hace falta. Validación y merma son privados: son política de la finca, no del modelo." |
| 1:24–1:38 | Terminal: prueba 8 | "Tres modelos, el mismo histórico. Miren la traza: los cuatro renglones salen en el mismo orden en los tres. Lo único que cambia son los números del paso dos. Y solo uno de los tres usa el gancho del clima; los otros dos no escribieron una línea." |
| 1:38–1:45 | Terminal: prueba 10 | "Un modelo nuevo son cinco líneas: solo define el paso dos. La validación y la merma se le aplican igual, porque las pone la plantilla." |

## Cierre — la comparación (1:45 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:45–2:00 | La tabla "Strategy vs Template Method" del README | "Los dos resuelven lo mismo: varias versiones de un algoritmo. Strategy lo hace por composición, cambia el algoritmo completo y se puede cambiar en ejecución. Template Method lo hace por herencia, fija el orden y solo deja variar los pasos. Dicho corto: Strategy cambia el qué; Template Method fija el orden y deja variar parte del cómo." |

## Notas para grabar

- Antes de grabar: `cd src && javac *.java`, limpiar la terminal.
- La prueba 7 (la comparativa de cuatro políticas) es la mejor toma si hay que recortar tiempo:
  resume el Strategy en cuatro renglones.
- Si el docente pregunta por qué no se encadenan estrategias: encadenar sería **Decorator**, que ya
  se hizo en las Semanas 8 y 9. Cada estrategia aquí resuelve el problema completa y sola.
