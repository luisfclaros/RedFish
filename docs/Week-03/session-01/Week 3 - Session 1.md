# Week 3 - Sesion 1

## Modelado inicial del dominio RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 3  
**Sesion:** 1  
**Historia de usuario:** HU-005

---

## 1. Objetivo de la sesion

Modelar las entidades iniciales del dominio RedFish dentro de la estructura de
monolito modular definida en Week 2.

Esta sesion transforma los bounded contexts y tablas preliminares en clases de
dominio con reglas basicas de negocio, sin incorporar todavia persistencia JPA,
controladores HTTP ni repositorios concretos.

---

## 2. Historia de usuario

### HU-005 - Modelar el dominio inicial

**Como** responsable del desarrollo de RedFish,  
**quiero** crear el modelo inicial del dominio,  
**para** representar entidades, reglas e invariantes principales antes de
implementar persistencia o endpoints.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se crean entidades iniciales para los bounded contexts principales. |
| CA-02 | Las entidades se ubican dentro de sus modulos de dominio. |
| CA-03 | El modelo evita depender de JPA, controladores o infraestructura. |
| CA-04 | Se implementan invariantes iniciales del negocio. |
| CA-05 | Se agregan pruebas unitarias para reglas criticas. |
| CA-06 | El modelo respeta la arquitectura de monolito modular. |
| CA-07 | Se documenta que queda pendiente la persistencia y exposicion HTTP. |

---

## 3. Entidades modeladas

| Modulo | Entidades / modelos | Ubicacion |
|---|---|---|
| Seguridad y Usuarios | `User`, `Role` | `RedFish/src/main/java/com/RedFish/RedFish/security/domain/model/` |
| Produccion y Alimentacion | `Pond`, `FeedingRecord` | `RedFish/src/main/java/com/RedFish/RedFish/production/domain/model/` |
| Inventario | `Product`, `InventoryItem`, `InventoryMovement` | `RedFish/src/main/java/com/RedFish/RedFish/inventory/domain/model/` |
| Pedidos | `Customer`, `Order`, `OrderItem` | `RedFish/src/main/java/com/RedFish/RedFish/orders/domain/model/` |
| Despachos | `Dispatch` | `RedFish/src/main/java/com/RedFish/RedFish/dispatches/domain/model/` |
| Vehiculos | `Vehicle` | `RedFish/src/main/java/com/RedFish/RedFish/vehicles/domain/model/` |

Tambien se crean enumeraciones de dominio para estados y tipos:

```text
ProductType
MovementType
OrderStatus
DispatchStatus
```

---

## 4. Reglas e invariantes iniciales

| Contexto | Reglas modeladas |
|---|---|
| Seguridad y Usuarios | Usuario con nombre, username, password hash, rol y fecha de creacion. |
| Produccion y Alimentacion | Un estanque inactivo no puede recibir registros de alimentacion. |
| Inventario | El inventario no puede quedar con stock negativo. |
| Inventario | Entradas y salidas deben tener cantidades positivas. |
| Pedidos | Un pedido debe tener cliente activo y al menos un item. |
| Pedidos | El total del pedido se calcula desde sus items. |
| Pedidos | Las transiciones de estado deben respetar el flujo definido. |
| Despachos | Un despacho debe tener pedido, vehiculo activo, fecha y direccion. |
| Vehiculos | Un vehiculo inactivo no puede asignarse a un despacho. |

---

## 5. Decisiones tecnicas

- El modelo se implementa como dominio puro.
- No se usan anotaciones JPA en esta historia.
- No se crean controladores HTTP en esta historia.
- No se crean repositorios concretos en esta historia.
- Las entidades viven dentro de cada modulo, no como carpetas aisladas por tabla.
- La validacion compartida se centraliza en `DomainValidation`.

---

## 6. Pruebas implementadas

Se agregan pruebas unitarias en:

```text
RedFish/src/test/java/com/RedFish/RedFish/modules/domain/DomainModelTests.java
```

Las pruebas cubren:

- inventario sin stock negativo;
- calculo total de pedido;
- bloqueo de alimentacion sobre estanque inactivo;
- bloqueo de despacho con vehiculo inactivo.

La prueba generada `RedFishApplicationTests.contextLoads()` queda desactivada
temporalmente porque depende de Docker/Testcontainers y corresponde a una
validacion de infraestructura, no al alcance de esta historia de dominio.

---

## 7. Pendientes

Esta historia no cubre:

- mapeo JPA de entidades;
- repositorios de persistencia;
- servicios de aplicacion;
- controladores REST;
- DTOs;
- migraciones de base de datos;
- autenticacion funcional con Spring Security.
- validacion de contexto Spring Boot con base de datos real.

Estos elementos deben abordarse en historias posteriores.

---

## 8. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| Modelo de dominio inicial | Implementado |
| Entidades principales | Implementadas |
| Invariantes iniciales | Implementadas |
| Pruebas unitarias de dominio | Implementadas |
| Persistencia | Pendiente |
| Endpoints HTTP | Pendiente |
| Validacion QA | Pendiente en rama `hu-005-qa` |


