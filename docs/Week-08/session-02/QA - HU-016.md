# QA - HU-016

## Validacion de la planificacion y compromiso del MVP 2

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 8  
**Sesion:** 2  
**Historia de usuario:** HU-016  
**Rama de desarrollo:** `hu-016-dev`  
**Rama de QA:** `hu-016-qa`

---

## 1. Objetivo de QA

Validar que HU-016 organice el MVP 2 mediante story mapping, priorizacion,
estimacion relativa, dependencias y capacidad sin presentar como definitivas
las HU cuyas guias oficiales todavia no se encuentran disponibles.

Tambien se verifica que la estrategia conserve el flujo por historia hasta
`Qa` y que la liberacion hacia `main` solo ocurra despues de HU-024 y de la
ultima HU oficial confirmada para el MVP 2.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Story map | Recorrido del usuario, prioridades y rebanada vertical. |
| Estimacion | Escala Fibonacci y division de capacidades grandes. |
| Dependencias | Orden entre migraciones, Inventario y Pedidos. |
| Capacidad | Hipotesis basada en throughput sin inventar velocidad historica. |
| Backlog | Trazabilidad provisional de HU-017 a HU-024. |
| Guias futuras | Prevalencia de las guias oficiales sobre la propuesta. |
| Frontend | Candidato provisional, no alcance confirmado de HU-023. |
| Liberacion | Prohibicion de promover a `main` antes de la ultima HU oficial. |
| Regresion | Suite Gradle, secretos y evidencias QA anteriores. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | Existe un story map organizado por recorrido y prioridad. | PASS | `docs/planning/mvp-2-story-map.md`. |
| CA-02 | La linea de liberacion representa una rebanada de extremo a extremo. | PASS | Secuencia propuesta desde datos hasta validacion. |
| CA-03 | El trabajo se clasifica mediante MoSCoW. | PASS | Mapa y compromiso distinguen Must, Should, Could y Won't now. |
| CA-04 | Las capacidades propuestas tienen criterios verificables. | PASS | Seccion 3 de `mvp-2-commitment.md`. |
| CA-05 | Las estimaciones usan puntos relativos Fibonacci. | PASS | Escala `1, 2, 3, 5, 8, 13`. |
| CA-06 | Ninguna capacidad estimada conserva 8 puntos o mas. | PASS | La propuesta se divide en elementos de 3 o 5 puntos. |
| CA-07 | Las dependencias tecnicas son visibles. | PASS | Diagrama y matriz de dependencias. |
| CA-08 | Se define contrato primero y uso de dobles. | PASS | Estrategias para Pedidos e Inventario. |
| CA-09 | No se presenta la capacidad como velocidad historica. | PASS | `8-10` puntos se documentan como hipotesis. |
| CA-10 | El plan conserva margen para incertidumbre. | PASS | El tercer ciclo preliminar no completa toda la capacidad hipotetica. |
| CA-11 | El frontend se identifica como propuesta. | PASS | HU-023 queda pendiente de su guia oficial. |
| CA-12 | `Qa -> main` espera la ultima HU oficial del MVP 2. | PASS | Politica de liberacion corregida. |
| CA-13 | Las HU futuras se identifican como provisionales. | PASS | Backlog, story map y compromiso incluyen la advertencia. |
| CA-14 | HU-024 se reconoce dentro del MVP 2. | PASS | No se permite una liberacion anterior a HU-024. |

---

## 4. Casos de prueba

### CP-001 - Verificar artefactos de planificacion

Archivos comprobados:

```text
docs/planning/mvp-2-story-map.md
docs/planning/mvp-2-commitment.md
docs/Week-08/session-02/Week 8 - Session 2.md
docs/backlog.md
docs/process/agile-devops.md
README.md
```

Todos existen y `README.md` registra el directorio `docs/planning/`.

Estado: PASS.

---

### CP-002 - Validar recorrido y rebanada vertical

El mapa comienza con la preparacion de datos, pasa por control de stock y
registro del pedido, protege la integracion contra duplicados y termina con
validacion. La propuesta atraviesa API, aplicacion, contratos, dominio,
persistencia y pruebas, en lugar de dividir el trabajo solamente por capas.

Estado: PASS.

---

### CP-003 - Validar estimaciones y capacidad

La estimacion preliminar comprobada es:

```text
5 + 5 + 5 + 5 + 3 + 3 = 26 puntos
```

Los 26 puntos no incluyen HU-023 ni HU-024, cuyos valores permanecen como
`TBD`. La capacidad de `8-10` puntos por ciclo se presenta como hipotesis y se
medira cuando el primer ciclo llegue realmente a `Qa`.

Estado: PASS.

---

### CP-004 - Validar dependencias

La propuesta hace visibles las dependencias entre migraciones, existencias,
pedidos, reserva e idempotencia. Los contratos y dobles permiten reducir el
bloqueo entre Inventario y Pedidos sin romper los limites del monolito modular.

Estado: PASS.

---

### CP-005 - Validar alcance provisional y guias oficiales

Se comprobo que:

