# Backlog inicial del MVP de RedFish

Este backlog es preliminar y sera refinado despues del analisis de dominio, la
identificacion de bounded contexts y la validacion arquitectonica de las
siguientes sesiones.

| ID | Historia de usuario | Prioridad inicial | Notas |
|---|---|---|---|
| HU-001 | Definir RedFish y sus fundamentos de sistemas distribuidos. | Obligatoria | Completada en Week 1 Sesion 1. |
| HU-002 | Establecer estandares de ingenieria y flujo de trabajo. | Obligatoria | Entregable actual de Week 1 Sesion 2. |
| HU-003 | Identificar bounded contexts del dominio RedFish. | Obligatoria | Entregable de Week 2 Sesion 1. |
| HU-004 | Seleccionar y documentar la arquitectura. | Obligatoria | Entregable de Week 2 Sesion 2. |
| HU-005 | Modelar el dominio inicial. | Obligatoria | Entregable de Week 3 Sesion 1. |
| HU-006 | Definir propiedad de datos y contratos. | Obligatoria | Entregable de Week 3 Sesion 2. |
| HU-007 | Construir el walking skeleton. | Obligatoria | Entregable de Week 4 Sesion 1. |
| HU-008 | Definir el contrato inicial de la API del MVP. | Obligatoria | Entregable de Week 4 Sesion 2. |
| HU-009 | Contenerizar la aplicacion y la base de datos. | Obligatoria | Entorno de ejecucion de Spring Boot y MySQL. |
| HU-010 | Validar y liberar el MVP 1. | Obligatoria | Cierre del MVP 1 y version candidata a liberacion. |
| HU-011 | Orquestar RedFish y MySQL con Docker Compose. | Obligatoria | Entregable de Week 6 Sesion 1 para iniciar el sistema completo con un comando. |
| HU-012 | Definir ambientes y estrategia de configuracion del MVP 2. | Obligatoria | Entregable de Week 6 Sesion 2 para promover una misma imagen entre Develop, Qa y main. |
| HU-013 | Definir comunicacion entre modulos y procesamiento idempotente. | Obligatoria | Entregable de Week 7 Sesion 1 con REST externo, eventos internos y puertos MySQL diferenciados. |
| HU-014 | Versionar y verificar los contratos de integracion. | Obligatoria | Entregable de Week 7 Sesion 2 con OpenAPI, compatibilidad y pruebas de contrato. |

## Reglas del backlog

- Toda historia de usuario debe tener criterios de aceptacion verificables antes
  de iniciar desarrollo.
- El alcance puede reducirse para el MVP, pero los criterios de calidad deben
  mantenerse activos.
- Los patrones tecnicos solo deben incorporarse cuando resuelvan un problema
  identificado en RedFish.
- Las correcciones encontradas en QA deben conservar trazabilidad dentro del
  flujo Git.
