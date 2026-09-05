# Mapa de contexto de RedFish

## Proposito

Este documento describe las relaciones iniciales entre los bounded contexts de
RedFish. El mapa busca reducir acoplamiento y servir como guia para el monolito
modular.

---

## Mapa general

```text
                 Seguridad y Usuarios
                         |
                         v
Produccion y Alimentacion -> Inventario -> Pedidos -> Despachos -> Reportes
                                  ^             ^          ^
                                  |             |          |
                                  +-------------+      Vehiculos
```

Interpretacion:

- Seguridad y Usuarios actua como contexto transversal.
- Produccion y Alimentacion puede generar consumo que afecte Inventario.
- Inventario protege productos, existencias y movimientos.
- Pedidos depende de Inventario para validar disponibilidad.
- Despachos depende de Pedidos para conocer que se debe enviar.
- Despachos depende de Vehiculos para asignar transporte.
- Reportes consume informacion de varios contextos, pero no modifica datos
  transaccionales.

---

## Relaciones entre contextos

| Origen | Destino | Relacion | Motivo |
|---|---|---|---|
| Produccion y Alimentacion | Inventario | Consumidor / Proveedor | La alimentacion puede requerir consumo de insumos. |
| Inventario | Pedidos | Proveedor | Pedidos necesita validar disponibilidad de productos. |
| Pedidos | Inventario | Consumidor | Pedidos solicita reserva o descuento de productos. |
| Pedidos | Despachos | Proveedor | Un pedido aprobado puede generar un despacho. |
| Despachos | Vehiculos | Consumidor | Un despacho necesita un vehiculo disponible. |
| Despachos | Pedidos | Consumidor / Coordinador | El estado del despacho puede afectar el estado del pedido. |
| Seguridad y Usuarios | Todos | Transversal | Los procesos registran responsables y permisos. |
| Reportes y Consultas | Todos | Lectura | Consolida informacion para seguimiento. |

---

## Reglas de comunicacion

- Un contexto no debe modificar directamente datos de otro contexto.
- Las modificaciones deben pasar por casos de uso del contexto dueno.
- Las consultas entre contextos deben ser explicitas y controladas.
- Las operaciones criticas deben proteger invariantes en el contexto dueno.
- Los reportes no deben convertirse en un camino alterno para modificar datos.
- Las dependencias deben seguir el principio del monolito modular: limites claros
  dentro de una sola unidad desplegable.

---

## Flujo operativo principal

```text
1. Produccion registra actividad y alimentacion de estanques.
2. Inventario registra productos, existencias y movimientos.
3. Pedidos valida cliente, productos, cantidades y disponibilidad.
4. Despachos prepara la entrega del pedido aprobado.
5. Vehiculos aporta el recurso logistico para el despacho.
6. Reportes consolida informacion para seguimiento y decisiones.
```

---

## Riesgos de integracion

| Riesgo | Contextos involucrados | Control recomendado |
|---|---|---|
| Descuento duplicado de inventario | Pedidos, Inventario | Idempotencia en operaciones de salida. |
| Pedido despachado sin stock | Pedidos, Inventario, Despachos | Validacion transaccional antes del despacho. |
| Vehiculo asignado a multiples despachos incompatibles | Despachos, Vehiculos | Validacion de disponibilidad y capacidad. |
| Reportes consultando tablas internas sin contrato | Reportes, Todos | Vistas o consultas de lectura controladas. |
| Reglas de usuario mezcladas con reglas operativas | Seguridad y Usuarios, Todos | Separar autorizacion de reglas de negocio. |

---

## Decisiones abiertas

- Definir si `clientes` permanece dentro de Pedidos o evoluciona a un contexto
  Comercial independiente.
- Definir si Alimentacion requiere integracion obligatoria con Inventario para
  descontar insumos.
- Definir si Reportes usara vistas, tablas proyectadas o consultas directas
  controladas.
- Definir los eventos o contratos internos necesarios cuando inicie la
  implementacion.
