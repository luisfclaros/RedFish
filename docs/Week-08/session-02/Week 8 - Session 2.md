# Week 8 - Sesion 2

## Story mapping, estimacion y compromiso del MVP 2

**Historia de usuario:** HU-016  
**Rama de desarrollo:** `hu-016-dev`  
**Estado:** Aprobada en la rama `hu-016-qa`

---

## 1. Historia de usuario

### HU-016 - Planificar y comprometer el alcance del MVP 2

Como equipo de RedFish, queremos organizar el recorrido del usuario, estimar el
trabajo restante y ordenar sus dependencias, para comprometer un MVP 2
realizable con comunicacion integrada y persistencia confiable.

---

## 2. Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Existe un story map organizado por recorrido y prioridad. |
| CA-02 | La linea de liberacion muestra la rebanada minima de extremo a extremo. |
| CA-03 | El trabajo se prioriza mediante MoSCoW. |
| CA-04 | Las historias comprometidas tienen criterios verificables. |
| CA-05 | Las historias se estiman con puntos relativos Fibonacci. |
| CA-06 | Ninguna historia comprometida conserva un tamano de 8 puntos o mas. |
| CA-07 | Las dependencias entre migraciones, Inventario y Pedidos son visibles. |
| CA-08 | Las dependencias tienen contrato primero y estrategia de mocks. |
| CA-09 | El compromiso utiliza throughput real sin inventar velocidad historica. |
| CA-10 | El plan conserva margen para incertidumbre y defectos. |
| CA-11 | El frontend y capacidades no esenciales quedan bajo la linea. |
| CA-12 | `Qa -> main` ocurre solamente al aprobar la ultima HU del MVP 2. |
| CA-13 | Las HU futuras se identifican como propuestas sujetas a sus guias oficiales. |
| CA-14 | HU-024 se reconoce dentro del MVP 2 y no se libera antes de completarla. |

---

## 3. Estado actual

RedFish ya dispone de:

- Productos y Clientes con REST y MySQL;
- modelos iniciales de Inventario, Pedidos, Despachos y Vehiculos;
- puertos de colaboracion entre Pedidos e Inventario;
- eventos locales con consumo idempotente;
- OpenAPI, Pact y errores estandarizados;
- Docker Compose y ambientes separados;
- CI, QA y un modelo Agile/DevOps.

Inventario y Pedidos todavia no ofrecen un flujo persistente completo. Esta
brecha define el valor principal pendiente del MVP 2.

---

## 4. Story map

La fuente visual y funcional se encuentra en:

```text
docs/planning/mvp-2-story-map.md
```

El backbone es:

```text
Preparar datos
  -> Controlar stock
    -> Registrar pedido
      -> Reservar sin duplicar
        -> Consultar resultado
          -> Validar y liberar
```

La rebanada comienza con un movimiento de entrada y termina con un pedido
consultable cuyo stock fue reservado una sola vez.

---

## 5. Estimacion preliminar de capacidades

| HU | Resultado | Puntos |
|---|---|---:|
| HU-017 | Migraciones versionadas | 5 |
| HU-018 | Existencias y movimientos | 5 |
| HU-019 | Pedidos y detalles persistentes | 5 |
| HU-020 | Reserva transaccional de inventario | 5 |
| HU-021 | Creacion idempotente de pedidos | 3 |
| HU-022 | Propuesta de validacion del incremento | 3 |
| HU-023 | Alcance pendiente de guia oficial | TBD |
| HU-024 | Alcance pendiente de guia oficial; pertenece al MVP 2 | TBD |
|  | **Total preliminar sin HU-023/HU-024** | **26** |

Las capacidades de 8 o mas puntos fueron evitadas separando persistencia,
integracion e idempotencia. Esta distribucion es una hipotesis de HU-016: cada
identificador, alcance y estimacion debe validarse con la guia oficial antes de
iniciar trabajo.

---

## 6. Priorizacion MoSCoW

### Must

- migraciones versionadas;
- stock y movimientos persistentes;
- pedidos y detalles persistentes;
- reserva transaccional;
- idempotencia de creacion;
- validacion del incremento.

### Should

- cliente web inicial;
- alertas de stock;
- cancelacion controlada;
- flujo inicial de despachos.

### Could

- reportes operativos;
- proyecciones de inventario;
- mensajeria durable.

### Won't now

- separacion en microservicios;
- gRPC;
- broker externo;
- pagos;
- aplicacion movil.

