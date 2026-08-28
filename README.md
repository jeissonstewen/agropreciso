<div align="center">

# 🌱 AgroPrecisión

**Sistema de Agricultura de Precisión — prototipo académico en Java**

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Tipo](https://img.shields.io/badge/Aplicación-Consola-blue)](#-cómo-ejecutar)
[![Datos](https://img.shields.io/badge/Datos-Simulados%20(sin%20BD)-lightgrey)](#-alcance-y-restricciones)
[![Avance](https://img.shields.io/badge/Entregas-1%2F12-yellow)](#-hoja-de-ruta-de-patrones)

</div>

---

Prototipo académico de un **sistema de agricultura de precisión** desarrollado en **Java puro**,
con datos simulados (*dummy*) y sin base de datos. Su propósito no es ser un producto completo,
sino **evidenciar la aplicación práctica de un patrón de diseño distinto en cada entrega**
de la asignatura **Patrones de Software**.

| | |
|---|---|
| **Asignatura** | Patrones de Software |
| **Autores** | Darwin Felipe Gil López · Jeisson Stewen Berdugo Cely |
| **Lenguaje** | Java 17+ (compatible con JDK 8+) |

---

## 📑 Tabla de contenido

- [Objetivo general](#-objetivo-general)
- [Alcance y restricciones](#-alcance-y-restricciones)
- [Estructura del repositorio](#-estructura-del-repositorio)
- [Cómo ejecutar](#-cómo-ejecutar)
- [Entregables](#-entregables)
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

```
agroprecision/
├── README.md                     Este archivo
├── .gitignore
├── docs/                         Documentación de diseño
│   └── 01-objetivos-y-diseno.md
├── src/                          Código fuente Java
│   ├── SesionUsuario.java
│   ├── ConfiguracionSistema.java
│   └── MainDemoSingleton.java
└── evidencias/                   Capturas de las pruebas ejecutadas
    └── entrega-01-singleton/
```

| Carpeta | Contenido |
|---|---|
| [`docs/`](docs/) | Objetivos y diseño del sistema |
| [`src/`](src/) | Código fuente Java, sin paquetes, compilable con `javac *.java` |
| [`evidencias/`](evidencias/) | Capturas de pantalla de las pruebas ejecutadas |

## ▶️ Cómo ejecutar

```bash
cd src
javac *.java
java MainDemoSingleton
```

> Requiere **JDK 17 o superior** (también funciona con JDK 8+).
> El código no usa paquetes, por lo que se compila completo con un solo `javac *.java`.

---

## 📦 Entregables

Cada entrega incluye el **código** donde se evidencia el patrón, la **actualización del diseño**
si aplica, las **evidencias** de las pruebas ejecutadas y el **video** con la explicación.

### Entrega 1 — Patrón Singleton

| Entregable | Enlace |
|---|---|
| 💻 Código fuente | [`SesionUsuario.java`](src/SesionUsuario.java) · [`ConfiguracionSistema.java`](src/ConfiguracionSistema.java) · [`MainDemoSingleton.java`](src/MainDemoSingleton.java) |
| 📐 Diseño | [`docs/01-objetivos-y-diseno.md`](docs/01-objetivos-y-diseno.md#5-entrega-1--patrón-singleton) |
| 📸 Evidencias | [`evidencias/entrega-01-singleton/`](evidencias/entrega-01-singleton/) |
| 🎬 Video de explicación | _(pendiente de publicar en YouTube)_ |

#### Evidencia del patrón

**Problema que resuelve:** el sistema opera con **un único usuario autenticado** y **una única
configuración de finca**, y los módulos de riego, inventario y reportes necesitan consultarlos.
Sin Singleton habría que pasar esos objetos por parámetro a todas las clases, o arriesgarse a
tener dos sesiones con estados distintos.

**Solución:** `SesionUsuario` (variante *lazy*, con `synchronized`) y `ConfiguracionSistema`
(variante *eager*, con `static final`) garantizan una sola instancia y un punto de acceso global.

Los tres elementos del patrón, en [`SesionUsuario.java`](src/SesionUsuario.java):

```java
public final class SesionUsuario {

    // 1. Atributo estático privado: la única instancia
    private static SesionUsuario instancia;

    // 2. Constructor privado: nadie puede hacer "new SesionUsuario()"
    private SesionUsuario() {
        this.activa = false;
    }

    // 3. Punto de acceso global. 'synchronized' evita dos instancias con hilos.
    public static synchronized SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }
}
```

El módulo cliente **no recibe la sesión por parámetro**, la obtiene del punto de acceso global
(ver `ModuloRiego` en [`MainDemoSingleton.java`](src/MainDemoSingleton.java)):

```java
public void ejecutarRiego() {
    SesionUsuario sesion = SesionUsuario.getInstancia();
    ConfiguracionSistema cfg = ConfiguracionSistema.getInstancia();
    ...
}
```

**Cómo se comprueba al ejecutar:**

| Evidencia en consola | Qué demuestra |
|---|---|
| `s1 == s2 ? true` y `hashCode` idénticos | Existe una sola instancia |
| `Desde s2 el usuario es: ingeniero` | El login hecho con `s1` es visible desde `s2`: estado global |
| `Umbral leído por c2: 45.0` | El cambio de configuración hecho con `c1` lo ve `c2` |
| `¿Sesión activa según s1? false` | Cerrar sesión desde `s2` afecta a `s1` y al módulo de riego |
| `error: SesionUsuario() has private access` | El constructor privado **impide** crear una segunda instancia: lo garantiza el compilador, no una convención |

**Ventajas:** instancia única garantizada, acceso global sin propagar parámetros, estado
consistente entre módulos.
**Desventajas:** introduce estado global, dificulta las pruebas unitarias y puede ocultar
dependencias. Por eso se limita a sesión y configuración, y no a los módulos de negocio.

### Resumen de entregas

| # | Patrón | Código | Evidencias | Video |
|---|---|---|---|---|
| 1 | Singleton | [Ver](src/) | [Ver](evidencias/entrega-01-singleton/) | _(pendiente)_ |

---

## 🗺️ Hoja de ruta de patrones

| # | Patrón | Tipo | Módulo | Estado |
|---|---|---|---|---|
| 1 | **Singleton** | Creacional | Sesión y configuración | ✅ Completado |
| 2 | Factory Method | Creacional | Sensores | ⏳ Pendiente |
| 3 | Builder | Creacional | Reportes | ⏳ Pendiente |
| 4 | Strategy | Comportamiento | Riego | ⏳ Pendiente |
| 5 | Observer | Comportamiento | Alertas | ⏳ Pendiente |
| 6 | Decorator | Estructural | Lecturas | ⏳ Pendiente |
| 7 | Adapter | Estructural | Servicio de clima externo | ⏳ Pendiente |
| 8 | Facade | Estructural | Menú de consola | ⏳ Pendiente |
| 9 | Command | Comportamiento | Acciones del menú | ⏳ Pendiente |
| 10 | Template Method | Comportamiento | Predicción | ⏳ Pendiente |
| 11 | State | Comportamiento | Estados del lote | ⏳ Pendiente |
| 12 | DAO / Repository | Arquitectural | Persistencia simulada | ⏳ Pendiente |

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
<sub>Proyecto académico · Asignatura Patrones de Software</sub>
</div>
