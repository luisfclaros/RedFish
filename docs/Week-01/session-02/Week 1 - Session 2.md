# Week 1 - Sesion 2

## Fundamentos de ingenieria profesional para RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 1  
**Sesion:** 2

---

## 1. Objetivo de la sesion

Establecer los estandares de ingenieria, flujo de trabajo, estrategia de pruebas,
practicas de documentacion y proceso de decisiones arquitectonicas que guiaran
el desarrollo de RedFish.

Esta sesion no implementa codigo de aplicacion. Su proposito es definir las
reglas profesionales que deberan seguir las siguientes entregas del proyecto.

---

## 2. Historia de usuario

### HU-002 - Establecer estandares de ingenieria y flujo de trabajo

**Como** responsable del desarrollo de RedFish,  
**quiero** establecer practicas de ingenieria, pruebas, documentacion y control
de versiones,  
**para** que las futuras funcionalidades puedan desarrollarse, validarse y
documentarse de forma consistente.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se documentan los principios de DDD aplicables al proyecto. |
| CA-02 | Se documenta la regla de dependencias para arquitectura hexagonal. |
| CA-03 | Se establecen reglas basicas de SOLID y Clean Code. |
| CA-04 | Se define una estrategia inicial de pruebas. |
| CA-05 | Se documentan Definition of Ready y Definition of Done. |
| CA-06 | Se documenta el flujo Git por ambientes. |
| CA-07 | `Develop` y `Qa` usan ramas hijas para mantener trazabilidad por historia de usuario. |
| CA-08 | `main` recibe cambios directamente desde `Qa` mediante Pull Request. |
| CA-09 | Se adopta la convencion Conventional Commits. |
| CA-10 | Se crea ADR-001 con estado inicial `Propuesto`. |
| CA-11 | Se redacta un backlog inicial del MVP. |
| CA-12 | Se consideran las evidencias individuales de Week 1. |

---

## 3. Domain-Driven Design

RedFish aplicara conceptos de Domain-Driven Design cuando aporten claridad al
dominio y ayuden a proteger las reglas del negocio. Los conceptos iniciales que
se evaluaran durante el proyecto son:

- Bounded Contexts.
- Lenguaje ubicuo.
- Entidades.
- Value Objects.
- Agregados.
- Eventos de dominio.

Un agregado representa un limite de consistencia y modificacion. Las invariantes
del negocio deben protegerse desde el dominio y no quedar distribuidas entre
controladores, consultas directas o componentes de infraestructura.

Los bounded contexts y el modelo de dominio definitivo se analizaran en
sesiones posteriores antes de cerrar la estructura interna de los modulos.

---

## 4. Arquitectura hexagonal

El proyecto establece la siguiente regla de dependencias:

```text
Adaptadores -> Aplicacion -> Dominio
```

El dominio no debe depender directamente de:

- frameworks HTTP;
- controladores;
- drivers de base de datos;
- ORMs;
- APIs externas;
- frameworks de interfaz de usuario;
- librerias propias de infraestructura.

Cuando esta estructura sea incorporada al codigo, los componentes de
infraestructura se comunicaran con la aplicacion por medio de puertos y
adaptadores.

Un controlador que ejecute SQL directamente o una entidad de dominio que importe
un ORM se considerara una violacion arquitectonica.

---

## 5. SOLID y Clean Code

### Single Responsibility Principle

Las clases, modulos y funciones deben tener responsabilidades claras y evitar
acumular comportamiento no relacionado.

### Dependency Inversion Principle

Los casos de uso de la aplicacion deben depender de abstracciones y no de
implementaciones concretas de infraestructura.

### Interface Segregation Principle

Los puertos e interfaces deben ser pequenos y orientados a las operaciones que
realmente necesitan sus consumidores.

### Clean Code

El proyecto usara como reglas minimas:

- nombres descriptivos;
- funciones pequenas y enfocadas;
- manejo explicito de errores;
- ausencia de errores ignorados silenciosamente;
- eliminacion de codigo muerto innecesario;
- ausencia de comentarios `TODO` o `FIXME` permanentes sin trazabilidad.

---

## 6. Principios de resiliencia

La comunicacion entre componentes puede fallar. Por esta razon, el proyecto
reconoce los siguientes patrones de resiliencia:

- Timeout.
- Retry con backoff y jitter.
- Circuit Breaker.
- Bulkhead.
- Saga.
- Outbox.
- CQRS.

Estos patrones no se introduciran automaticamente. Cada mecanismo de resiliencia
debe responder a un problema real identificado en RedFish antes de incorporarse
a la implementacion.

