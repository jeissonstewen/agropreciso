# Semana 2 — Análisis de code smells (puntos 11, 13, 15 y 20)

**Asignatura:** Patrones de Software
**Autores:** Darwin Felipe Gil López · Jeisson Stewen Berdugo Cely

Análisis de los puntos restantes del taller de *code smells*. La semana 1 cubrió el
[punto 4](../semana-1-code-smells/); aquí se analizan los cuatro fragmentos siguientes.

| Punto | Código analizado | Smell principal |
|---|---|---|
| [11](#punto-11--long-parameter-list) | `Registrar()` y `GuardarUsuario()` | Long Parameter List |
| [13](#punto-13--comments) | Clase `Calculadora` | Comments (comentarios como ruido) |
| [15](#punto-15--speculative-generality) | `ValidadorGenerico` y `ValidadorSimple` | Speculative Generality |
| [20](#punto-20--inappropriate-intimacy) | `SesionManager`, `ValidadorSesion`, `ProcesadorPedido` | Inappropriate Intimacy |

---

## Punto 11 — Long Parameter List

**Smell principal: Long Parameter List (lista larga de parámetros)**

`Registrar()` recibe 7 parámetros y `GuardarUsuario()` otros 7, todos `string` sueltos. Es fácil
equivocarse en el orden: nombre y apellido son intercambiables sin que el compilador se queje, y
lo mismo ocurre con DNI y teléfono. El error no se detecta al compilar, sino en producción.

### Smells asociados

| Smell | Cómo se manifiesta |
|---|---|
| **Primitive Obsession** | Email, DNI, teléfono y fecha se modelan como `string` en vez de tipos propios (`Email`, `Dni`, `Telefono`) que garanticen su propia validez. |
| **Data Clumps** | Los mismos parámetros viajan juntos en ambos métodos, señal de que en realidad forman un objeto: `DatosUsuario`. |
| **Long Method** | `Registrar` valida y persiste en el mismo lugar; toda la lógica de validación está *inline* en un solo método. |
| **Código duplicado** | Las validaciones de DNI y teléfono son idénticas: longitud 10 y solo dígitos. |

---

## Punto 13 — Comments

**Smell principal: Comments (comentarios redundantes o ruido)**

Los comentarios no aportan información: repiten lo que el código ya dice de forma evidente, como
`return a + b; // Suma los dos parámetros` o `return numero * numero; // ¡OBVIO!`. Según Fowler,
el comentario suele ser un *desodorante* que tapa otros olores; aquí el código ya es claro y el
comentario es puro ruido.

### Problemas asociados

| Problema | Cómo se manifiesta |
|---|---|
| **Comentarios mentirosos o desactualizados** | El Javadoc de `sumar` afirma que "no maneja números negativos", pero `a + b` sí los maneja. La documentación contradice al código, lo que es peor que no tener comentario. |
| **Comentarios exagerados o engañosos** | `cuadrado` se describe como "muy compleja" cuando es una sola multiplicación. `esEmailValido` se declara "VALIDACIÓN CRÍTICA" y "sin ella el sistema colapsa", cuando es una comprobación trivial (`contains("@")`) que ni siquiera valida un email correctamente. |
| **Metadatos en comentarios** | "REVISADO POR: Juan Pérez 15/03/2024" es información que corresponde al control de versiones, no al código fuente. |
| **Nombre de clase inadecuado** | `Calculadora` contiene `esEmailValido`, que no tiene relación con cálculos: baja cohesión. |

---

## Punto 15 — Speculative Generality

**Smell principal: Speculative Generality (generalidad especulativa) / sobreingeniería**

`ValidadorGenerico` construye una infraestructura completa —mapa de *lambdas*, registro dinámico
de reglas, resolución por clave en tiempo de ejecución— para un problema que `ValidadorSimple`
resuelve en una línea.

La flexibilidad de `agregarRegla` se construyó "por si algún día se necesita", y el contraste
entre las dos clases lo evidencia: el mismo chequeo de email existe en ambas versiones, una en
cuatro líneas y otra sobre una arquitectura genérica que nadie pidió.

El costo es real: más código que mantener, más difícil de leer y de depurar, y una abstracción
que hay que entender antes de poder tocar una validación trivial.

---

## Punto 20 — Inappropriate Intimacy

**Smell principal: Inappropriate Intimacy (intimidad inapropiada)**

`ValidadorSesion` y `ProcesadorPedido` acceden y modifican directamente los campos internos de
`SesionManager.Sesion` (`usuarioTemp`, `ultimaValidacion`).

Peor aún: el validador **muta estado ajeno** (`s.usuarioTemp = null`) dentro de un método que
solo debería consultar. De ahí viene el `null` inesperado en tiempo de ejecución que sufre
`ProcesadorPedido`, un error difícil de rastrear porque el daño lo causa una clase distinta a la
que falla.

### Smells asociados

| Smell | Cómo se manifiesta |
|---|---|
| **Efecto secundario oculto** | Un método llamado `validar…` cambia el estado del objeto. El nombre miente sobre lo que hace. |
| **Encapsulamiento roto** | Los campos son `private`, pero se usan desde fuera: la clase anidada expone su interior a todo el módulo. Ni siquiera hay *getters* o *setters* que controlen el acceso. |
