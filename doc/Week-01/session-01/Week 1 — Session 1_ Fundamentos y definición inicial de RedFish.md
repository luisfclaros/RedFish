# Week 1 — Session 1

## Fundamentos de Sistemas Distribuidos y definición inicial de RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Week:** 1  
**Session:** 1

---

## 1. Objetivo de la sesión

Establecer los fundamentos iniciales del proyecto RedFish, identificando el problema que busca resolver, sus principales operaciones y los requisitos preliminares de consistencia, comunicación y tolerancia a fallos que deberán considerarse durante el diseño e implementación del sistema.

En esta sesión no se selecciona formalmente una arquitectura de software. La decisión arquitectónica será analizada y documentada posteriormente utilizando los criterios correspondientes a las siguientes semanas del proyecto.

---

## 2. Descripción del problema

Las empresas piscícolas manejan diferentes procesos operativos relacionados con producción, alimentación, inventario, pedidos, vehículos y despachos.

Cuando esta información se encuentra dispersa o se gestiona de forma independiente, pueden presentarse dificultades para consultar el estado actual de las operaciones, mantener la consistencia de los datos y disponer de información confiable para la toma de decisiones.

RedFish busca proporcionar un sistema de información que permita gestionar de forma centralizada estos procesos y establecer una base tecnológica que pueda evolucionar de forma controlada durante el desarrollo del proyecto.

---

## 3. Objetivo general inicial

Desarrollar un sistema de información para apoyar la gestión de los procesos de producción, alimentación, inventario, pedidos, vehículos y despachos de una empresa piscícola, procurando mantener la integridad, disponibilidad y trazabilidad de la información utilizada durante sus operaciones.

La arquitectura específica del sistema será evaluada y justificada durante las siguientes etapas del proyecto.

---

## 4. Operaciones principales identificadas

Durante esta etapa se identifican inicialmente las siguientes áreas funcionales:

- producción;
- alimentación;
- inventario;
- productos;
- pedidos;
- clientes;
- despachos;
- vehículos;
- usuarios;
- consultas y dashboard.

Estas áreas representan candidatos funcionales iniciales. Su existencia no implica todavía que correspondan a servicios, microservicios o bounded contexts independientes.

---

## 5. Modelo inicial del sistema

Para esta etapa se considera un entorno donde diferentes componentes pueden comunicarse mediante una red.

Se adopta inicialmente un modelo de red asíncrono, debido a que no puede garantizarse un tiempo máximo constante para la comunicación o procesamiento de cada solicitud.

El modelo inicial de fallos considerado es:

**Crash-recovery:** un componente puede detenerse y posteriormente volver a funcionar.

**Omission:** una solicitud o respuesta puede perderse durante la comunicación.

No se considera inicialmente necesario diseñar tolerancia frente a fallos bizantinos.

---

## 6. Consistencia inicial de las operaciones

| Operación | Consistencia inicial | Justificación |
|---|---|---|
| Registrar producción | Fuerte / transaccional | Un registro operacional no debe quedar parcialmente almacenado |
| Registrar alimentación | Fuerte / transaccional | Los datos deben conservar integridad durante la operación |
| Modificar inventario | Fuerte | Se deben evitar existencias inválidas o actualizaciones concurrentes incorrectas |
| Crear o confirmar pedido | Fuerte | La operación debe mantener coherencia con las reglas del negocio |
| Asignar vehículo o despacho | Fuerte | Deben evitarse asignaciones incompatibles o duplicadas |
| Gestionar usuarios | Fuerte | Los cambios relacionados con acceso y seguridad requieren información actual |
| Consultar dashboard | Eventual aceptable | Una pequeña demora en información de consulta no necesariamente afecta operaciones transaccionales |

Estas decisiones son preliminares y podrán modificarse cuando el dominio y la arquitectura sean analizados con mayor profundidad.

---

## 7. Comunicación y delivery semantics

Inicialmente se considera que las operaciones solicitadas por los usuarios utilizarán un modelo de comunicación síncrona de solicitud y respuesta.

Sin embargo, una comunicación mediante red puede fallar incluso después de que el servidor haya procesado correctamente una solicitud.

Por esta razón, determinadas operaciones deberán diseñarse para soportar reintentos sin producir efectos duplicados.

Ejemplos importantes son:

- creación de pedidos;
- movimientos de inventario;
- confirmaciones de operaciones;
- creación de despachos.

Estas operaciones deberán considerar idempotencia, identificadores únicos u otros mecanismos equivalentes cuando sean implementadas.

No se selecciona todavía ningún sistema de mensajería ni tecnología adicional, ya que actualmente no existe una necesidad técnica que justifique su introducción.

---

## 8. Riesgos identificados inicialmente

### Solicitudes duplicadas

Una petición puede ser procesada correctamente mientras su respuesta se pierde. Un reintento podría ejecutar nuevamente la operación.

### Actualizaciones concurrentes

Dos usuarios pueden intentar modificar simultáneamente datos como las existencias de inventario.

### Fallos de componentes

El backend o la base de datos pueden dejar de estar disponibles temporalmente.

### Latencia

Las comunicaciones entre componentes pueden experimentar retrasos.

### Datos parcialmente actualizados

Una operación compuesta por varios cambios debe evitar quedar parcialmente aplicada.

Estos riesgos serán abordados progresivamente durante el desarrollo.

---

## 9. Decisiones que todavía no se toman

Durante esta sesión no se define formalmente:

- arquitectura definitiva del proyecto;
- bounded contexts;
- separación interna de módulos;
- framework de backend;
- framework de frontend;
- sistema de mensajería;
- caché;
- replicación de base de datos;
- microservicios;
- estructura hexagonal definitiva.

Estas decisiones deberán justificarse cuando el contenido correspondiente sea estudiado.

---

## 10. Historia de usuario de la sesión

### HU-001 — Definición inicial y fundamentos de RedFish

**Como** responsable del proyecto RedFish,  
**quiero** documentar el problema, alcance y requisitos iniciales de consistencia y comunicación,  
**para** disponer de una base técnica verificable sobre la cual diseñar el sistema.

### Criterios de aceptación

- El problema que resuelve RedFish está documentado.
- El objetivo general no presupone una arquitectura específica.
- Las principales operaciones del sistema están identificadas.
- Las operaciones críticas tienen una necesidad inicial de consistencia.
- Se documenta el modelo inicial de fallos.
- Se consideran los posibles reintentos de solicitudes.
- Se identifican operaciones que deberán contemplar idempotencia.
- No se introducen tecnologías sin una necesidad identificada.
- El contenido puede ser revisado posteriormente en QA.

---

## 11. Próximo paso

En Week 1 — Session 2 se estudiarán y aplicarán fundamentos profesionales de ingeniería de software, incluyendo prácticas de trabajo, estrategia Git, backlog, testing, documentación y decisiones arquitectónicas mediante ADR.