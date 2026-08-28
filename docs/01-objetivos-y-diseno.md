# AgroPrecisión — Sistema de Agricultura de Precisión (proyecto académico)

Proyecto del sector agricultura para la asignatura **Patrones de Software**.
Alcance educativo: aplicación de consola en Java, datos simulados (dummy) en memoria, sin base de datos.

---

## 1. Objetivo general

Desarrollar un prototipo funcional de un sistema de agricultura de precisión, en Java y con datos simulados, que permita el monitoreo de cultivos, el riego automatizado, la gestión de inventario y la predicción de cosechas, con el fin de evidenciar la aplicación práctica de patrones de diseño de software en cada componente del sistema.

## 2. Objetivos específicos

1. **Diseñar** la arquitectura básica del prototipo (módulos, clases y responsabilidades), identificando en cada módulo el patrón de diseño más apropiado y justificando su elección.
2. **Implementar** de forma incremental los módulos del sistema (sesión y configuración, sensores, riego, inventario y predicción), aplicando en cada entrega un patrón de diseño distinto (creacional, estructural o de comportamiento).
3. **Verificar** el funcionamiento de cada patrón implementado mediante pruebas de escritorio y casos de uso ejecutables en consola, documentando el comportamiento esperado frente al obtenido.

---

## 3. Diseño básico del sistema

### 3.1 Módulos

| Módulo | Responsabilidad | Datos dummy |
|---|---|---|
| `sesion` | Login simple y datos del usuario activo | 2–3 usuarios quemados en código |
| `config` | Parámetros globales (finca, unidades, umbrales) | valores por defecto |
| `sensores` | Lecturas de humedad, temperatura, pH desde "drones/IoT" | valores aleatorios o lista fija |
| `riego` | Decide y ejecuta el riego según reglas de clima | reglas simples if/else o estrategias |
| `inventario` | Insumos y productos cosechados, cadena de frío | lista en memoria (`ArrayList`) |
| `prediccion` | Estimación de cosecha con datos históricos | promedio simple sobre arreglo fijo |
| `reportes` | Salida por consola de estado y alertas | — |

### 3.2 Modelo de dominio mínimo

- `Usuario` (usuario, rol)
- `Cultivo` (id, nombre, hectáreas, lote)
- `Lectura` (idSensor, tipo, valor, fecha)
- `Insumo` (código, nombre, cantidad, temperaturaRequerida)
- `RegistroCosecha` (cultivo, año, toneladas)

### 3.3 Flujo general

```
Login (Sesión) -> Menú principal
                   |-- 1. Ver lecturas de sensores
                   |-- 2. Evaluar y ejecutar riego
                   |-- 3. Gestionar inventario / cadena de frío
                   |-- 4. Predecir cosecha
                   |-- 5. Ver reportes y alertas
                   |-- 0. Cerrar sesión
```

---

## 4. Hoja de ruta de patrones (una entrega por semana)

| # | Patrón | Tipo | Dónde se aplica |
|---|---|---|---|
| 1 | **Singleton** | Creacional | `SesionUsuario` (sesión única activa) y/o `ConfiguracionSistema` |
| 2 | **Factory Method** | Creacional | `SensorFactory` crea sensores de humedad, temperatura o pH |
| 3 | **Builder** | Creacional | Construcción del `ReporteCosecha` con secciones opcionales |
| 4 | **Strategy** | Comportamiento | Reglas de riego: `RiegoPorHumedad`, `RiegoPorPronostico`, `RiegoProgramado` |
| 5 | **Observer** | Comportamiento | Alertas: sensor emite lectura crítica y notifica a riego, inventario y reportes |
| 6 | **Decorator** | Estructural | Enriquecer una lectura con calibración, filtrado de ruido o unidades |
| 7 | **Adapter** | Estructural | Adaptar un "servicio de clima externo" (clase con interfaz distinta) al sistema |
| 8 | **Facade** | Estructural | `AgroFacade` expone operaciones simples al menú de consola |
| 9 | **Command** | Comportamiento | Acciones del menú como objetos, con historial y deshacer |
| 10 | **Template Method** | Comportamiento | Esqueleto del proceso de predicción de cosecha |
| 11 | **State** | Comportamiento | Estados del lote: `Sembrado -> EnCrecimiento -> Listo -> Cosechado` |
| 12 | **DAO / Repository** | Arquitectural | Persistencia simulada en memoria, intercambiable a futuro |

> Cada semana se entrega: el fragmento de código del patrón, el guion explicativo y la prueba ejecutada.

---

## 5. Entrega 1 — Patrón Singleton

**Ubicación:** clase `SesionUsuario` del módulo `sesion`.

**Justificación:** el sistema opera con **un único usuario autenticado a la vez**. Muchos módulos (riego, inventario, reportes) necesitan saber quién está operando y con qué rol, para registrar la acción y validar permisos. Sin Singleton habría que pasar el objeto sesión por parámetro a todas las clases, o correr el riesgo de tener dos sesiones inconsistentes. El Singleton garantiza un único punto de acceso global y una sola instancia en toda la aplicación.

**Alternativa complementaria:** `ConfiguracionSistema`, que guarda los umbrales de humedad y temperatura usados por varios módulos. Misma justificación (estado global único).

Ver archivos: `SesionUsuario.java`, `ConfiguracionSistema.java`, `MainDemoSingleton.java`.
