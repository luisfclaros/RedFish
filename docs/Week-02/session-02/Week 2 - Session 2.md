# Week 2 - Sesion 2

## Seleccion de arquitectura y stack tecnologico de RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 2  
**Sesion:** 2  
**Historia de usuario:** HU-004

---

## 1. Objetivo de la sesion

Seleccionar y documentar la arquitectura base y el stack tecnologico inicial de
RedFish, tomando como entrada el analisis de bounded contexts realizado en
`HU-003`.

Esta sesion formaliza el uso de monolito modular como arquitectura objetivo,
Spring Boot como framework principal del backend y JavaScript para la capa
cliente o scripts de apoyo del proyecto.

---

## 2. Historia de usuario

### HU-004 - Seleccionar y documentar la arquitectura

**Como** responsable tecnico de RedFish,  
**quiero** seleccionar y documentar la arquitectura y stack tecnologico del
proyecto,  
**para** que las siguientes implementaciones tengan una guia clara de estructura,
responsabilidades y tecnologias base.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se confirma el monolito modular como arquitectura objetivo. |
| CA-02 | Se documenta Spring Boot como framework backend. |
| CA-03 | Se documenta JavaScript como tecnologia para capa cliente o scripts de apoyo. |
| CA-04 | Se define una estructura inicial compatible con modulos/bounded contexts. |
| CA-05 | Se aclara que los modulos no son entidades individuales. |
| CA-06 | Se documenta la relacion entre arquitectura, DDD y arquitectura hexagonal. |
| CA-07 | Se actualiza ADR-001 con la decision arquitectonica aceptada. |
| CA-08 | Se registran riesgos y decisiones abiertas del stack. |

---

## 3. Decision arquitectonica

RedFish adopta como arquitectura base un **Monolito Modular**.

La aplicacion se desplegara como una sola unidad, pero su codigo se organizara
por modulos alineados con los bounded contexts identificados en `HU-003`.

Los modulos iniciales son:

- Seguridad y Usuarios.
- Produccion y Alimentacion.
- Inventario.
- Pedidos.
- Despachos.
- Vehiculos.
- Reportes y Consultas.

Estos modulos no representan entidades individuales. Cada modulo puede contener
entidades, servicios de dominio, casos de uso, repositorios, controladores y
adaptadores relacionados con su responsabilidad de negocio.

---

## 4. Stack tecnologico inicial

| Capa | Tecnologia | Uso |
|---|---|---|
| Backend | Spring Boot | Construccion de la API, casos de uso, modulos e integracion con persistencia. |
| Lenguaje backend | Java | Lenguaje principal para implementar Spring Boot. |
| Cliente / scripts | JavaScript | Interacciones del cliente, consumo de API o scripts de apoyo segun evolucione el proyecto. |
| Base de datos | MySQL | Persistencia relacional segun el esquema preliminar de `BaseDeDatosRedFish`. |
| Control de versiones | Git y GitHub | Ramas por ambiente, Pull Requests y trazabilidad por HU. |
| Documentacion | Markdown | Evidencias, ADR, backlog y validaciones. |

Spring Boot no se define como framework de JavaScript. Se usara para el backend,
mientras JavaScript queda asociado a la capa cliente o automatizaciones simples
cuando sean necesarias.

---

## 5. Estructura inicial propuesta

Una estructura compatible con monolito modular, DDD y arquitectura hexagonal
seria:

```text
src/
└── main/
    ├── java/
    │   └── com/redfish/
    │       ├── security/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── production/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── inventory/
    │       ├── orders/
    │       ├── dispatches/
    │       ├── vehicles/
    │       └── reporting/
    └── resources/
```

Cada modulo podra organizar internamente:

- `domain`: entidades, Value Objects, reglas e invariantes.
- `application`: casos de uso y servicios de aplicacion.
- `infrastructure`: repositorios, persistencia, integraciones y adaptadores.
- `interfaces`: controladores HTTP y entrada/salida de la API.

---

## 6. Relacion con DDD y arquitectura hexagonal

La arquitectura propuesta mantiene los siguientes principios:

- Los bounded contexts de `HU-003` orientan la separacion modular.
- El dominio debe proteger las reglas del negocio.
- Los controladores no deben contener logica de negocio.
- Los repositorios pertenecen a infraestructura.
- Los casos de uso coordinan operaciones del modulo.
- Las dependencias deben apuntar hacia el dominio, no desde el dominio hacia
  frameworks o infraestructura.

Regla base:

```text
Interfaces / Infrastructure -> Application -> Domain
```

---

## 7. Riesgos del stack

| Riesgo | Impacto | Mitigacion |
|---|---|---|
| Organizar el codigo por entidades aisladas | Perdida de limites de dominio | Mantener estructura por modulos/bounded contexts. |
| Colocar logica de negocio en controladores | Dificultad de prueba y mantenimiento | Llevar reglas a dominio o casos de uso. |
| Acoplar modulos mediante consultas directas | Monolito dificil de evolucionar | Usar contratos internos o casos de uso del modulo dueno. |
| Confundir JavaScript con framework backend | Decision tecnica inconsistente | Documentar JavaScript para cliente/scripts y Spring Boot para backend. |
| Aceptar ADR sin validar contexto | Decision prematura | Usar HU-003 como evidencia de validacion inicial. |

---

## 8. Decisiones abiertas

- Definir si el cliente sera una aplicacion web completa o una interfaz simple.
- Definir si JavaScript se usara con un framework frontend especifico.
- Definir si el backend usara Maven o Gradle.
- Definir la version exacta de Java y Spring Boot cuando inicie la
  implementacion.
- Definir estrategia de migraciones de base de datos.

---

## 9. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| Arquitectura | Monolito modular |
| Backend | Spring Boot con Java |
| Cliente / apoyo | JavaScript |
| Base de datos | MySQL |
| ADR-001 | Aceptado |
| Validacion QA | Pendiente en rama `hu-004-qa` |

---

## 10. Siguiente paso

Promover `HU-004` al ambiente QA mediante `hu-004-qa`, validar la documentacion
y usar esta decision como base para el modelado inicial del dominio y la futura
implementacion del walking skeleton.
