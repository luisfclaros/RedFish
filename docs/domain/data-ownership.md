# Propiedad de datos de RedFish

## Proposito

Este documento define el contexto dueno de cada tabla preliminar del modelo de
datos de RedFish.

La propiedad de datos no significa que otros contextos no puedan consultar
informacion. Significa que solo el contexto dueno puede modificar directamente
sus datos y proteger sus reglas de negocio.

---

## Matriz de propiedad

| Tabla | Contexto dueno | Puede modificar | Puede consultar |
|---|---|---|---|
| `usuarios` | Seguridad y Usuarios | Seguridad y Usuarios | Todos los contextos con necesidad de auditoria o permisos |
| `roles` | Seguridad y Usuarios | Seguridad y Usuarios | Todos los contextos con necesidad de autorizacion |
| `estanques` | Produccion y Alimentacion | Produccion y Alimentacion | Inventario, Reportes y Consultas |
| `alimentacion` | Produccion y Alimentacion | Produccion y Alimentacion | Inventario, Reportes y Consultas |
| `productos` | Inventario | Inventario | Pedidos, Produccion y Alimentacion, Reportes y Consultas |
| `inventario` | Inventario | Inventario | Pedidos, Despachos, Reportes y Consultas |
| `movimientos_inventario` | Inventario | Inventario | Pedidos, Produccion y Alimentacion, Reportes y Consultas |
| `clientes` | Pedidos | Pedidos | Despachos, Reportes y Consultas |
| `pedidos` | Pedidos | Pedidos | Inventario, Despachos, Reportes y Consultas |
| `detalle_pedido` | Pedidos | Pedidos | Inventario, Despachos, Reportes y Consultas |
| `despachos` | Despachos | Despachos | Pedidos, Vehiculos, Reportes y Consultas |
| `vehiculos` | Vehiculos | Vehiculos | Despachos, Reportes y Consultas |

---

## Reglas de modificacion

- Seguridad y Usuarios es el unico contexto que administra usuarios y roles.
- Produccion y Alimentacion es el unico contexto que administra estanques y
  registros de alimentacion.
- Inventario es el unico contexto que modifica existencias y movimientos.
- Pedidos es el unico contexto que crea y modifica pedidos y detalles.
- Despachos es el unico contexto que crea y modifica despachos.
- Vehiculos es el unico contexto que administra vehiculos.
- Reportes y Consultas no modifica tablas transaccionales.

---

## Reglas de consulta

- Las consultas deben responder a una necesidad funcional concreta.
- La lectura de datos de otro contexto no autoriza su modificacion directa.
- Las consultas criticas deben evolucionar hacia contratos internos, vistas o
  proyecciones cuando el acoplamiento aumente.
- Los reportes pueden aceptar consistencia eventual si no afectan una operacion
  transaccional inmediata.

---

## Invariantes protegidas por contexto

| Contexto | Invariantes iniciales |
|---|---|
| Seguridad y Usuarios | Usuario unico, rol valido, estado activo para operar. |
| Produccion y Alimentacion | Estanque valido, registro con fecha y cantidad, responsable identificado. |
| Inventario | Stock no negativo, producto valido, movimiento trazable. |
| Pedidos | Pedido con cliente y detalle, total coherente, estado valido. |
| Despachos | Pedido despachable, vehiculo valido, estado logistico coherente. |
| Vehiculos | Placa unica, vehiculo activo para asignacion, capacidad registrada. |
| Reportes y Consultas | Lectura sin modificacion de datos transaccionales. |

---

## Implicaciones para la implementacion

En el monolito modular, cada carpeta dentro de `modules/` representa un modulo o
bounded context, no una entidad individual.

Las entidades pertenecen al dominio interno de cada modulo. Por ejemplo,
`orders/` puede contener entidades como `Pedido`, `Cliente` y
`DetallePedido`; `inventory/` puede contener entidades como `Producto`,
`Inventario` y `MovimientoInventario`.

Una estructura posible seria:

```text
src/
└── modules/
    ├── security/
    │   ├── domain/
    │   ├── application/
    │   └── infrastructure/
    ├── production/
    │   ├── domain/
    │   ├── application/
    │   └── infrastructure/
    ├── inventory/
    │   ├── domain/
    │   ├── application/
    │   └── infrastructure/
    ├── orders/
    │   ├── domain/
    │   ├── application/
    │   └── infrastructure/
    ├── dispatches/
    │   ├── domain/
    │   ├── application/
    │   └── infrastructure/
    ├── vehicles/
    │   ├── domain/
    │   ├── application/
    │   └── infrastructure/
    └── reporting/
        ├── application/
        └── infrastructure/
```

La estructura definitiva puede cambiar cuando se seleccione el stack tecnico,
pero debe conservar la idea principal: cada modulo protege sus datos y reglas.
