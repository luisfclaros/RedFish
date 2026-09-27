# QA - HU-015

## Validacion del modelo Agile y DevOps de RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 8  
**Sesion:** 1  
**Historia de usuario:** HU-015  
**Rama de desarrollo:** `hu-015-dev`  
**Rama de QA:** `hu-015-qa`

---

## 1. Objetivo de QA

Validar que RedFish cuente con un modelo Agile y DevOps consistente, aplicable
y verificable, que defina responsabilidades, ceremonias, criterios de entrada
y salida, limites de trabajo en curso, retroalimentacion automatizada y
metricas de flujo sin contradecir la estrategia Git del MVP 2.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Responsabilidad | Matriz RACI para Product Owner, Desarrollo, QA y DevOps. |
| Scrum | Refinamiento, planificacion, sincronizacion, revision y retrospectiva. |
| Historias | Formato con rol, accion, beneficio y criterios verificables. |
| Ready | Condiciones minimas para comprometer una HU. |
| Done | Calidad, pruebas, CI, documentacion y promocion hasta `Qa`. |
| Flujo | Limites WIP y politica de bloqueos. |
| Revision | Plantilla comun de Pull Request. |
| Automatizacion | Ejecucion de Gradle en PR hacia ramas de ambiente. |
| Medicion | WIP, lead time, cycle time, throughput y defectos escapados. |
| Liberacion | `Qa -> main` solamente al completar el MVP 2. |
| Regresion | Suite Gradle, secretos y evidencias QA anteriores. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | Se documentan roles y responsabilidades mediante RACI. | PASS | Seccion 2 de `docs/process/agile-devops.md`. |
| CA-02 | Se definen las ceremonias Scrum y sus resultados. | PASS | Seccion 4 del modelo operativo. |
| CA-03 | Existe una plantilla de historia verificable. | PASS | Seccion 5 con rol, accion, beneficio y criterios. |
| CA-04 | Existe una Definition of Ready. | PASS | Seccion 6 con diez controles de entrada. |
| CA-05 | Existe una Definition of Done alineada con el flujo Git. | PASS | La HU queda Done al llegar a `Qa`; el MVP queda Released en `main`. |
| CA-06 | Se establecen limites WIP. | PASS | Una HU en desarrollo por responsable y dos en QA para el equipo. |
| CA-07 | Los Pull Requests usan una plantilla comun. | PASS | `.github/pull_request_template.md`. |
| CA-08 | La CI valida las ramas de ambiente. | PASS | Workflow para `Develop`, `Qa` y `main`. |
| CA-09 | Se definen metricas de flujo. | PASS | WIP, lead time, cycle time, throughput y defectos escapados. |
| CA-10 | El backlog incluye HU-015 y HU-016. | PASS | `docs/backlog.md`. |
| CA-11 | La estrategia no promueve cada HU hacia `main`. | PASS | Cada HU termina en `Qa`; solo el MVP completo llega a `main`. |

---

## 4. Casos de prueba

### CP-001 - Verificar artefactos requeridos

Archivos comprobados:

```text
docs/process/agile-devops.md
.github/pull_request_template.md
docs/Week-08/session-01/Week 8 - Session 1.md
docs/backlog.md
```

Resultado: todos los archivos existen y estan vinculados desde el README.

Estado: PASS.

---

### CP-002 - Validar responsabilidades y ceremonias

La matriz RACI asigna responsabilidad y rendicion de cuentas para priorizacion,
criterios, estimacion, implementacion, QA, CI, promocion e incidentes.

Las cinco ceremonias definen frecuencia y un resultado verificable. Las
decisiones tecnicas deben conservarse en Jira, ADR, contratos, documentos o
Pull Requests y no solamente en reuniones.

Estado: PASS.

---

### CP-003 - Validar historias y Definition of Ready

La plantilla incluye:

- rol, accion y beneficio;
- criterios evaluables como PASS o FAIL;
- prioridad MoSCoW;
- estimacion relativa;
- dependencias y responsable.

La Definition of Ready impide iniciar una historia sin alcance, criterios,
dependencias, riesgos o contratos suficientes.

Estado: PASS.

---

### CP-004 - Validar Definition of Done y liberacion

El proceso diferencia los estados:

```text
Por cada HU:
hu-xxx-dev -> Develop
hu-xxx-qa  -> Qa

Al completar el MVP 2:
Qa -> main
```

Una HU individual queda `Done` en `Qa`. La version completa queda `Released`
cuando la ultima HU fue aprobada y se realiza el unico PR de `Qa` hacia `main`.

Estado: PASS.

---

### CP-005 - Validar limites WIP

| Estado | Limite |
|---|---:|
| Desarrollo | 1 HU activa por responsable |
| Revision | 1 PR por responsable |
| QA | 2 HU activas para el equipo |

