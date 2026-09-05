<div align="center">

# Patrones de Software

### 🌱 AgroPrecisión — Sistema de Agricultura de Precisión

**Estudiantes:** Jeisson Stewen Berdugo Cely · Darwin Felipe Gil López
**Grupo:** E-195

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Tipo](https://img.shields.io/badge/Aplicación-Consola-blue)](#-cómo-ejecutar)
[![Datos](https://img.shields.io/badge/Datos-Simulados%20(sin%20BD)-lightgrey)](#-alcance-y-restricciones)
[![Patrones](https://img.shields.io/badge/Patrones-2%2F12-yellow)](#-hoja-de-ruta-de-patrones)

</div>

---

Prototipo académico de un **sistema de agricultura de precisión** desarrollado en **Java puro**,
con datos simulados (*dummy*) y sin base de datos. Su propósito no es ser un producto completo,
sino **evidenciar la aplicación práctica de un patrón de diseño distinto en cada entrega**
de la asignatura **Patrones de Software**.

| | |
|---|---|
| **Asignatura** | Patrones de Software |
| **Grupo** | E-195 |
| **Estudiantes** | Jeisson Stewen Berdugo Cely · Darwin Felipe Gil López |
| **Lenguaje** | Java 17+ (compatible con JDK 8+) |

---

## 📑 Tabla de contenido

- [Objetivo general](#-objetivo-general)
- [Alcance y restricciones](#-alcance-y-restricciones)
- [Estructura del repositorio](#-estructura-del-repositorio)
- [Cómo ejecutar](#-cómo-ejecutar)
- [Entregables](#-entregables)
  - [Entrega 1 — Code smells, punto 4](#entrega-1--análisis-de-code-smells-punto-4)
  - [Entrega 2 — Code smells, puntos 11, 13, 15 y 20](#entrega-2--análisis-de-code-smells-puntos-11-13-15-y-20)
  - [Entrega 3 — Singleton](#entrega-3--patrón-singleton)
  - [Entrega 4 — Factory Method](#entrega-4--patrón-factory-method)
- [Hoja de ruta de patrones](#-hoja-de-ruta-de-patrones)
- [Módulos del sistema](#-módulos-del-sistema)

---

## 🎯 Objetivo general

Desarrollar un prototipo funcional de un sistema de agricultura de precisión, en Java y con
datos simulados, que permita el monitoreo de cultivos, el riego automatizado, la gestión de
inventario y la predicción de cosechas, con el fin de evidenciar la aplicación práctica de
patrones de diseño de software en cada componente del sistema.

📄 Objetivos específicos, modelo de dominio y diseño completo:
**[`docs/01-objetivos-y-diseno.md`](docs/01-objetivos-y-diseno.md)**

## 📌 Alcance y restricciones

- Aplicación de **consola**, en **Java puro**: sin GUI, sin frameworks, sin Maven ni Gradle.
- **Sin base de datos**: los datos están quemados en código, en listas en memoria o son aleatorios.
- **Alcance mínimo intencional**: en cada entrega se implementa lo más pequeño que permita
  demostrar el patrón con claridad.
- Código **comentado en español**, pensado para ser sustentado ante el docente.

## 📂 Estructura del repositorio

Cada entrega vive en su propia carpeta, con su código, sus evidencias y su explicación.

```
agroprecision/
├── README.md                          Este archivo
├── .gitignore
├── docs/
│   └── 01-objetivos-y-diseno.md       Objetivos y diseño del sistema
├── entrega-01-code-smells/            Análisis de code smells (punto 4)
├── entrega-02-code-smells/            Análisis de code smells (puntos 11, 13, 15 y 20)
├── entrega-03-singleton/              Patrón Singleton
│   ├── README.md                      Explicación y evidencias
│   ├── src/                           Código fuente
│   └── evidencias/                    Capturas de las pruebas
└── entrega-04-factory-method/         Patrón Factory Method
    ├── README.md
    ├── src/
    └── evidencias/
```

| Carpeta | Contenido |
|---|---|
| [`docs/`](docs/) | Objetivos y diseño general del sistema |
| [`entrega-01-code-smells/`](entrega-01-code-smells/) | Entrega 1 — Análisis de code smells, punto 4 |
| [`entrega-02-code-smells/`](entrega-02-code-smells/) | Entrega 2 — Análisis de code smells, puntos 11, 13, 15 y 20 |
| [`entrega-03-singleton/`](entrega-03-singleton/) | Entrega 3 — Singleton en sesión y configuración |
| [`entrega-04-factory-method/`](entrega-04-factory-method/) | Entrega 4 — Factory Method en el módulo de sensores |

## ▶️ Cómo ejecutar

Cada entrega es independiente: se compila y se ejecuta dentro de su propia carpeta.

```bash
cd entrega-03-singleton/src && javac *.java && java MainDemoSingleton
```

```bash
cd entrega-04-factory-method/src && javac *.java && java MainDemoFactory
```

> Requiere **JDK 17 o superior** (también funciona con JDK 8+). El código no usa paquetes.
> `SesionUsuario` y `ConfiguracionSistema` aparecen copiadas en la entrega 4 para que esa
> carpeta compile por sí sola; el original y su explicación están en la entrega 3.

---

## 📦 Entregables

Cada entrega tiene su propia carpeta con el documento que la sustenta. Las entregas 1 y 2 son
análisis de *code smells*; de la 3 en adelante son implementaciones de patrones, y cada carpeta
incluye además su código y las evidencias de las pruebas ejecutadas.

| Entrega | Tema | Alcance | Documento | Código | Evidencias | Video |
|---|---|---|---|---|---|---|
| 1 | Análisis de code smells | Punto 4 | [Ver](entrega-01-code-smells/) | — | — | — |
| 2 | Análisis de code smells | Puntos 11, 13, 15 y 20 | [Ver](entrega-02-code-smells/) | — | — | — |
| 3 | Singleton | Sesión y configuración | [Ver](entrega-03-singleton/) | [Ver](entrega-03-singleton/src/) | [6 capturas](entrega-03-singleton/evidencias/) | _(pendiente)_ |
| 4 | Factory Method | Sensores | [Ver](entrega-04-factory-method/) | [Ver](entrega-04-factory-method/src/) | [6 capturas](entrega-04-factory-method/evidencias/) | _(pendiente)_ |

### Entrega 1 — Análisis de code smells (punto 4)

Análisis del método `Transferir()` de `BancoService`. El olor principal es **Feature Envy**: el
método usa más los datos y métodos de `Cuenta` que los propios, así que la lógica está ubicada
donde no viven los datos. Alrededor aparecen **Anemic Domain Model**, **Inappropriate Intimacy**
y violaciones de *Tell, Don't Ask* y del principio de responsabilidad única.

📄 **[Documento completo de la Entrega 1](entrega-01-code-smells/)**

### Entrega 2 — Análisis de code smells (puntos 11, 13, 15 y 20)

Cuatro fragmentos más: **Long Parameter List** con *Primitive Obsession* y *Data Clumps* (punto
11), **Comments** que repiten o contradicen al código (punto 13), **Speculative Generality** en
un validador sobreingenierizado (punto 15) e **Inappropriate Intimacy** con un método `validar…`
que muta estado ajeno y provoca un `null` en tiempo de ejecución (punto 20).

📄 **[Documento completo de la Entrega 2](entrega-02-code-smells/)**

### Entrega 3 — Patrón Singleton

Garantiza que exista **una sola sesión de usuario y una sola configuración de finca** en toda la
aplicación, con un punto de acceso global. Se implementaron las dos variantes del patrón:
`SesionUsuario` con inicialización perezosa y `synchronized`, y `ConfiguracionSistema` con
inicialización temprana mediante `static final`.

Resuelve el problema de tener que propagar esos objetos por parámetro a todas las clases, y el
de que dos módulos trabajen con estados distintos. La prueba más contundente es que el
constructor privado impide compilar un `new SesionUsuario()`: la unicidad la garantiza el
compilador, no una convención.

📄 **[Documento completo de la Entrega 3](entrega-03-singleton/)** — explicación, código y las
seis capturas de las pruebas.

### Entrega 4 — Patrón Factory Method

Traslada la decisión de **qué sensor construir** desde el código cliente hacia una jerarquía de
creadores. `CreadorSensor` declara el *factory method* `crearSensor()` como abstracto y define
la operación `tomarLectura()`, que trabaja contra la interfaz `Sensor` sin conocer las clases
concretas. Cada creador concreto aporta una sola línea.

Elimina los `if/else` de creación que de otro modo se repetirían en riego, reportes y predicción.
La demostración más clara es que se agregó un cuarto sensor —luminosidad— y el código cliente no
cambió ni una línea.

📄 **[Documento completo de la Entrega 4](entrega-04-factory-method/)** — recoge también la
Entrega 3, con la explicación de ambos patrones y las doce capturas.

---

## 🗺️ Hoja de ruta de patrones

La numeración de los patrones es la del proyecto; la columna *Entrega* indica en qué entrega
del curso se sustentó cada uno.

| # | Patrón | Tipo | Módulo | Entrega | Estado |
|---|---|---|---|---|---|
| 1 | **Singleton** | Creacional | Sesión y configuración | 3 | ✅ Completado |
| 2 | **Factory Method** | Creacional | Sensores | 4 | ✅ Completado |
| 3 | Builder | Creacional | Reportes | — | ⏳ Pendiente |
| 4 | Strategy | Comportamiento | Riego | — | ⏳ Pendiente |
| 5 | Observer | Comportamiento | Alertas | — | ⏳ Pendiente |
| 6 | Decorator | Estructural | Lecturas | — | ⏳ Pendiente |
| 7 | Adapter | Estructural | Servicio de clima externo | — | ⏳ Pendiente |
| 8 | Facade | Estructural | Menú de consola | — | ⏳ Pendiente |
| 9 | Command | Comportamiento | Acciones del menú | — | ⏳ Pendiente |
| 10 | Template Method | Comportamiento | Predicción | — | ⏳ Pendiente |
| 11 | State | Comportamiento | Estados del lote | — | ⏳ Pendiente |
| 12 | DAO / Repository | Arquitectural | Persistencia simulada | — | ⏳ Pendiente |

## 🧩 Módulos del sistema

| Módulo | Responsabilidad |
|---|---|
| `sesion` | Login simple, usuario y rol activos |
| `config` | Umbrales globales (humedad, cadena de frío, unidades) |
| `sensores` | Lecturas de humedad, temperatura y pH ("drones e IoT") |
| `riego` | Decide y ejecuta el riego según reglas de clima |
| `inventario` | Insumos, productos cosechados, cadena de frío |
| `prediccion` | Estimación de cosecha con datos históricos |
| `reportes` | Salida por consola de estado y alertas |

---

<div align="center">

**Universitaria Tecnológica de Santander**

<sub>Patrones de Software · Grupo E-195</sub>

</div>
