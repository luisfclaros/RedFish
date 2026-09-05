# Modelo de dominio inicial de RedFish

## Proposito

Este documento resume el modelo de dominio inicial implementado en `HU-005`.

El modelo parte de los bounded contexts definidos en `HU-003` y de la
arquitectura aceptada en `HU-004`.

---

## Principio de organizacion

El codigo se organiza por modulo o bounded context:

```text
RedFish/src/main/java/com/RedFish/RedFish/
├── security/
├── production/
├── inventory/
├── orders/
├── dispatches/
├── vehicles/
└── shared/
```

Dentro de cada modulo, las entidades se ubican en:

```text
domain/model/
```

Esto mantiene la decision arquitectonica: los modulos no son entidades
individuales; cada modulo contiene las entidades relacionadas con una capacidad
de negocio.

---

## Seguridad y Usuarios

| Modelo | Responsabilidad |
|---|---|
| `User` | Representa un usuario del sistema, su rol y estado activo/inactivo. |
| `Role` | Representa un rol asignable a usuarios. |

Invariantes:

- el usuario debe tener nombre, username, password hash y rol;
- un rol debe tener nombre;
- un usuario puede activarse o desactivarse.

---

## Produccion y Alimentacion

| Modelo | Responsabilidad |
|---|---|
| `Pond` | Representa un estanque productivo. |
| `FeedingRecord` | Representa un registro de alimentacion de un estanque. |

Invariantes:

- un estanque debe tener codigo, nombre, ubicacion y capacidad;
- un estanque inactivo no puede recibir registros de alimentacion;
- un registro de alimentacion debe tener fecha, tipo de alimento, cantidad y
  responsable;
- la cantidad de alimentacion debe ser positiva.

---

## Inventario

| Modelo | Responsabilidad |
|---|---|
| `Product` | Representa un producto o insumo. |
| `InventoryItem` | Representa la existencia disponible de un producto. |
| `InventoryMovement` | Representa una entrada o salida de inventario. |
| `ProductType` | Clasifica productos e insumos. |
| `MovementType` | Clasifica entradas y salidas. |

Invariantes:

- el producto debe tener codigo, nombre, unidad de medida y tipo;
- un producto inactivo no debe usarse en operaciones;
- la cantidad de inventario no puede ser negativa;
- las entradas y salidas deben tener cantidad positiva;
- una salida no puede dejar el inventario en negativo.

---

## Pedidos

| Modelo | Responsabilidad |
|---|---|
| `Customer` | Representa el cliente que realiza pedidos. |
| `Order` | Representa el agregado principal de pedido. |
| `OrderItem` | Representa un producto solicitado dentro del pedido. |
| `OrderStatus` | Define estados validos del pedido. |

Invariantes:

- un pedido debe pertenecer a un cliente activo;
- un pedido debe tener al menos un item;
- cada item debe tener producto, nombre, cantidad y precio unitario;
- cantidad y precio unitario deben ser positivos;
- el total del pedido se calcula desde sus items;
- un pedido despachado no puede cancelarse.

---

## Despachos

| Modelo | Responsabilidad |
|---|---|
| `Dispatch` | Representa la preparacion y entrega de un pedido. |
| `DispatchStatus` | Define estados validos del despacho. |

Invariantes:

- un despacho debe estar asociado a un pedido;
- un despacho debe tener vehiculo activo;
- un despacho debe tener fecha y direccion de entrega;
- un despacho entregado no puede cancelarse;
- la entrega debe registrar fecha y persona que recibe.

---

## Vehiculos

| Modelo | Responsabilidad |
|---|---|
| `Vehicle` | Representa un vehiculo disponible para despachos. |

Invariantes:

- un vehiculo debe tener placa, marca, modelo y capacidad;
- un vehiculo inactivo no puede asignarse a despachos.

---

## Elementos compartidos

| Modelo | Responsabilidad |
|---|---|
| `DomainValidation` | Centraliza validaciones basicas reutilizables del dominio. |

---

## Pendientes de modelado

- Convertir campos primitivos relevantes en Value Objects.
- Definir eventos de dominio.
- Definir agregados finales por contexto.
- Incorporar repositorios como puertos de dominio o aplicacion.
- Mapear entidades de persistencia cuando se implemente JPA.
- Refinar reglas con datos reales del negocio.
