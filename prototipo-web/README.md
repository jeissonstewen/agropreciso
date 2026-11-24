# Prototipo web — AgroPrecisión

**Patrones de Software** · Grupo E-195
Jeisson Stewen Berdugo Cely · Darwin Felipe Gil López

Interfaz clickeable que muestra para qué sirve el sistema y, en cada acción, **qué patrón
la resuelve en el código Java y por qué ese patrón y no otro**.

Responde al **Objetivo específico 1** —diseñar la arquitectura del prototipo identificando
el patrón adecuado para cada módulo— mostrando esa arquitectura de forma recorrible, y
sirve de apoyo visual para la sustentación de los 21 patrones del catálogo.

---

## Cómo se abre

Doble clic en `index.html`. No necesita servidor, ni internet, ni instalar nada.

Es **un solo archivo autónomo**: HTML, CSS y JavaScript sin librerías externas ni
peticiones de red. Funciona con el protocolo `file://` de cualquier navegador.

### Para enviarlo al docente

Enviar la carpeta del repositorio completa, o al menos `prototipo-web/` **junto a las
carpetas `semana-N-...`**. Todas las rutas son **relativas** (`../semana-15-.../src/...`),
de modo que los enlaces a los archivos de código funcionan sin configurar nada.

Si solo se envía `index.html` suelto, la página sigue funcionando y los fragmentos de
código se siguen viendo —están embebidos en el archivo—; lo único que deja de abrirse es
el enlace al archivo `.java` original.

---

## Qué NO hace, y por qué

**No ejecuta el programa Java.** Reproduce su comportamiento en JavaScript para poder
mostrarlo en un navegador sin compilar nada.

Conectarlo de verdad exigiría un servidor HTTP o un empaquetado, y eso contradice la
restricción del proyecto —Java puro de consola, sin frameworks— sin aportar nada a lo que
se evalúa, que es el uso de los patrones. La decisión está tomada a conciencia y la propia
página lo advierte en su pie, para no dar a entender otra cosa.

Lo que sí es verificable:

- **Los números coinciden con los del Java.** Las fórmulas de riego y de predicción son las
  mismas, y producen la misma salida que las demos: 30 % de humedad con umbral 45 % da
  5 min y 10.0 L con la estrategia de ahorro, 10 min y 20.0 L con la intensiva, y el
  histórico `{8.0, 9.5, 11.0, 12.5}` da 10.12 t, 12.88 t y 9.34 t netas según el modelo.
- **Los fragmentos de código son copias literales** del repositorio, con su archivo y su
  rango de líneas. No se transcribieron a mano: se extrajeron de los `.java` y se comprobó
  que coinciden carácter por carácter.

---

## Mapa de pantallas y patrones

| # | Pantalla | Patrones que se encienden |
|---|---|---|
| 1 | Sesión y acceso | Singleton · Proxy · Facade |
| 2 | La finca | Composite · Iterator · Prototype · Abstract Factory |
| 3 | Sensores | Factory Method · Decorator · *Flyweight (no implementado)* |
| 4 | Riego | State · Strategy |
| 5 | Predicción | Template Method |
| 6 | Operaciones | Chain of Responsibility · Command · Mediator |
| 7 | Reportes y alertas | Builder · Bridge · Observer · Visitor · Adapter |

Los 21 patrones del catálogo están cubiertos, incluido el **Flyweight**, que aparece como
ficha marcada *no implementado* con la medición que sustenta la decisión.

---

## Descarga de los reportes

En la pantalla **Reportes y alertas**, todo lo que se genera queda disponible para
**descargar** o **copiar**:

| Qué | Archivo |
|---|---|
| Reporte de estado o de alertas | `agroprecision-estado-AAAA-MM-DD.txt` / `.md` / `.csv` |
| Bitácora de alertas (Observer) | `agroprecision-alertas-bitacora-AAAA-MM-DD.csv` |
| Inventario (Visitor) | `agroprecision-inventario-AAAA-MM-DD.txt` |
| Costo de riego (Visitor) | `agroprecision-costo-riego-AAAA-MM-DD.txt` |
| Exportar CSV (Visitor) | `agroprecision-finca-AAAA-MM-DD.csv` |

Dos cosas que conviene saber:

- **Los datos no son fijos.** El reporte lee el árbol de la finca tal como esté en ese momento,
  así que lo que se clone con el Prototype o se equipe con la Abstract Factory en la pantalla
  «La finca» aparece en el archivo descargado.
- **El archivo lleva solo el documento**, no el eco de las llamadas Java ni los comentarios que
  se ven en la consola. Son dos cosas distintas: la consola narra, el archivo informa.

Esto además hace visible el **Bridge**: el mismo reporte se descarga en tres formatos, y cada
salida decide cómo escribirlo. `SalidaCsv` descarta el título y el pie —un CSV con texto suelto
antes de la cabecera se abre mal en una hoja de cálculo— mientras que `SalidaConsola` alinea en
columnas y `SalidaMarkdown` arma una tabla. Esa decisión es de la salida, no del reporte.

Si el navegador impide la descarga —puede ocurrir con `file://` en algunos navegadores, sobre
todo Safari—, el botón **¿No se descargó?** muestra el texto completo para copiarlo a mano, y el
botón **Copiar** lo lleva directamente al portapapeles.

## Atajos de teclado

| Tecla | Efecto |
|---|---|
| `1` … `7` | Saltar a ese módulo |
| `←` `→` | Módulo anterior / siguiente |

Se ignoran mientras se escribe en un campo.

## Cómo se usa en la sustentación

Cada pantalla sugiere un recorrido concreto en la línea «Pruebe esto».

El panel de la derecha es el que vale para explicar. Cada acción enciende los patrones que
intervienen, apaga los demás y abre el primero encendido; en la ficha se ve:

1. **El problema que resuelve** — la situación concreta de AgroPrecisión, no la definición
   de libro.
2. **Por qué este patrón** — qué se ganó y por qué no otra solución.
3. **La frontera** — con qué otro patrón se confunde y dónde está la línea.
4. **Dónde está en el código** — archivo, líneas y el fragmento real.

Tres recorridos que se explican solos:

- **Riego → poner el lote en barbecho con la política intensiva puesta.** Se enciende solo
  el State: el lote no se riega aunque esté seco, y la estrategia sigue puesta sin haber
  llegado a ejecutarse. Es la frontera de la Semana 15 hecha visible.
- **Sensores → marcar calibración y validación, leer, invertir el orden y volver a leer.**
  Con la lectura cruda en 99 %, calibrar y luego validar da 100.0 %; validar y luego calibrar
  da 101.5 %, una humedad imposible. El orden de las envolturas importa.
- **La finca → recorrer en profundidad y luego por niveles.** El mismo árbol da dos órdenes
  distintos, porque uno usa pila y el otro cola.

---

## Mantenimiento

Si cambia el código Java, los fragmentos de `index.html` quedan desactualizados. Están en
la constante `FRAGMENTOS`, al inicio del bloque de scripts, cada uno con su archivo y su
rango de líneas, de modo que se pueden volver a copiar del `.java` correspondiente.
