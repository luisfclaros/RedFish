# Contratos entre modulos de RedFish

## Proposito

Este documento define los contratos internos iniciales entre modulos del
monolito modular de RedFish.

Los contratos evitan que un modulo modifique directamente datos de otro modulo y
permiten mantener limites de dominio claros.

---

## Principio general

```text
Un modulo solo modifica sus propios datos.
Si necesita una accion de otro modulo, debe usar un puerto o caso de uso.
```

Esto permite que RedFish siga siendo un monolito modular y no una coleccion de
clases acopladas entre si.

---

## Pedidos e Inventario

### Necesidad

Pedidos necesita validar disponibilidad y reservar productos antes de guardar o
confirmar un pedido.

### Contrato

```text
InventoryReservationPort
```

### Regla

```text
orders -> InventoryReservationPort
inventory protege existencias y movimientos
```

Pedidos no debe descontar stock directamente ni modificar tablas de inventario.

---

## Despachos y Vehiculos

### Necesidad

Despachos necesita confirmar que un vehiculo puede ser asignado antes de
programar la entrega de un pedido.

### Contrato

```text
VehicleAvailabilityPort
```

### Regla

```text
dispatches -> VehicleAvailabilityPort
vehicles protege estado, placa y capacidad del vehiculo
```

Despachos no debe depender directamente de la entidad `Vehicle` ni modificar la
tabla `vehiculos`.

---

## Produccion e Inventario

### Necesidad

Produccion y Alimentacion puede requerir descontar alimento o insumos cuando se
registre una alimentacion.

### Contrato

```text
FeedingInventoryPort
```

### Regla

```text
production -> FeedingInventoryPort
inventory registra la salida del insumo
```

Produccion no debe modificar directamente cantidades de inventario.

---

## Seguridad y contextos operativos

### Necesidad

Los contextos operativos necesitan registrar el responsable de una accion, pero
no deben administrar credenciales ni roles.

### Contrato inicial

```text
UserRepositoryPort
```

### Regla

```text
security protege usuarios, roles y estado de cuenta
otros modulos solo referencian el usuario responsable
```

---

## Reportes y Consultas

Reportes puede leer informacion de varios contextos, pero no debe modificar
datos transaccionales.

Cuando el acoplamiento aumente, sus consultas deben evolucionar hacia vistas,
proyecciones o contratos de lectura controlados.
