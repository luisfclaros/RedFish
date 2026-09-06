# RedFish MVP 1 - Release Candidate

## Proposito

Este documento resume el alcance de la version candidata del MVP 1 de RedFish
antes de su promocion desde `Qa` hacia `main`.

---

## Alcance incluido

| Componente | Estado |
|---|---|
| Fundamentos del proyecto | Incluido |
| Flujo Git por ambientes | Incluido |
| Monolito modular | Incluido |
| ADR inicial de arquitectura | Incluido |
| Modelo de dominio inicial | Incluido |
| Contratos entre modulos | Incluido |
| Walking skeleton backend | Incluido |
| API de productos | Incluida |
| API de clientes | Incluida |
| Persistencia MySQL | Incluida |
| Docker Compose | Incluido |
| Pruebas automatizadas | Incluidas |
| Documentacion QA | Incluida por HU |

---

## Stack validado

```text
Backend: Spring Boot + Java
Base de datos: MySQL
Contenedores: Docker Compose
Pruebas: JUnit + Spring Test
Documentacion: Markdown
```

---

## Endpoints disponibles

```text
POST /api/products
GET  /api/products
GET  /api/products/{id}

POST /api/customers
GET  /api/customers
GET  /api/customers/{id}
```

---

## Tablas esperadas

```text
productos
clientes
```

---

## Criterio de liberacion

El MVP 1 puede promoverse a `main` cuando:

- la rama `Qa` contenga las historias aprobadas hasta `HU-010`;
- las pruebas automatizadas finalicen con `BUILD SUCCESSFUL`;
- las pruebas manuales principales respondan correctamente en Postman;
- MySQL Workbench permita visualizar `productos` y `clientes`;
- no existan defectos bloqueantes conocidos.

---

## Pendientes post-MVP

- Autenticacion y autorizacion real.
- CRUD completo por recurso.
- Pedidos y detalles de pedido.
- Movimientos y disponibilidad de inventario.
- Despachos y seguimiento logistico.
- Migraciones versionadas de base de datos.
- Perfiles de configuracion para ambientes.