- HU-017 a HU-023 se describen como propuestas de HU-016;
- cada HU debe refinarse con su guia oficial antes de implementarse;
- HU-024 pertenece al MVP 2, pero su contenido no se inventa;
- la propuesta de frontend para HU-023 no se presenta como definitiva;
- la guia oficial prevalece cuando exista una diferencia con este plan.

Estado: PASS.

---

### CP-006 - Validar flujo Git y liberacion

El flujo por historia se conserva:

```text
hu-xxx-dev -> Develop
hu-xxx-qa  -> Qa
```

No se crea una rama hija de `main`. El unico PR `Qa -> main` del MVP 2 se
realizara despues de HU-024 y despues de confirmar que la ultima HU oficial fue
aprobada. HU-022 no autoriza por si sola la liberacion.

Estado: PASS.

---

### CP-007 - Preservar evidencias QA anteriores

Se compararon los documentos de HU-011 a HU-015 y la estrategia de
comunicacion con la base vigente de `Qa`. Se restauraron los estados aprobados,
la entrega sincrona del evento local y la deduplicacion mediante
`INSERT IGNORE`.

Estado: PASS.

---

### CP-008 - Ejecutar la suite completa

Comando:

```powershell
.\gradlew.bat clean test --no-daemon
```

Resultado:

```text
BUILD SUCCESSFUL in 42s
27 tests, 0 failures, 0 errors, 1 skipped
```

La prueba omitida corresponde al contexto completo con Testcontainers y ya se
encontraba deshabilitada antes de HU-016. Esta HU no modifica codigo de
produccion ni agrega pruebas omitidas.

Estado: PASS.

---

### CP-009 - Verificar formato, secretos y artefactos

`git diff --check` no reporta errores. Tambien se comprobo que Git ignora:

```text
RedFish/.env
RedFish/.env.qa
RedFish/.env.prod
RedFish/.gradle/
RedFish/build/
RedFish/bin/
```

Estado: PASS.

---

## 5. Defectos encontrados y corregidos

| ID | Severidad | Descripcion | Correccion | Estado |
|---|---|---|---|---|
| DEF-001 | Alta | El plan presentaba HU-022 como ultima HU y autorizaba `Qa -> main`, aunque HU-024 pertenece al MVP 2. | Se retiro la liberacion de HU-022, se agrego HU-024 y se exige confirmar la ultima HU oficial. | Corregido |
| DEF-002 | Media | HU-017 a HU-023 aparecian como asignaciones definitivas aunque las guias futuras estan bloqueadas. | Se marcaron como hipotesis y se establecio que las guias oficiales prevalecen. | Corregido |
| DEF-003 | Media | La copia desde `Develop` sobrescribio estados QA aprobados de HU-011 a HU-015. | Se restauraron los documentos desde la referencia vigente de `Qa`. | Corregido |
| DEF-004 | Media | La copia volvio a describir el evento local Spring como asincrono y cambio la deduplicacion implementada. | Se restauro entrega sincrona local e `INSERT IGNORE`. | Corregido |

No se encontraron defectos funcionales bloqueantes despues de aplicar estas
correcciones.

---

## 6. Limitaciones conocidas

| ID | Limitacion | Tratamiento |
|---|---|---|
| OBS-01 | Las guias de las semanas posteriores aun estan bloqueadas. | Refinar cada HU cuando su guia sea habilitada. |
| OBS-02 | No existe velocidad historica en story points. | Medir puntos terminados en `Qa` tras el primer ciclo. |
| OBS-03 | HU-023 y HU-024 no tienen estimacion confirmada. | Mantener `TBD` y evitar iniciar codigo por inferencia. |
| OBS-04 | El workflow remoto se ejecuta al publicar el PR. | Confirmar el check verde antes de fusionar hacia `Qa`. |

Estas observaciones no bloquean HU-016 porque el entregable es una base de
planificacion adaptable, no la implementacion de las HU futuras.

---

## 7. Evidencias finales

| Evidencia | Resultado |
|---|---|
| Story map del MVP 2 | PASS |
| Priorizacion MoSCoW | PASS |
| Estimacion Fibonacci | PASS |
| Dependencias y contratos primero | PASS |
| Hipotesis de capacidad | PASS |
| Guias futuras reconocidas | PASS |
| HU-024 incluida en MVP 2 | PASS |
| Politica `Qa -> main` | PASS |
| Evidencias QA anteriores preservadas | PASS |
| Suite Gradle | PASS |
| Secretos fuera de Git | PASS |
| Formato documental | PASS |

---

## 8. Resultado final

QA considera que HU-016 cumple sus criterios de aceptacion despues de corregir
la linea de liberacion y distinguir las propuestas de las guias oficiales. El
story map y las estimaciones sirven como linea base adaptable sin inventar el
contenido de HU-023 o HU-024.

La historia queda aprobada para continuar el flujo:

```text
hu-016-dev -> Develop
hu-016-qa  -> Qa
```

El Pull Request desde `hu-016-qa` hacia `Qa` puede realizarse cuando estos
cambios esten confirmados y el workflow remoto termine correctamente. `main`
permanece sin cambios hasta completar y aprobar la ultima HU oficial del MVP 2.
