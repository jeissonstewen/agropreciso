# Guion del video — Semana 10: Proxy y Flyweight (≈2 min)

Grabar pantalla con audio. Preparar antes: el editor con `src/` abierto y una terminal en
`semana-10-proxy-flyweight/`. Compilar una vez **antes** de grabar.

## Parte 1 — Proxy de protección (0:00 – 1:05)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 0:00–0:12 | `SesionUsuario.java` (Semana 3), señalar `tienePermiso()` | "Semana 10. Desde la Semana 3 el sistema distingue tres roles y tiene un método de permisos… que nadie usaba. Un operario de riego podía emitir reportes gerenciales y cerrar la jornada." |
| 0:12–0:25 | `SistemaAgroPrecision.java` | "Para arreglarlo extraemos una interfaz de la fachada de la Semana 9. Fíjense en el Javadoc: decidimos a propósito no declarar getFinca, y ahora les cuento por qué." |
| 0:25–0:42 | `ProxyAccesoSistema.java`, señalar `exigir()` | "El proxy implementa esa misma interfaz y envuelve a la fachada. Antes de delegar comprueba el rol. Si no alcanza, lanza una excepción y la llamada nunca llega al subsistema. Esa es la diferencia con el Decorator: mismo esqueleto, pero un decorador añade comportamiento, un proxy decide si la llamada ocurre." |
| 0:42–1:05 | Terminal: pruebas 1 a 6 | "En ejecución: sin sesión no pasa nada; el operario monitorea pero no reporta; el agrónomo sí reporta; el administrador puede todo. Y el mensaje dice siempre qué rol tiene el usuario y cuál haría falta." |

## El conflicto Facade / Proxy (1:05 – 1:20)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:05–1:20 | Terminal: prueba 9, y el Javadoc de la interfaz | "Y aquí está lo interesante de la semana. La fachada expone getFinca a propósito: 'simplificar no es encerrar'. Pero eso abre un agujero: con el árbol en la mano te saltas el control de acceso. Los dos patrones tiran en direcciones opuestas en el mismo punto. Lo resolvimos dejando getFinca fuera del contrato: quien sostiene la interfaz no alcanza el subsistema." |

## Parte 2 — Flyweight: por qué NO (1:20 – 2:00)

| Tiempo | Qué se ve | Qué se dice |
|---|---|---|
| 1:20–1:32 | Diagrama `diagramas/uml-flyweight-hipotetico.png` | "El segundo patrón es Flyweight, y aquí tomamos la opción de no implementarlo. Este es el diseño que haríamos: TipoSensor como estado intrínseco compartido, y el id de cada sensor como estado extrínseco." |
| 1:32–1:45 | `SensorHumedad.java`, señalar los `static final` | "Primera razón: estos rangos ya son static final, o sea que Java ya los comparte entre todas las instancias. Un flyweight que solo comparta constantes no aporta nada sobre lo que el lenguaje hace." |
| 1:45–2:00 | Terminal: `./medir.sh` | "Y la razón decisiva: lo medimos. El ahorro es de unos 64 bytes por sensor. Para ahorrar un megabyte harían falta dieciséis mil sensores; el prototipo tiene cinco, o sea 320 bytes. Implementarlo hoy sería optimización prematura. En el documento queda escrito el umbral a partir del cual sí: decenas de miles de sensores, o que los rangos dejen de ser constantes y se lean de una base de datos." |

## Comandos exactos

```
cd semana-10-proxy-flyweight/src && javac *.java && java MainDemoProxyFlyweight
```

```
cd semana-10-proxy-flyweight && ./medir.sh
```

```
cd semana-10-proxy-flyweight && ./pruebas.sh
```

## Recomendaciones

- El plano imprescindible es el 1:45–2:00: la medición es lo que convierte "no lo hicimos" en "no
  corresponde hacerlo, y aquí está el número". Si falta tiempo, recortar la parte 1, no esta.
- `./medir.sh` tarda unos segundos por las recolecciones de basura. Ejecutarlo una vez antes de
  grabar para que en la toma no haya esperas largas.
- Terminal con fuente grande y `clear` antes de cada ejecución.
