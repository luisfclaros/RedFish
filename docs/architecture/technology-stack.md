# Stack tecnologico de RedFish

## Proposito

Este documento registra el stack tecnologico inicial seleccionado para RedFish y
su relacion con la arquitectura de monolito modular.

---

## Stack seleccionado

| Area | Tecnologia | Decision |
|---|---|---|
| Backend | Spring Boot | Framework principal para construir la API y los modulos de negocio. |
| Lenguaje backend | Java | Lenguaje requerido para la implementacion principal con Spring Boot. |
| Cliente / scripts | JavaScript | Tecnologia para consumo de API, interacciones de cliente o scripts de apoyo. |
| Base de datos | MySQL | Persistencia relacional alineada con el modelo preliminar. |
| Documentacion | Markdown | Registro de entregables, ADR, QA y evidencias. |
| Versionamiento | Git y GitHub | Ramas por ambiente y Pull Requests por HU. |

---

## Aclaracion sobre JavaScript y Spring Boot

Spring Boot pertenece al ecosistema Java y sera usado como framework backend.

JavaScript no reemplaza a Java dentro de Spring Boot. En RedFish, JavaScript se
considera para la capa cliente, consumo de servicios HTTP o automatizaciones de
apoyo cuando el proyecto lo requiera.

---

## Responsabilidades por tecnologia

### Spring Boot

- Exponer controladores HTTP.
- Ejecutar casos de uso de aplicacion.
- Coordinar modulos del monolito.
- Integrar persistencia relacional.
- Centralizar configuracion del backend.

### Java

- Implementar entidades de dominio.
- Implementar Value Objects.
- Implementar servicios de dominio y aplicacion.
- Implementar repositorios y adaptadores.
- Implementar pruebas backend.

### JavaScript

- Consumir endpoints del backend cuando exista interfaz cliente.
- Implementar validaciones de interfaz cuando aplique.
- Apoyar scripts simples de automatizacion si son necesarios.
- Mantenerse fuera de la logica principal del backend.

### MySQL

- Persistir informacion transaccional.
- Aplicar restricciones de integridad.
- Servir como base del modelo relacional inicial.
- Evolucionar mediante scripts o migraciones cuando se implemente el backend.

---

## Decisiones pendientes

- Version de Java.
- Version de Spring Boot.
- Herramienta de construccion: Maven o Gradle.
- Framework frontend, si se decide construir una interfaz web.
- Herramienta de migraciones: Flyway, Liquibase o scripts SQL versionados.
