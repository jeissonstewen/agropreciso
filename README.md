<div align="center">

# Patrones de Software

### 🌱 AgroPrecisión — Sistema de Agricultura de Precisión

**Estudiantes:** Jeisson Stewen Berdugo Cely · Darwin Felipe Gil López
**Grupo:** E-195

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Tipo](https://img.shields.io/badge/Aplicación-Consola-blue)](#-cómo-ejecutar)
[![Datos](https://img.shields.io/badge/Datos-Simulados%20(sin%20BD)-lightgrey)](#-alcance-y-restricciones)
[![Patrones](https://img.shields.io/badge/Patrones-7%2F15-yellow)](#-hoja-de-ruta-de-patrones)

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
  - [Semana 1 — Code smells, punto 4](#semana-1--análisis-de-code-smells-punto-4)
  - [Semana 2 — Code smells, puntos 11, 13, 15 y 20](#semana-2--análisis-de-code-smells-puntos-11-13-15-y-20)
  - [Semana 3 — Singleton](#semana-3--patrón-singleton)
  - [Semana 4 — Factory Method](#semana-4--patrón-factory-method)
  - [Semana 5 — Builder](#semana-5--patrón-builder)
  - [Semana 6 — Abstract Factory y Prototype](#semana-6--patrones-abstract-factory-y-prototype)
  - [Semana 7 — Adapter y Bridge](#semana-7--patrones-adapter-y-bridge)
- [Hoja de ruta de patrones](#-hoja-de-ruta-de-patrones)
- [Módulos del sistema](#-módulos-del-sistema)

---

## 🎯 Objetivo general

Desarrollar un prototipo funcional de un sistema de agricultura de precisión, en Java y con
datos simulados, que permita el monitoreo de cultivos, el riego automatizado, la gestión de
inventario y la predicción de cosechas, con el fin de evidenciar la aplicación práctica de
patrones de diseño de software en cada componente del sistema.

📄 Objetivos específicos y desarrollo completo de cada semana:
**[`Patrones-de-Software-E195.docx`](Patrones-de-Software-E195.docx)**

## 📌 Alcance y restricciones

- Aplicación de **consola**, en **Java puro**: sin GUI, sin frameworks, sin Maven ni Gradle.
- **Sin base de datos**: los datos están quemados en código, en listas en memoria o son aleatorios.
- **Alcance mínimo intencional**: en cada entrega se implementa lo más pequeño que permita
  demostrar el patrón con claridad.
- Código **comentado en español**, pensado para ser sustentado ante el docente.

## 📂 Estructura del repositorio

Cada semana vive en su propia carpeta, con su código, sus evidencias y su explicación.

```
agroprecision/
├── README.md                          Este archivo
├── Patrones-de-Software-E195.docx     Documento Word acumulativo del proyecto (todas las semanas)
├── .gitignore
├── semana-1-code-smells/            Análisis de code smells (punto 4)
├── semana-2-code-smells/            Análisis de code smells (puntos 11, 13, 15 y 20)
├── semana-3-singleton/              Patrón Singleton
│   ├── README.md                      Explicación y evidencias
│   ├── src/                           Código fuente
│   └── evidencias/                    Capturas de las pruebas
├── semana-4-factory-method/         Patrón Factory Method
│   ├── README.md
│   ├── src/
│   └── evidencias/
├── semana-5-builder/                Patrón Builder
│   ├── README.md
│   ├── src/
│   └── evidencias/
├── semana-6-abstract-factory-prototype/   Patrones Abstract Factory y Prototype
│   ├── README.md
│   ├── src/
│   ├── diagramas/                  Diagramas UML (fuente .mmd y .png)
│   └── evidencias/
├── semana-7-adapter-bridge/        Patrones Adapter y Bridge
│   ├── README.md
│   ├── src/
│   ├── test/                       Pruebas unitarias con JUnit 5
│   ├── diagramas/
│   └── evidencias/
└── lib/                            junit-platform-console-standalone (JUnit 5)
```

| Carpeta / archivo | Contenido |
|---|---|
| [`Patrones-de-Software-E195.docx`](Patrones-de-Software-E195.docx) | Documento Word acumulativo: objetivos y todas las semanas |
| [`semana-1-code-smells/`](semana-1-code-smells/) | Semana 1 — Análisis de code smells, punto 4 |
| [`semana-2-code-smells/`](semana-2-code-smells/) | Semana 2 — Análisis de code smells, puntos 11, 13, 15 y 20 |
| [`semana-3-singleton/`](semana-3-singleton/) | Semana 3 — Singleton en sesión y configuración |
| [`semana-4-factory-method/`](semana-4-factory-method/) | Semana 4 — Factory Method en el módulo de sensores |
| [`semana-5-builder/`](semana-5-builder/) | Semana 5 — Builder en el módulo de reportes |
| [`semana-6-abstract-factory-prototype/`](semana-6-abstract-factory-prototype/) | Semana 6 — Abstract Factory (equipamiento por zona) y Prototype (plantillas de lote) |
| [`semana-7-adapter-bridge/`](semana-7-adapter-bridge/) | Semana 7 — Adapter (clima externo) y Bridge (formatos de reporte), con pruebas unitarias |
| [`lib/`](lib/) | JUnit 5 (jar autónomo) para las pruebas unitarias |

## ▶️ Cómo ejecutar

Cada semana es independiente: se compila y se ejecuta dentro de su propia carpeta.

```bash
cd semana-3-singleton/src && javac *.java && java MainDemoSingleton
```

```bash
cd semana-4-factory-method/src && javac *.java && java MainDemoFactory
```

```bash
cd semana-5-builder/src && javac *.java && java MainDemoBuilder
```

```bash
cd semana-6-abstract-factory-prototype/src && javac *.java && java MainDemoAbstractFactoryPrototype
```

```bash
cd semana-7-adapter-bridge/src && javac *.java && java MainDemoAdapterBridge
```

Las pruebas unitarias de la Semana 7 se ejecutan con JUnit 5, sin Maven ni Gradle:

```bash
cd semana-7-adapter-bridge && ./pruebas.sh
```

> Requiere **JDK 17 o superior** (también funciona con JDK 8+). El código no usa paquetes.
> Las clases de semanas anteriores aparecen copiadas en las semanas siguientes para que cada
> carpeta compile por sí sola; el original y su explicación están en la semana donde se crearon.

---

## 📦 Entregables

Cada semana tiene su propia carpeta con el documento que la sustenta. Las semanas 1 y 2 son
análisis de *code smells*; de la 3 en adelante son implementaciones de patrones, y cada carpeta
incluye además su código y las evidencias de las pruebas ejecutadas.

| Semana | Tema | Alcance | Documento | Código | Evidencias | Video |
|---|---|---|---|---|---|---|
| 1 | Análisis de code smells | Punto 4 | [Ver](semana-1-code-smells/) | — | — | — |
| 2 | Análisis de code smells | Puntos 11, 13, 15 y 20 | [Ver](semana-2-code-smells/) | — | — | — |
| 3 | Singleton | Sesión y configuración | [Ver](semana-3-singleton/) | [Ver](semana-3-singleton/src/) | [6 capturas](semana-3-singleton/evidencias/) | _(pendiente)_ |
| 4 | Factory Method | Sensores | [Ver](semana-4-factory-method/) | [Ver](semana-4-factory-method/src/) | [6 capturas](semana-4-factory-method/evidencias/) | _(pendiente)_ |
| 5 | Builder | Reportes | [Ver](semana-5-builder/) | [Ver](semana-5-builder/src/) | [6 capturas](semana-5-builder/evidencias/) | _(pendiente)_ |
| 6 | Abstract Factory · Prototype | Equipamiento y lotes | [Ver](semana-6-abstract-factory-prototype/) | [Ver](semana-6-abstract-factory-prototype/src/) | [6 capturas](semana-6-abstract-factory-prototype/evidencias/) | _(pendiente)_ |
| 7 | Adapter · Bridge | Clima externo y reportes | [Ver](semana-7-adapter-bridge/) | [Ver](semana-7-adapter-bridge/src/) | [6 capturas](semana-7-adapter-bridge/evidencias/) | _(pendiente)_ |

### Semana 1 — Análisis de code smells (punto 4)

Análisis del método `Transferir()` de `BancoService`. El olor principal es **Feature Envy**: el
método usa más los datos y métodos de `Cuenta` que los propios, así que la lógica está ubicada
donde no viven los datos. Alrededor aparecen **Anemic Domain Model**, **Inappropriate Intimacy**
y violaciones de *Tell, Don't Ask* y del principio de responsabilidad única.

📄 **[Documento completo de la Semana 1](semana-1-code-smells/)**

### Semana 2 — Análisis de code smells (puntos 11, 13, 15 y 20)

Cuatro fragmentos más: **Long Parameter List** con *Primitive Obsession* y *Data Clumps* (punto
11), **Comments** que repiten o contradicen al código (punto 13), **Speculative Generality** en
un validador sobreingenierizado (punto 15) e **Inappropriate Intimacy** con un método `validar…`
que muta estado ajeno y provoca un `null` en tiempo de ejecución (punto 20).

📄 **[Documento completo de la Semana 2](semana-2-code-smells/)**

### Semana 3 — Patrón Singleton

Garantiza que exista **una sola sesión de usuario y una sola configuración de finca** en toda la
aplicación, con un punto de acceso global. Se implementaron las dos variantes del patrón:
`SesionUsuario` con inicialización perezosa y `synchronized`, y `ConfiguracionSistema` con
inicialización temprana mediante `static final`.

Resuelve el problema de tener que propagar esos objetos por parámetro a todas las clases, y el
de que dos módulos trabajen con estados distintos. La prueba más contundente es que el
constructor privado impide compilar un `new SesionUsuario()`: la unicidad la garantiza el
compilador, no una convención.

📄 **[Documento completo de la Semana 3](semana-3-singleton/)** — explicación, código y las
seis capturas de las pruebas.

### Semana 4 — Patrón Factory Method

Traslada la decisión de **qué sensor construir** desde el código cliente hacia una jerarquía de
creadores. `CreadorSensor` declara el *factory method* `crearSensor()` como abstracto y define
la operación `tomarLectura()`, que trabaja contra la interfaz `Sensor` sin conocer las clases
concretas. Cada creador concreto aporta una sola línea.

Elimina los `if/else` de creación que de otro modo se repetirían en riego, reportes y predicción.
La demostración más clara es que se agregó un cuarto sensor —luminosidad— y el código cliente no
cambió ni una línea.

📄 **[Documento completo de la Semana 4](semana-4-factory-method/)** — recoge también la
Semana 3, con la explicación de ambos patrones y las doce capturas.

### Semana 5 — Patrón Builder

Separa la **construcción de un reporte de finca** de su representación. `ReporteFinca` es un
producto inmutable con secciones opcionales (encabezado, lecturas, alertas, inventario, resumen);
`ReporteBuilder` declara un paso por sección y `ReporteConsolaBuilder` los implementa, valida y
crea el producto de una sola vez. `DirectorReportes` guarda las recetas: el reporte de turno del
operario y el gerencial.

Resuelve el constructor de siete parámetros —la mitad opcionales— que de otro modo exigiría el
reporte, y conecta las tres semanas: el encabezado sale de los Singleton de la Semana 3 y las
lecturas de las fábricas de la Semana 4.

📄 **[Documento completo de la Semana 5](semana-5-builder/)** — explicación, código y las seis
capturas de las pruebas.

### Semana 6 — Patrones Abstract Factory y Prototype

**Abstract Factory** produce familias completas de equipamiento coherentes con la zona:
`FabricaInvernadero` entrega sensores de rango estrecho y riego por goteo; `FabricaCampoAbierto`,
sensores de rango amplio y aspersor. `EstacionMonitoreo` recibe una `FabricaEquipamiento` y arma
la estación sin un solo `new` concreto: mezclar familias es imposible por construcción.

**Prototype** convierte un `Lote` ya equipado en plantilla: `CatalogoLotes` la guarda bajo una
clave y entrega clones con nombre y área propios. `Lote.clone()` hace copia profunda de los
umbrales y de la lista de sensores, así que modificar un clon nunca altera la plantilla.

📄 **[Documento completo de la Semana 6](semana-6-abstract-factory-prototype/)** — explicación de
ambos patrones, código y las seis capturas de las pruebas.

### Semana 7 — Patrones Adapter y Bridge

**Adapter** integra un servicio meteorológico de un tercero que devuelve un `String` con la
temperatura en Fahrenheit y la humedad como fracción. `AdaptadorClimaExterno` implementa nuestra
interfaz `ProveedorClima` y compone al servicio, adaptando formato, unidades y tipo de retorno.
`MonitorClima` decide el riego sin distinguir si los datos vienen de un sensor propio o de la API.

**Bridge** separa qué se reporta de cómo se escribe: la jerarquía `Reporte` (estado, alertas) y la
jerarquía `SalidaReporte` (consola, Markdown, CSV) varían por separado, unidas por una sola
referencia. Seis combinaciones con cinco clases en lugar de seis.

Es la primera semana con **pruebas unitarias**: 13 casos con JUnit 5, incluido uno que agrega un
formato nuevo sin tocar la jerarquía de reportes.

📄 **[Documento completo de la Semana 7](semana-7-adapter-bridge/)** — explicación de ambos
patrones, diagramas UML, pruebas unitarias y las seis capturas.

---

## 🗺️ Hoja de ruta de patrones

La numeración de los patrones es la del proyecto; la columna *Semana* indica en qué semana
del curso se sustentó cada uno.

| # | Patrón | Tipo | Módulo | Semana | Estado |
|---|---|---|---|---|---|
| 1 | **Singleton** | Creacional | Sesión y configuración | 3 | ✅ Completado |
| 2 | **Factory Method** | Creacional | Sensores | 4 | ✅ Completado |
| 3 | **Builder** | Creacional | Reportes | 5 | ✅ Completado |
| 4 | **Abstract Factory** | Creacional | Equipamiento por zona | 6 | ✅ Completado |
| 5 | **Prototype** | Creacional | Plantillas de lote | 6 | ✅ Completado |
| 6 | **Adapter** | Estructural | Servicio de clima externo | 7 | ✅ Completado |
| 7 | **Bridge** | Estructural | Formatos de reporte | 7 | ✅ Completado |
| 8 | Strategy | Comportamiento | Riego | — | ⏳ Pendiente |
| 9 | Observer | Comportamiento | Alertas | — | ⏳ Pendiente |
| 10 | Decorator | Estructural | Lecturas | — | ⏳ Pendiente |
| 11 | Facade | Estructural | Menú de consola | — | ⏳ Pendiente |
| 12 | Command | Comportamiento | Acciones del menú | — | ⏳ Pendiente |
| 13 | Template Method | Comportamiento | Predicción | — | ⏳ Pendiente |
| 14 | State | Comportamiento | Estados del lote | — | ⏳ Pendiente |
| 15 | DAO / Repository | Arquitectural | Persistencia simulada | — | ⏳ Pendiente |

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
| `lote` | Parcelas de cultivo: equipamiento, umbrales propios y plantillas |

---

<div align="center">

**Universitaria Tecnológica de Santander**

<sub>Patrones de Software · Grupo E-195</sub>

</div>