---

## 7. Estrategia de pruebas

Las pruebas se implementaran en el nivel mas economico que entregue confianza
suficiente.

### Pruebas unitarias

Se usaran principalmente para:

- reglas de dominio;
- invariantes;
- Value Objects;
- casos de uso aislados.

### Pruebas de integracion

Se usaran para:

- repositorios;
- integracion con PostgreSQL;
- adaptadores de infraestructura;
- limites con infraestructura externa.

Cuando inicie la implementacion, la base de datos real podra ejecutarse mediante
contenedores para validar integraciones.

### Pruebas de contrato

Se usaran cuando sea necesario garantizar compatibilidad entre productores y
consumidores de contratos.

### Pruebas end-to-end

Se usaran para validar flujos completos de negocio a traves de la aplicacion.

Las sesiones compuestas solo por documentacion se validaran mediante criterios
de aceptacion, sin crear pruebas de codigo artificiales.

---

## 8. Definition of Ready

Una historia de usuario esta lista para iniciar cuando:

- su objetivo es comprensible;
- el actor esta identificado;
- el beneficio esperado es claro;
- los criterios de aceptacion son verificables;
- las dependencias importantes estan identificadas;
- existe informacion suficiente para comenzar;
- los riesgos relevantes son conocidos.

Una historia no debe iniciar solo porque tiene un titulo.

---

## 9. Definition of Done

Una historia de usuario esta terminada cuando:

- la implementacion esta completa, cuando aplique;
- los criterios de aceptacion se cumplen;
- las pruebas requeridas han pasado;
- la validacion de QA ha sido realizada;
- los riesgos criticos de seguridad han sido revisados;
- la documentacion fue actualizada;
- no quedan defectos bloqueantes conocidos;
- se realizo validacion en ejecucion cuando existe software ejecutable;
- el Pull Request fue revisado;
- las evidencias requeridas fueron registradas.

El MVP reduce alcance, no los estandares de calidad.

---

## 10. Flujo Git

RedFish usa tres ramas de ambiente de larga vida:

```text
Develop -> Qa -> main
```

Para mantener trazabilidad, cada historia de usuario debe desarrollarse en una
rama hija del ambiente donde se este trabajando.

Flujo para `HU-002`:

```text
Develop
└── hu-002-dev
      └── Pull Request -> Develop

Qa
└── hu-002-qa
      └── Pull Request -> Qa

Qa
└── Pull Request -> main
```

Las ramas `Develop` y `Qa` no deben usarse para desarrollar cambios
directamente. `main` representa la version estable y recibe cambios promovidos
desde `Qa` mediante Pull Request directo.

---

## 11. Conventional Commits

El proyecto adopta mensajes basados en Conventional Commits. Ejemplos:

```text
feat(pedidos): implementar creacion de pedidos
fix(inventario): evitar existencias negativas
test(pedidos): agregar pruebas de validacion de pedidos
docs(week-01): establecer estandares de ingenieria
docs(adr): proponer arquitectura de monolito modular
```

---

## 12. Backlog inicial del MVP

El backlog inicial del MVP 1 se documenta en:

```text
docs/backlog.md
```

Este backlog es preliminar y se refinara a medida que se analice el dominio.

---

## 13. Evidencia individual

La evidencia de Week 1 se registra en:

```text
01-week/hu-status/README.md
```

Participante del proyecto:

```text
LUIS FERNANDO CLAROS RAMOS
```

Usuario de GitHub:

```text
luisfclaros
```

Debido a que RedFish es un proyecto individual, el requisito de fork personal de
un repositorio grupal debe verificarse contra las expectativas de evaluacion del
docente. Aun asi, se mantiene la ruta de evidencia solicitada.

---

## 14. Estado de la decision arquitectonica

La arquitectura objetivo propuesta para RedFish es:

```text
Monolito Modular
```

ADR-001 usa inicialmente el estado:

```text
Propuesto
```

La decision se validara despues del analisis de bounded contexts y arquitectura
que se realizara en Week 2.

---

## 15. Estado de la sesion

| Area | Estado |
|---|---|
| Documentacion de desarrollo | Completada en `hu-002-dev` |
| Validacion QA | Pendiente en la rama `hu-002-qa` |
| Estado final | Pendiente de aprobacion en QA |

---

## 16. Siguiente paso

Promover `HU-002` al ambiente QA, validar sus criterios de aceptacion en la rama
`hu-002-qa`, registrar hallazgos si aparecen y cerrar Week 1 antes de iniciar
Week 2.
