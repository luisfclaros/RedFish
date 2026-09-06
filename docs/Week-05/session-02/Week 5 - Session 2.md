# Week 5 - Sesion 2

## Validacion y liberacion del MVP 1

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 5  
**Sesion:** 2  
**Historia de usuario:** HU-010

---

## 1. Objetivo de la sesion

Validar el estado del MVP 1 de RedFish y preparar la version candidata para ser
promovida desde `Qa` hacia `main` mediante Pull Request.

Esta HU no agrega una entidad nueva. Su proposito es cerrar el primer ciclo de
entrega revisando que arquitectura, backend, base de datos, Docker, endpoints,
pruebas y documentacion se encuentren alineados.

---

## 2. Historia de usuario

### HU-010 - Validar y liberar el MVP 1

**Como** responsable del desarrollo de RedFish,  
**quiero** validar el MVP 1 completo,  
**para** confirmar que el backend, la base de datos, Docker, la documentacion y
los endpoints principales estan listos para ser promovidos a `main`.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se revisa que el stack del proyecto sea consistente: Spring Boot + MySQL. |
| CA-02 | Se valida que el backend tenga endpoints funcionales para productos. |
| CA-03 | Se valida que el backend tenga endpoints funcionales para clientes. |
| CA-04 | Se confirma que MySQL puede ejecutarse mediante Docker Compose. |
| CA-05 | Se confirma que el backend puede ejecutarse localmente con Spring Boot. |
| CA-06 | Se documenta el alcance real del MVP 1. |
| CA-07 | Se registran pendientes conocidos para historias posteriores. |
| CA-08 | Se ejecutan pruebas automatizadas del backend. |
| CA-09 | Se prepara comentario para Pull Request de `Qa` hacia `main`. |

---

## 3. Alcance validado del MVP 1

El MVP 1 de RedFish incluye:

| Area | Resultado |
|---|---|
| Arquitectura | Monolito modular documentado. |
| Backend | Spring Boot con Java. |
| Base de datos | MySQL. |
| Contenedores | Docker Compose para MySQL y perfil para backend. |
| Modulo Inventario | API inicial de productos. |
| Modulo Pedidos | API inicial de clientes. |
| Persistencia | JPA/Hibernate sobre MySQL. |
| Pruebas | Pruebas unitarias y de controlador. |
| Documentacion | Entregables por semana, ADR, dominio, API y QA. |

---

## 4. Recursos funcionales del MVP

### Productos

Endpoints disponibles:

```text
POST /api/products
GET  /api/products
GET  /api/products/{id}
```

Tabla asociada:

```text
productos
```

### Clientes

Endpoints disponibles:

```text
POST /api/customers
GET  /api/customers
GET  /api/customers/{id}
```

Tabla asociada:

```text
clientes
```

En esta version, productos y clientes funcionan como recursos independientes.
La relacion entre clientes y productos se incorporara posteriormente mediante
el flujo de pedidos.

---

## 5. Ejecucion local esperada

### Levantar MySQL con Docker

```powershell
cd D:\descargas\pago\RedFish\RedFish
docker compose up -d mysql
```

### Ejecutar backend local

```powershell
.\gradlew.bat bootRun
```

### Ejecutar backend y MySQL con Docker

```powershell
docker compose --profile container up --build
```

---

## 6. Validacion manual esperada

Peticiones principales:

```text
POST http://localhost:8080/api/products
GET  http://localhost:8080/api/products
POST http://localhost:8080/api/customers
GET  http://localhost:8080/api/customers
```

Validacion en MySQL Workbench:

```sql
SHOW TABLES;
SELECT * FROM redfish.productos;
SELECT * FROM redfish.clientes;
```

---

## 7. Pendientes conocidos

Estos elementos quedan fuera del MVP 1 y se consideran candidatos para
historias posteriores:

- autenticacion real de usuarios;
- CRUD completo de productos;
- CRUD completo de clientes;
- creacion de pedidos;
- relacion `Cliente -> Pedido -> DetallePedido -> Producto`;
- control de inventario por existencias;
- despachos asociados a pedidos;
- migraciones versionadas con Flyway o Liquibase;
- perfiles separados para desarrollo, QA y produccion;
- configuracion de secretos fuera del repositorio.

---

## 8. Validacion tecnica

Pruebas automatizadas:

```powershell
.\gradlew.bat test
```

Resultado esperado:

```text
BUILD SUCCESSFUL
```

Resultado obtenido:

```text
BUILD SUCCESSFUL
```

---

## 9. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| Alcance del MVP 1 | Documentado |
| Endpoints de productos | Validados como parte del MVP |
| Endpoints de clientes | Validados como parte del MVP |
| MySQL con Docker | Incluido en el cierre |
| Backend con Docker | Incluido en el cierre |
| Pendientes posteriores | Documentados |
| Pruebas automatizadas | Aprobadas |
| Comentario PR `Qa` -> `main` | Preparado |

---

## 10. Comentario sugerido para PR de Qa a main

```text
This release promotes MVP 1 of RedFish to main. It includes the documented modular monolith architecture, the Spring Boot backend, MySQL persistence, Docker Compose support, initial product and customer REST APIs, automated tests, API documentation, and QA evidence for the completed user stories. Known post-MVP items such as authentication, full CRUD operations, orders, inventory movements, dispatch flows, and database migrations remain documented for future iterations.
```
