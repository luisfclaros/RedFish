# Puertos de repositorio de RedFish

## Proposito

Este documento resume los puertos de repositorio definidos en `HU-006`.

Los puertos representan abstracciones. Las implementaciones concretas con JPA,
MySQL u otra tecnologia se agregaran posteriormente en infraestructura.

---

## Puertos definidos

| Puerto | Modulo | Responsabilidad |
|---|---|---|
| `UserRepositoryPort` | Seguridad | Buscar y guardar usuarios. |
| `InventoryRepositoryPort` | Inventario | Buscar inventario por producto y guardar inventario. |
| `OrderRepositoryPort` | Pedidos | Guardar pedidos y buscarlos por identificador. |
| `DispatchRepositoryPort` | Despachos | Guardar despachos y buscarlos por identificador. |
| `VehicleRepositoryPort` | Vehiculos | Buscar y guardar vehiculos. |

---

## Puertos de colaboracion

| Puerto | Consumidor | Responsabilidad |
|---|---|---|
| `InventoryReservationPort` | Pedidos | Reservar o validar productos requeridos por un pedido. |
| `VehicleAvailabilityPort` | Despachos | Validar que un vehiculo pueda asignarse a un despacho. |
| `FeedingInventoryPort` | Produccion y Alimentacion | Registrar consumo de alimento o insumos en inventario. |
| `InventoryAvailabilityPort` | Contextos que consulten stock | Validar disponibilidad sin modificar inventario. |
| `InventoryMovementPort` | Contextos autorizados | Registrar entradas o salidas de inventario. |

---

## Ubicacion en codigo

```text
RedFish/src/main/java/com/RedFish/RedFish/
├── security/application/port/
├── production/application/port/
├── inventory/application/port/
├── orders/application/port/
├── dispatches/application/port/
└── vehicles/application/port/
```

---

## Reglas de implementacion futura

- Las interfaces se mantienen en la capa de aplicacion del modulo que las
  consume o gobierna.
- Las implementaciones concretas deben ubicarse en `infrastructure`.
- Los controladores no deben depender de repositorios concretos.
- El dominio no debe depender de JPA, MySQL, Spring MVC ni adaptadores externos.
- Las pruebas unitarias pueden usar implementaciones en memoria o dobles de
  prueba.