`Won't now` significa fuera de la propuesta actual, no descartado
permanentemente. Las guias oficiales pueden cambiar esta priorizacion.

---

## 7. Estimacion

Se utiliza la escala `1, 2, 3, 5, 8, 13`. Los puntos representan tamano,
complejidad, integracion e incertidumbre; no horas ni productividad individual.

El detalle de cada historia, sus criterios y la justificacion del compromiso se
encuentran en:

```text
docs/planning/mvp-2-commitment.md
```

Las estimaciones deben revisarse al iniciar cada HU. Un cambio se documenta, no
se oculta para conservar artificialmente una cifra.

---

## 8. Capacidad y ciclos

Week 6 y Week 7 muestran un throughput de dos HU por semana. No existen puntos
historicos completados, por lo que `8-10` puntos es una hipotesis inicial de
capacidad.

| Ciclo | HU | Puntos | Objetivo |
|---|---|---:|---|
| 1 | HU-017 y HU-018 | 10 | Base versionada e inventario funcional. |
| 2 | HU-019 y HU-020 | 10 | Pedido persistente con reserva. |
| 3 | HU-021 y HU-022 propuestas | 6 | Reintentos seguros y validacion. |

El ultimo ciclo mantiene margen para defectos y ajustes de integracion. Las
historias `Should` no se agregan automaticamente para ocuparlo.

---

## 9. Dependencias

```text
HU-017
  |-- HU-018 --\
  |             -> HU-020 -> HU-021 -> HU-022 -> guias pendientes
  |-- HU-019 --/
```

Pedidos e Inventario acuerdan primero el contrato de reserva. Los consumidores
pueden trabajar contra mocks o dobles mientras el proveedor completa su
adaptador. Esto evita que un modulo quede detenido esperando la implementacion
interna del otro.

---

## 10. Frontend

HU-023 propone, de manera preliminar, un cliente web inicial de 5 puntos y
prioridad `Should`. Puede
comenzar despues de estabilizar la API de Pedidos, usando OpenAPI y mocks, si no
desplaza trabajo Must ni viola el limite WIP.

Esta propuesta no constituye el alcance oficial de HU-023. Debe confirmarse o
reemplazarse al habilitarse su guia antes de realizar cambios de codigo.

---

## 11. Politica de liberacion

Cada historia sigue:

```text
hu-xxx-dev -> Develop
hu-xxx-qa  -> Qa
```

Cada HU termina en `Qa` y no genera por separado un PR hacia `main`. HU-024
pertenece al MVP 2, por lo que no puede existir una liberacion anterior. El PR
final solo se ejecuta despues de confirmar y aprobar la ultima HU oficial del
MVP 2:

```text
Qa -> main
```

---

## 12. Archivos principales

| Archivo | Responsabilidad |
|---|---|
| `mvp-2-story-map.md` | Recorrido, prioridades y linea de liberacion. |
| `mvp-2-commitment.md` | Historias, puntos, dependencias, ciclos y riesgos. |
| `docs/backlog.md` | Trazabilidad provisional de HU-017 a HU-024. |
| `agile-devops.md` | Capacidad inicial y medicion posterior. |
| `README.md` | Estructura documental actualizada. |

---

## 13. Validacion de desarrollo

Se debe comprobar:

```powershell
git diff --check
cd RedFish
.\gradlew.bat test --no-daemon
```

Resultado obtenido en `hu-016-dev`:

```text
BUILD SUCCESSFUL
27 tests, 0 failures, 0 errors, 1 skipped
```

La prueba omitida corresponde al contexto completo con Testcontainers y ya se
encontraba deshabilitada antes de HU-016. Esta historia no modifica codigo de
produccion ni agrega una nueva omision.

QA validara que la suma de puntos, dependencias, linea de liberacion y politica
de ramas sean consistentes antes de aprobar HU-016.

---

## 14. Flujo Git

```text
hu-016-dev -> Develop
hu-016-qa  -> Qa
```

HU-016 no se promueve individualmente a `main`.

---

## 15. Resultado y siguiente paso

HU-016 fue validada en `hu-016-qa`. El informe detallado se encuentra en
`QA - HU-016.md`.

Antes de comenzar HU-017, se debe analizar su guia oficial y reconciliarla con
la propuesta de migraciones. El compromiso y la secuencia se actualizaran al
habilitarse cada guia posterior.
