# Week 2 - Sesion 1

## Identificacion de bounded contexts del dominio RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 2  
**Sesion:** 1  
**Historia de usuario:** HU-003

---

## 1. Objetivo de la sesion

Identificar los bounded contexts iniciales del dominio RedFish a partir del
problema documentado en Week 1, la arquitectura propuesta como monolito modular
y el esquema preliminar de base de datos.

El resultado de esta sesion sirve como base para refinar los modulos internos,
la propiedad de datos y las reglas de comunicacion entre capacidades del
sistema.

---

## 2. Historia de usuario

### HU-003 - Identificar bounded contexts del dominio RedFish

**Como** responsable del diseno de RedFish,  
**quiero** identificar los bounded contexts principales del dominio,  
**para** organizar el monolito modular con limites claros de responsabilidad,
datos y reglas de negocio.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se identifican los bounded contexts principales del dominio. |
| CA-02 | Cada bounded context tiene una responsabilidad clara. |
| CA-03 | Las tablas del modelo preliminar se asocian con un contexto dueno. |
| CA-04 | Se documentan las relaciones entre contextos. |
| CA-05 | Se identifican riesgos iniciales de acoplamiento. |
| CA-06 | Se documentan reglas de propiedad de datos. |
| CA-07 | El analisis es compatible con la arquitectura de monolito modular. |
| CA-08 | Se registra la fuente usada para el analisis del modelo de datos. |

---

## 3. Fuentes de analisis

El analisis parte de:

- documentacion de Week 1;
- ADR-001, donde se propone monolito modular;
- backlog inicial del MVP;
- esquema preliminar de base de datos del repositorio `BaseDeDatosRedFish`.

Repositorio de base de datos:

```text
https://github.com/luisfclaros/BaseDeDatosRedFish
```

Tablas observadas en el esquema preliminar:

```text
usuarios
roles
estanques
alimentacion
productos
inventario
movimientos_inventario
clientes
pedidos
detalle_pedido
despachos
vehiculos
```

---

## 4. Bounded contexts identificados

| Bounded context | Responsabilidad principal | Tablas asociadas |
|---|---|---|
| Seguridad y Usuarios | Gestionar usuarios, roles, acceso y estado de cuentas. | `usuarios`, `roles` |
| Produccion y Alimentacion | Gestionar estanques y registros de alimentacion. | `estanques`, `alimentacion` |
| Inventario | Gestionar productos, existencias y movimientos. | `productos`, `inventario`, `movimientos_inventario` |
| Pedidos | Gestionar clientes, pedidos y detalle de productos solicitados. | `clientes`, `pedidos`, `detalle_pedido` |
| Despachos | Preparar y controlar el envio de pedidos. | `despachos` |
| Vehiculos | Administrar vehiculos disponibles para despachos. | `vehiculos` |
| Reportes y Consultas | Consolidar informacion operativa para consulta y toma de decisiones. | No posee tablas transaccionales iniciales |

El detalle de cada contexto se encuentra en:

```text
docs/domain/bounded-contexts.md
```

---

## 5. Mapa de contexto inicial

El flujo principal del sistema se interpreta asi:

```text
Produccion y Alimentacion
          |
          v
      Inventario
          |
          v
       Pedidos
          |
          v
      Despachos <---- Vehiculos
          |
          v
  Reportes y Consultas

Seguridad y Usuarios aplica como contexto transversal.
```

El mapa completo se documenta en:

```text
docs/domain/context-map.md
```

---

## 6. Propiedad de datos

Cada tabla debe tener un contexto dueno. El contexto dueno es el unico
responsable de modificar directamente sus datos y proteger sus reglas de
negocio.

Otros contextos pueden consultar informacion mediante casos de uso, consultas
controladas o contratos internos, pero no deben modificar tablas que no les
pertenecen.

El detalle se encuentra en:

```text
docs/domain/data-ownership.md
```

---

## 7. Riesgos iniciales de acoplamiento

| Riesgo | Impacto | Mitigacion inicial |
|---|---|---|
| Pedidos modificando inventario directamente | Inconsistencias de stock | La reserva o descuento debe pasar por reglas del contexto Inventario. |
| Despachos cambiando estado de pedidos sin coordinacion | Estados incoherentes | El cambio de estado debe estar coordinado con el contexto Pedidos. |
| Alimentacion dependiendo de consultas directas a inventario | Acoplamiento entre modulos | Registrar consumo mediante caso de uso o contrato interno. |
| Reportes consultando tablas transaccionales sin control | Consultas fragiles y acopladas | Crear consultas de lectura separadas o vistas controladas. |
| Usuarios mezclado con reglas de negocio operativas | Responsabilidades confusas | Mantener autenticacion/autorizacion separada del dominio operativo. |

---

## 8. Decisiones de la sesion

- RedFish mantiene como arquitectura propuesta un monolito modular.
- Los bounded contexts iniciales no representan microservicios.
- Cada contexto puede convertirse en modulo interno del monolito.
- La base de datos actual se toma como insumo preliminar, no como contrato
  definitivo.
- La propiedad de datos debe documentarse antes de implementar persistencia.
- `Reportes y Consultas` se reconoce como contexto de lectura, no como dueno de
  datos transaccionales en esta etapa.