El modelo exige resolver, replanificar o devolver una historia bloqueada antes
de ocultar el problema iniciando trabajo paralelo.

Estado: PASS.

---

### CP-006 - Validar plantilla de Pull Request

La plantilla solicita:

- HU y ramas relacionadas;
- resumen y alcance excluido;
- criterios de aceptacion;
- cambios en modulos, API, datos y configuracion;
- comandos y resultados de pruebas;
- riesgos y estrategia de rollback;
- confirmacion de CI, contratos, documentacion y secretos.

Tambien impide usar `Qa -> main` antes de aprobar el MVP completo.

Estado: PASS.

---

### CP-007 - Validar integracion continua

El workflow `.github/workflows/contract-tests.yml` se activa en Pull Requests
y envios hacia `Develop`, `Qa` y `main`, y ejecuta:

```bash
./gradlew test --no-daemon
```

La validacion de `main` se conserva para el PR final de liberacion del MVP 2.
La ejecucion remota de esta rama se producira al publicar el Pull Request.

Estado: PASS.

---

### CP-008 - Validar metricas de flujo

| Metrica | Fuente definida |
|---|---|
| WIP | Jira y PR abiertos. |
| Lead time | Estado Ready y fecha de llegada a Qa. |
| Cycle time | Inicio de rama dev y aprobacion QA. |
| Throughput | HU promovidas a Qa por ciclo. |
| Defectos escapados | Jira e informes QA. |

El throughput inicial es de dos HU por semana durante Week 6 y Week 7. No se
inventa una velocidad en puntos; HU-016 iniciara esa medicion.

Estado: PASS.

---

### CP-009 - Ejecutar la suite completa

Comando:

```powershell
.\gradlew.bat clean test --no-daemon
```

Resultado:

```text
BUILD SUCCESSFUL
27 tests, 0 failures, 0 errors, 1 skipped
```

La prueba omitida corresponde al contexto completo con Testcontainers y ya se
encontraba deshabilitada antes de HU-015.

Estado: PASS.

---

### CP-010 - Verificar secretos y artefactos generados

Se confirmo que Git ignora:

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
| DEF-001 | Media | `hu-015-qa` se creo desde una referencia local de `Qa` atrasada respecto de `origin/Qa`. Al copiar Develop se perdieron evidencias aprobadas de HU-011 a HU-014. | Se restauraron estados, resultados e informe QA de HU-014 sin eliminar los cambios de HU-015. | Corregido |
| DEF-002 | Media | La documentacion copiada volvio a describir el evento local de Spring como asincrono y la deduplicacion con una excepcion que no usa la implementacion. | Se restauro la descripcion aprobada: entrega sincrona local e `INSERT IGNORE`. | Corregido |

No se encontraron defectos funcionales bloqueantes en HU-015.

---

## 6. Limitaciones conocidas

| ID | Limitacion | Tratamiento |
|---|---|---|
| OBS-01 | Una persona puede ocupar varios roles en un equipo pequeno. | Mantener separadas las evidencias de desarrollo y QA. |
| OBS-02 | Todavia no existe velocidad historica en story points. | Estimar el backlog e iniciar la medicion en HU-016. |
| OBS-03 | El workflow remoto se ejecuta despues de publicar el PR. | Confirmar el check verde antes de fusionar hacia `Qa`. |
| OBS-04 | La rama local `Qa` estaba atrasada respecto de `origin/Qa`. | Sincronizar `hu-015-qa` con `origin/Qa` antes del PR y actualizar `Qa` local antes de la siguiente HU. |

Estas observaciones no bloquean el alcance de HU-015.

---

## 7. Evidencias finales

| Evidencia | Resultado |
|---|---|
| Modelo Agile y DevOps | PASS |
| Matriz RACI | PASS |
| Ceremonias Scrum | PASS |
| Plantilla de historia | PASS |
| Definition of Ready | PASS |
| Definition of Done | PASS |
| Limites WIP | PASS |
| Plantilla de Pull Request | PASS |
| Workflow CI | PASS |
| Metricas de flujo | PASS |
| Politica de liberacion del MVP | PASS |
| Suite Gradle limpia | PASS |
| Secretos fuera de Git | PASS |
| Evidencias QA anteriores preservadas | PASS |

---

## 8. Resultado final

QA considera que HU-015 cumple todos sus criterios de aceptacion. RedFish cuenta
con un modelo operativo verificable que limita el trabajo en curso, exige
retroalimentacion automatizada y diferencia correctamente la aprobacion de una
HU de la liberacion completa del MVP.

La historia queda aprobada para continuar el flujo:

```text
hu-015-dev -> Develop
hu-015-qa  -> Qa
```

Despues de confirmar estos cambios y sincronizar la rama con `origin/Qa`, el
Pull Request desde `hu-015-qa` hacia `Qa` puede realizarse. `main` permanece sin
cambios hasta completar y aprobar la ultima HU del MVP 2.
