# Modelo Agile y DevOps de RedFish

## 1. Proposito

Este documento define como el equipo de RedFish transforma una historia de
usuario en un incremento validado. El objetivo es trabajar en lotes pequenos,
limitar el trabajo en curso, recibir retroalimentacion temprana y conservar la
trazabilidad desde el backlog hasta la liberacion.

Las reglas se aplican al monolito modular completo y a su documentacion. Una
misma persona puede asumir varios roles, pero las responsabilidades y las
evidencias no deben omitirse.

Week 1 establecio una primera Definition of Ready y Definition of Done. HU-015
conserva esos principios, los amplia con controles de flujo, CI y promocion por
ambiente, y establece este documento como la referencia operativa vigente.

---

## 2. Roles y responsabilidades

| Actividad | Product Owner | Desarrollo | QA | DevOps |
|---|---|---|---|---|
| Priorizar el backlog | A/R | C | C | C |
| Definir valor y criterios de aceptacion | A | R | C | C |
| Estimar y dividir historias | C | A/R | C | C |
| Disenar e implementar | C | A/R | C | C |
| Revisar contratos y arquitectura | C | A/R | C | C |
| Ejecutar validacion funcional | C | C | A/R | C |
| Mantener CI y contenedores | C | R | C | A/R |
| Aprobar promocion hacia `Qa` | C | C | A/R | C |
| Autorizar liberacion hacia `main` | A | C | R | C |
| Atender incidentes y aprender de ellos | C | R | R | A/R |

Leyenda:

- `R`: ejecuta la actividad;
- `A`: responde por el resultado final;
- `C`: es consultado antes de tomar la decision.

No se asigna el nombre de una persona dentro del repositorio. La asignacion
concreta se registra en Jira para que pueda cambiar sin modificar estas reglas.

---

## 3. Ciclo de trabajo

```text
Backlog priorizado
        |
        v
Refinamiento y Definition of Ready
        |
        v
Planificacion y compromiso
        |
        v
hu-xxx-dev -> Pull Request -> Develop
        |
        v
hu-xxx-qa -> evidencia QA -> Pull Request -> Qa
        |
        v
Siguiente HU del MVP
        |
        v
Ultima HU aprobada en Qa
        |
        v
Qa -> Pull Request de liberacion -> main
        |
        v
Retrospectiva y mejora del proceso
```

`Develop` integra cambios terminados por desarrollo. `Qa` contiene historias
que superaron validacion. El ciclo de desarrollo y QA se repite para cada HU y
termina en `Qa`. `main` representa la liberacion del MVP completo y solo recibe
el Pull Request de `Qa` despues de aprobar la ultima HU. No se crea una rama
hija de `main`.

---

## 4. Ceremonias

| Ceremonia | Frecuencia | Resultado verificable |
|---|---|---|
| Refinamiento | Antes de comprometer una HU | Historia pequena, criterios, dependencias y estimacion. |
| Planificacion | Inicio de cada ciclo semanal | Objetivo y conjunto de historias comprometidas. |
| Sincronizacion | Diaria, maximo 15 minutos | Avance, siguiente paso y bloqueos visibles. |
| Revision | Al completar el incremento | Demostracion contra criterios de aceptacion. |
| Retrospectiva | Fin de cada ciclo semanal | Una mejora concreta con responsable y fecha. |

Las decisiones que afecten contratos, arquitectura o despliegue se documentan
en el repositorio mediante ADR, contrato, documento tecnico o descripcion del
Pull Request. La reunion no reemplaza la evidencia escrita.

---

## 5. Plantilla de historia de usuario

```text
HU-XXX - Titulo orientado a valor

Como [rol],
quiero [capacidad o accion],
para [beneficio verificable].

Criterios de aceptacion:
- Dado [contexto], cuando [accion], entonces [resultado observable].
- Cada criterio puede responderse PASS o FAIL mediante una prueba.

Prioridad MoSCoW: Must | Should | Could | Won't now
Estimacion: 1 | 2 | 3 | 5 | 8 | 13 puntos
Dependencias:
Responsable:
```

Una historia estimada en 8 o mas puntos debe revisarse y, cuando sea posible,
dividirse antes de entrar al ciclo. Los puntos representan tamano relativo,
complejidad e incertidumbre; no representan horas ni se usan para comparar
personas.

---

## 6. Definition of Ready

Una historia puede comenzar cuando cumple todo lo siguiente:

