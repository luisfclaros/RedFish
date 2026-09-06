# Bounded contexts de RedFish

## Proposito

Este documento identifica los bounded contexts iniciales de RedFish y los
relaciona con las tablas del modelo preliminar de base de datos.

Los contextos aqui definidos no son microservicios. En esta etapa representan
limites de responsabilidad dentro del monolito modular propuesto.

---

## 1. Seguridad y Usuarios

### Responsabilidad

Gestionar usuarios, roles, acceso y estado de las cuentas del sistema.

### Tablas asociadas

- `usuarios`
- `roles`

### Entidades principales

- Usuario.
- Rol.

### Reglas iniciales

- Un usuario debe tener un rol asignado.
- El nombre de usuario debe ser unico.
- Un usuario inactivo no debe operar procesos del sistema.
- Las reglas de autenticacion y autorizacion no deben mezclarse con reglas de
  produccion, inventario, pedidos o despachos.

### Relaciones con otros contextos

Este contexto es transversal. Otros contextos pueden necesitar conocer el usuario
que registra una operacion, pero no deben administrar roles ni credenciales.

---

## 2. Produccion y Alimentacion

### Responsabilidad

Gestionar estanques y registros de alimentacion asociados a la produccion
piscicola.

### Tablas asociadas

- `estanques`
- `alimentacion`

### Entidades principales

- Estanque.
- Registro de alimentacion.

### Reglas iniciales

- Un estanque puede tener muchos registros de alimentacion.
- La alimentacion debe registrarse con fecha, cantidad, tipo de alimento y
  responsable.
- No se deben registrar operaciones sobre estanques inactivos.
- La alimentacion puede impactar inventario cuando el alimento se maneje como
  producto o insumo.

### Relaciones con otros contextos

- Puede consumir informacion del contexto Inventario para registrar consumo de
  alimento.
- Usa Seguridad y Usuarios para identificar el responsable del registro.
- Puede entregar datos a Reportes y Consultas.

---

## 3. Inventario

### Responsabilidad

Gestionar productos, existencias, stock minimo y movimientos de inventario.

### Tablas asociadas

- `productos`
- `inventario`
- `movimientos_inventario`

### Entidades principales

- Producto.
- Inventario.
- Movimiento de inventario.

### Reglas iniciales

- Un producto puede tener un registro de inventario.
- Cada movimiento debe representar una entrada o salida.
- No se deben permitir existencias negativas.
- El stock minimo debe permitir alertas o validaciones operativas.
- Los movimientos deben quedar trazados con fecha y usuario responsable.

### Relaciones con otros contextos

- Pedidos consulta disponibilidad de productos.
- Pedidos puede solicitar reserva o descuento de inventario mediante un caso de
  uso controlado.
- Produccion y Alimentacion puede registrar consumo de insumos si aplica.
- Reportes y Consultas usa datos de inventario para analisis operativo.

---

## 4. Pedidos

### Responsabilidad

Gestionar clientes, pedidos, detalle de productos solicitados, totales y estado
del pedido.

### Tablas asociadas

- `clientes`
- `pedidos`
- `detalle_pedido`

### Entidades principales

- Cliente.
- Pedido.
- Detalle de pedido.

### Reglas iniciales

- Un pedido pertenece a un cliente.
- Un pedido debe tener uno o varios productos.
- El subtotal de cada detalle debe corresponder a cantidad por precio unitario.
- El total del pedido debe representar la suma de sus detalles.
- Un pedido no debe avanzar a despacho si no existe disponibilidad suficiente en
  inventario.
- Los estados iniciales son: pendiente, en preparacion, despachado y cancelado.

### Relaciones con otros contextos

- Consulta Inventario para validar disponibilidad.
- Solicita cambios de inventario cuando el pedido se confirma o prepara.
- Entrega pedidos listos al contexto Despachos.
- Usa Reportes y Consultas para seguimiento comercial y operativo.

---

## 5. Despachos

### Responsabilidad

Gestionar la preparacion, asignacion, envio y entrega de pedidos despachados.

### Tablas asociadas

- `despachos`

### Entidades principales

- Despacho.

### Reglas iniciales

- Un despacho se asocia a un pedido.
- Un despacho se asocia a un vehiculo.
- No se debe despachar un pedido sin productos suficientes en inventario.
- El despacho debe registrar fecha de despacho, fecha de entrega, estado,
  direccion de entrega y persona que recibe.
- Los estados iniciales son: programado, en transito, entregado y cancelado.

### Relaciones con otros contextos

- Recibe pedidos aprobados desde Pedidos.
- Solicita disponibilidad o asignacion desde Vehiculos.
- Puede provocar actualizaciones de estado en Pedidos.
- Entrega informacion a Reportes y Consultas.

---

## 6. Vehiculos

### Responsabilidad

Administrar los vehiculos usados para transportar pedidos.

### Tablas asociadas

- `vehiculos`

### Entidades principales

- Vehiculo.

### Reglas iniciales

- La placa del vehiculo debe ser unica.
- Un vehiculo inactivo no debe asignarse a despachos.
- La capacidad de carga debe considerarse al preparar despachos.
- Las observaciones del vehiculo deben conservar informacion operativa relevante.

### Relaciones con otros contextos

- Despachos consulta y asigna vehiculos disponibles.
- Reportes y Consultas puede usar informacion de vehiculos para seguimiento
  logistico.

---

## 7. Reportes y Consultas

### Responsabilidad

Consolidar informacion operativa para analisis, seguimiento y toma de
decisiones.

### Tablas asociadas

No posee tablas transaccionales iniciales. Puede usar vistas, consultas de
lectura o proyecciones cuando la implementacion lo requiera.

### Reglas iniciales

- No debe modificar datos de contextos transaccionales.
- Debe evitar consultas que rompan los limites internos de los modulos.
- Puede aceptar consistencia eventual cuando la consulta no afecte una decision
  transaccional inmediata.

### Relaciones con otros contextos

Consume informacion de Produccion y Alimentacion, Inventario, Pedidos,
Despachos y Vehiculos.

---

## Resumen de limites

| Contexto | Tipo de responsabilidad | Observacion |
|---|---|---|
| Seguridad y Usuarios | Transversal | Controla acceso e identidad. |
| Produccion y Alimentacion | Operativa | Registra actividad productiva. |
| Inventario | Operativa y transaccional | Protege existencias y movimientos. |
| Pedidos | Comercial y transaccional | Coordina solicitud de productos. |
| Despachos | Logistica | Controla salida y entrega de pedidos. |
| Vehiculos | Recurso logistico | Administra capacidad de transporte. |
| Reportes y Consultas | Lectura | Consolida informacion sin modificarla. |
