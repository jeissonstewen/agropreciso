# Guion del video — Semana 7: Adapter y Bridge (≈2 min)

Grabar pantalla con audio. Preparar antes: el editor con `src/` abierto y una terminal en
`semana-7-adapter-bridge/`. Compilar una vez **antes** de grabar para que en la toma no se pierdan
segundos.

## Parte 1 — Adapter (0:00 – 1:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | `ServicioClimaExterno.java` | "Semana 7, dos patrones estructurales. Este es un servicio meteorológico de un tercero: no lo controlamos. Devuelve un String con pares clave-valor, la temperatura en Fahrenheit y la humedad como fracción de 0 a 1." |
| 0:12–0:22 | `ProveedorClima.java` | "Nuestro sistema quiere otra cosa: objetos `Lectura`, en grados Celsius y en porcentaje. Las dos interfaces son incompatibles." |
| 0:22–0:40 | `AdaptadorClimaExterno.java`, señalar el campo `servicio`, luego `obtenerTemperatura()` y `leerCampo()` | "El adaptador implementa nuestra interfaz y compone al servicio externo. Hace tres adaptaciones: de formato, al parsear la cadena; de unidades, Fahrenheit a Celsius; y de tipo, porque devuelve `Lectura`. Es un adapter de objeto: componemos, no heredamos." |
| 0:40–0:50 | `MonitorClima.java` | "El cliente solo conoce `ProveedorClima`. No tiene una sola línea que distinga si los datos vienen de un sensor propio o de la API externa." |
| 0:50–1:00 | Terminal: `java MainDemoAdapterBridge`, pruebas 1 a 4 | "En ejecución: la respuesta cruda, la traducción a 30.3 grados y 42 por ciento, y el mismo monitor decidiendo el riego con las dos fuentes." |

## Parte 2 — Bridge (1:00 – 1:50)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:00–1:12 | Diagrama `diagramas/uml-bridge.png` | "Segundo patrón: Bridge. Dos jerarquías independientes: qué se reporta y cómo se escribe. Si lo resolviéramos por herencia serían dos reportes por tres formatos: seis clases." |
| 1:12–1:25 | `Reporte.java`, señalar `protected final SalidaReporte salida` | "Aquí está el puente: la abstracción guarda una referencia al implementador. `generar()` es abstracto; esta clase no menciona consola, Markdown ni CSV." |
| 1:25–1:35 | `ReporteEstado.java` y `SalidaCsv.java` lado a lado | "`ReporteEstado` decide el contenido: título, contexto y tabla. `SalidaCsv` decide el formato y hasta entrecomilla los valores con comas. Ninguno sabe del otro." |
| 1:35–1:50 | Terminal: pruebas 5 a 7 | "El mismo reporte sale en los tres formatos, y el reporte de alertas reutiliza esos mismos formatos. Seis combinaciones con cinco clases, no seis." |

## Cierre — Pruebas unitarias (1:50 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:50–2:00 | Terminal: `./pruebas.sh` | "Y esta semana el proyecto trae pruebas unitarias con JUnit 5: trece pruebas, todas en verde. La última crea un formato nuevo dentro de la propia prueba y funciona sin tocar la jerarquía de reportes: esa es la independencia que promete el Bridge." |

## Comandos exactos

```
cd semana-7-adapter-bridge/src && javac *.java && java MainDemoAdapterBridge
```

```
cd semana-7-adapter-bridge && ./pruebas.sh
```

## Recomendaciones

- Terminal con fuente grande y `clear` antes de cada ejecución.
- El plano más importante es el minuto 0:22–0:40 (las tres adaptaciones) y el 1:12–1:25 (el campo
  que hace de puente). Si hay que recortar, recortar el cierre, no esos.
- No leer el código línea por línea: señalar y explicar la idea.