- expresa rol, capacidad y beneficio;
- tiene criterios de aceptacion observables y verificables;
- su prioridad MoSCoW esta acordada;
- cuenta con una estimacion relativa;
- cabe dentro del ciclo o fue dividida;
- identifica modulos, contratos y datos afectados;
- declara dependencias, riesgos y bloqueos conocidos;
- dispone de ejemplos de solicitud y respuesta cuando modifica una API;
- tiene responsable y rama base definidos;
- no depende de una decision arquitectonica sin resolver.

Una historia que no cumple esta definicion permanece en refinamiento y no se
considera trabajo comprometido.

---

## 7. Definition of Done

Una historia se considera `Done` cuando:

- cumple todos sus criterios de aceptacion;
- conserva los limites del monolito modular;
- incluye pruebas proporcionales al riesgo del cambio;
- la suite automatizada termina sin fallos;
- actualiza OpenAPI y Pact cuando cambia un contrato;
- actualiza scripts o documentacion cuando cambia persistencia o configuracion;
- no versiona secretos, logs ni artefactos generados;
- su Pull Request explica alcance, pruebas, riesgos y evidencias;
- el workflow de CI termina correctamente;
- QA registra resultados, defectos y limitaciones conocidas;
- los cambios fueron promovidos mediante Pull Request hasta `Qa`.

Las historias aprobadas se acumulan en `Qa`. Despues de finalizar la ultima HU
del MVP, la promocion directa de `Qa` a `main` cambia el conjunto completo de
`Done` a `Released`.

---

## 8. Limites de trabajo en curso

| Estado | Limite |
|---|---:|
| En desarrollo | 1 HU activa por responsable |
| En revision de desarrollo | 1 Pull Request por responsable |
| En QA | 2 HU activas para todo el equipo |
| Bloqueada | No cuenta como capacidad disponible; debe resolverse o replanificarse |

Antes de iniciar otra historia se debe terminar, desbloquear o devolver al
backlog la historia activa. Una correccion de QA conserva el identificador de
la HU y no abre trabajo paralelo sin trazabilidad.

---

## 9. Pull Requests y retroalimentacion

Todo cambio llega a una rama de ambiente mediante Pull Request. La plantilla
ubicada en `.github/pull_request_template.md` exige:

- referencia a la HU;
- resumen y alcance fuera del cambio;
- criterios de aceptacion;
- comandos y resultados de pruebas;
- impacto en API, datos, configuracion y seguridad;
- evidencia documental;
- confirmacion de CI y ausencia de secretos.

Los comentarios deben evaluar el cambio, no a la persona. Un defecto se trata
como informacion para mejorar el producto y el proceso.

---

## 10. Integracion continua

El workflow `.github/workflows/contract-tests.yml` se ejecuta en los Pull
Requests hacia `Develop`, `Qa` y `main`. Actualmente compila el proyecto y
ejecuta la suite completa, incluidas las pruebas unitarias, REST, OpenAPI y
Pact.

Un Pull Request con CI fallida no se promueve. Si una prueba se omite, la causa
y el riesgo deben quedar documentados; ocultar o eliminar una prueba para
obtener un resultado exitoso no cumple la Definition of Done.

---

## 11. Metricas de flujo

| Metrica | Definicion en RedFish | Fuente | Revision |
|---|---|---|---|
| WIP | HU iniciadas que aun no estan en `Qa`. | Jira y Pull Requests abiertos | Diaria |
| Lead time | Tiempo desde que la HU entra en `Ready` hasta que llega a `Qa`. | Jira y fechas de PR | Semanal |
| Cycle time | Tiempo desde el primer trabajo en `hu-xxx-dev` hasta la aprobacion en `Qa`. | Git y Pull Requests | Semanal |
| Throughput | Cantidad de HU promovidas a `Qa` durante el ciclo. | Historial de merges | Semanal |
| Defectos escapados | Defectos detectados despues de promover la HU. | Jira e informes QA | Por liberacion |

La linea base disponible es un throughput de dos HU por semana durante Week 6
y Week 7 (`HU-011` a `HU-014`). Aun no existe una velocidad historica en story
points; se comenzara a medir despues de estimar el backlog de MVP 2 en HU-016.

Las metricas sirven para mejorar previsibilidad y flujo. No se utilizan para
rankings individuales ni para convertir los puntos en una cuota.

---

## 12. Retrospectiva

Cada retrospectiva registra como minimo:

```text
Fecha:
Que funciono:
Que dificulto el flujo:
Metrica observada:
Mejora acordada:
Responsable:
Fecha de revision:
```

Solo se compromete una mejora principal por ciclo. En la siguiente
retrospectiva se verifica su resultado antes de agregar otra.
