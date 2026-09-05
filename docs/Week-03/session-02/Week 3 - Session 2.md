# Week 3 - Sesion 2

## Definicion de contratos y puertos entre modulos

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 3  
**Sesion:** 2  
**Historia de usuario:** HU-006

---

## 1. Objetivo de la sesion

Definir contratos internos y puertos de aplicacion para que los modulos de
RedFish puedan colaborar sin romper los limites del monolito modular.

Esta sesion toma como base el modelo de dominio creado en `HU-005` y prepara el
camino para futuras implementaciones de persistencia, servicios REST y pruebas
con herramientas como Postman.

---

## 2. Historia de usuario

### HU-006 - Definir propiedad de datos y contratos

**Como** responsable tecnico de RedFish,  
**quiero** definir contratos y puertos entre modulos,  
**para** evitar acoplamiento directo entre contextos y preparar la futura
integracion con persistencia e interfaces HTTP.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se crean puertos de aplicacion para repositorios principales. |
| CA-02 | Se crean contratos internos para colaboracion entre modulos. |
| CA-03 | Los modulos evitan modificar directamente datos de otros modulos. |
| CA-04 | Se corrige acoplamiento directo entre Despachos y Vehiculos. |
| CA-05 | Se agregan servicios de aplicacion iniciales para validar los contratos. |
| CA-06 | Se agregan pruebas unitarias de contratos de aplicacion. |
| CA-07 | Se documentan puertos, reglas y pendientes de infraestructura. |

---

## 3. Cambios implementados

Se agregan puertos de aplicacion y servicios iniciales en el proyecto Spring
Boot ubicado en `RedFish/`.

| Modulo | Cambio | Proposito |
|---|---|---|
| Inventario | `InventoryAvailabilityPort`, `InventoryMovementPort`, `InventoryRepositoryPort` | Exponer disponibilidad, movimientos y persistencia abstracta. |
| Pedidos | `InventoryReservationPort`, `OrderRepositoryPort`, `CreateOrderService` | Crear pedidos sin modificar inventario directamente. |
| Despachos | `VehicleAvailabilityPort`, `DispatchRepositoryPort`, `ScheduleDispatchService` | Programar despachos validando vehiculos por contrato. |
| Vehiculos | `VehicleRepositoryPort` | Preparar persistencia abstracta de vehiculos. |
| Produccion | `FeedingInventoryPort` | Preparar integracion de alimentacion con consumo de inventario. |
| Seguridad | `UserRepositoryPort` | Preparar persistencia abstracta de usuarios. |

---

## 4. Correccion arquitectonica aplicada

Antes de esta sesion, `Dispatch` recibia directamente un objeto `Vehicle`. Eso
generaba una dependencia del dominio de Despachos hacia el dominio de Vehiculos.

La correccion aplicada fue:

```text
Antes:
Dispatch -> Vehicle

Despues:
ScheduleDispatchService -> VehicleAvailabilityPort
Dispatch -> vehicleId
```

Con esto, Despachos ya no necesita importar la entidad `Vehicle`. La validacion
de disponibilidad del vehiculo queda expresada mediante un puerto de aplicacion.

---

## 5. Contratos principales

### Pedidos e Inventario

```text
CreateOrderService
    -> InventoryReservationPort
    -> OrderRepositoryPort
```

Regla:

```text
Pedidos no modifica inventario directamente.
Pedidos solicita reserva o validacion mediante un contrato.
```

### Despachos y Vehiculos

```text
ScheduleDispatchService
    -> VehicleAvailabilityPort
    -> DispatchRepositoryPort
```

Regla:

```text
Despachos no consulta ni modifica vehiculos directamente.
Despachos solicita validacion de disponibilidad mediante un contrato.
```

### Produccion e Inventario

```text
FeedingInventoryPort
```

Regla:

```text
Produccion y Alimentacion no descuenta inventario directamente.
El consumo de alimento debe pasar por un contrato del contexto Inventario.
```

---

## 6. Pruebas agregadas

Se agregan pruebas en:

```text
RedFish/src/test/java/com/RedFish/RedFish/modules/application/ApplicationContractTests.java
```

Las pruebas validan:

- `CreateOrderService` solicita reserva de inventario antes de guardar el
  pedido.
- `ScheduleDispatchService` valida disponibilidad de vehiculo antes de guardar
  el despacho.
- La programacion de despacho se detiene si el vehiculo no puede asignarse.

Tambien se ajustan pruebas de dominio para reflejar que `Dispatch` ya no depende
directamente de `Vehicle`.
---

## 7. Pendientes

Esta historia no implementa:

- repositorios JPA concretos;
- consultas reales a base de datos;
- controladores REST;
- DTOs;
- endpoints HTTP;
- colecciones Postman;
- autenticacion funcional;
- migraciones de base de datos.

Estos elementos deben implementarse en historias posteriores.

---
## 8. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| Puertos de aplicacion | Implementados |
| Contratos entre modulos | Implementados |
| Servicios de aplicacion iniciales | Implementados |
| Pruebas de contratos | Implementadas |
| Repositorios concretos | Pendientes |
| Endpoints HTTP | Pendientes |
| Validacion QA | Pendiente en rama `hu-006-qa` |

