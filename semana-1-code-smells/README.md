# Semana 1 — Análisis de code smells (punto 4)

**Asignatura:** Patrones de Software
**Autores:** Darwin Felipe Gil López · Jeisson Stewen Berdugo Cely

Análisis del **punto 4** del taller de *code smells*: identificar los olores de código presentes
en el fragmento asignado y explicar por qué constituyen un problema de diseño.

**Código analizado:** el método `Transferir()` de la clase `BancoService`, junto con la clase
`Cuenta`.

---

## Smell principal: Feature Envy (envidia de funcionalidad)

`BancoService.Transferir()` está más interesado en los datos y métodos de la clase `Cuenta` que
en los propios. Prácticamente todas sus líneas son llamadas a `origen.*` y `destino.*`:
`GetSaldoActual`, `SetSaldoTemporal`, `AgregarSaldo`, `RegistrarMovimiento`,
`ValidarLimitesDiarios`, entre otras.

El síntoma es claro: **la lógica vive en el servicio, pero los datos viven en `Cuenta`**. Cuando
un método usa más los datos de otra clase que los suyos, esa lógica está ubicada en el lugar
equivocado.

## Smells asociados

| Smell | Cómo se manifiesta en el fragmento |
|---|---|
| **Anemic Domain Model** | `Cuenta` es solo un contenedor de *getters* y *setters*, con más de 20 métodos expuestos y sin comportamiento propio. Es una estructura de datos disfrazada de objeto. |
| **Inappropriate Intimacy** | El servicio conoce demasiados detalles internos de `Cuenta`: el saldo temporal, el historial, las políticas. Depende de su implementación, no de su interfaz. |
| **Violación de Tell, Don't Ask** | El método pregunta el saldo, calcula por fuera y devuelve el resultado, en lugar de ordenarle a la cuenta que se debite a sí misma. |
| **Violación del SRP** | Un solo método mezcla débito, comisión, historial, auditoría y cumplimiento antilavado. Tiene cinco razones distintas para cambiar. |

## Por qué importa

El conjunto de estos olores produce un diseño frágil. Cualquier cambio en la representación
interna de `Cuenta` —cómo se guarda el saldo, cómo se registra un movimiento— obliga a modificar
`BancoService`, aunque la regla de negocio no haya cambiado. Y como la lógica está fuera del
objeto que posee los datos, nada impide que otro servicio manipule una cuenta de forma distinta e
inconsistente.
