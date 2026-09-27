# Estimacion y compromiso del MVP 2

## 1. Punto de partida

HU-011 a HU-016 entregaron la plataforma del MVP 2: orquestacion, ambientes,
comunicacion, contratos versionados y proceso de trabajo. El incremento aun no
contiene un flujo persistente completo entre Pedidos e Inventario.

El compromiso restante busca cerrar esa diferencia sin incluir frontend,
despachos, seguridad completa ni mensajeria externa.

---

## 2. Metodo de estimacion

Se utiliza la escala relativa:

```text
1, 2, 3, 5, 8, 13
```

Los puntos combinan complejidad, cantidad de integraciones, incertidumbre y
esfuerzo de validacion. No equivalen a horas. Una historia de 8 o mas puntos se
debe discutir y dividir antes de comprometerla.

Las estimaciones de HU-016 son una linea base de planificacion. Al iniciar cada
HU se revisaran mediante planning poker y se registrara cualquier cambio con su
justificacion.

---

## 3. Backlog comprometido

### HU-017 - Versionar el esquema de base de datos

**Historia:** Como equipo de operacion, queremos aplicar migraciones de base de
datos versionadas, para reproducir el esquema sin depender de cambios
automaticos de Hibernate.

**Prioridad:** Must  
**Estimacion:** 5 puntos  
**Responsable:** Desarrollo / DevOps  
**Dependencias:** MySQL y configuracion de ambientes existentes.

Criterios de aceptacion:

- Flyway se integra al arranque de Spring Boot con soporte especifico para MySQL;
- el esquema actual puede crearse desde una base vacia;
- las migraciones quedan ordenadas e inmutables;
- Develop y QA inician con migraciones y sin edicion manual;
- produccion conserva `ddl-auto=validate`;
- la estrategia de rollback y respaldo queda documentada.

### HU-018 - Gestionar existencias y movimientos

**Historia:** Como responsable de inventario, quiero registrar entradas y
salidas y consultar existencias, para conocer la cantidad disponible de cada
producto.

**Prioridad:** Must  
**Estimacion:** 5 puntos  
**Responsable:** Modulo Inventario  
**Dependencias:** HU-017 y Productos existentes.

Criterios de aceptacion:

- una entrada positiva incrementa la existencia;
- una salida valida reduce la existencia;
- una salida que deja stock negativo responde `409 INSUFFICIENT_STOCK`;
- existencia y movimiento se persisten en una transaccion;
- la API v1 permite registrar movimientos y consultar stock;
- OpenAPI, pruebas automatizadas y migraciones se actualizan.

### HU-019 - Crear y consultar pedidos persistentes

**Historia:** Como operador comercial, quiero registrar y consultar un pedido
con sus detalles, para conservar la solicitud realizada por un cliente.

**Prioridad:** Must  
**Estimacion:** 5 puntos  
**Responsable:** Modulo Pedidos  
**Dependencias:** HU-017, Clientes y Productos existentes.

Criterios de aceptacion:

- el pedido requiere un cliente activo y al menos un detalle;
- cantidades y precios son positivos;
- total y estado inicial se calculan en el dominio;
- pedido y detalles se guardan atomicamente;
- `POST /api/v1/orders` responde `201` y `Location` versionado;
- `GET /api/v1/orders/{id}` devuelve pedido y detalles;
- OpenAPI y pruebas de contrato describen el nuevo recurso.

### HU-020 - Integrar pedidos con la reserva de inventario

**Historia:** Como operador comercial, quiero que un pedido reserve sus
productos disponibles, para impedir ventas por encima de las existencias.

**Prioridad:** Must  
**Estimacion:** 5 puntos  
**Responsable:** Pedidos e Inventario  
**Dependencias:** HU-018 y HU-019.

Criterios de aceptacion:

- Pedidos usa un puerto publico y no modifica tablas de Inventario;
- el contrato de reserva se acuerda antes del adaptador;
- la falta de stock responde `409 INSUFFICIENT_STOCK`;
- reserva y persistencia del pedido comparten una transaccion local;
- un fallo revierte pedido, detalles y movimientos;
- pruebas con dobles permiten verificar ambos lados del contrato.

### HU-021 - Hacer idempotente la creacion de pedidos

**Historia:** Como consumidor de la API, quiero repetir una solicitud de pedido
sin duplicar la reserva, para recuperarme de respuestas perdidas o reintentos.

**Prioridad:** Must  
**Estimacion:** 3 puntos  
**Responsable:** Modulo Pedidos  
**Dependencias:** HU-020.

Criterios de aceptacion:

- `POST /api/v1/orders` acepta una clave de idempotencia;
- la primera solicitud crea pedido y reserva stock;
- una repeticion con la misma clave devuelve el pedido original;
- la repeticion no crea otro movimiento ni descuenta nuevamente;
- claves iguales con cuerpos diferentes producen un error controlado;
- existen pruebas de concurrencia o restriccion atomica en persistencia.

### HU-022 - Validar y liberar el MVP 2

**Historia:** Como equipo de RedFish, queremos validar el flujo integrado y
promover el MVP 2 aprobado, para disponer de una version reproducible y
demostrable.

**Prioridad:** Must  
**Estimacion:** 3 puntos  
**Responsable:** QA / DevOps / Product Owner  
**Dependencias:** HU-017 a HU-021 aprobadas en `Qa`.

Criterios de aceptacion:

- el flujo de inventario y pedido se prueba de extremo a extremo;
- la suite automatizada y los contratos terminan correctamente;
- Develop y QA ejecutan la misma imagen inmutable;
- no existen defectos bloqueantes ni secretos versionados;
- riesgos, limitaciones y rollback quedan documentados;
- solamente despues de aprobar HU-022 se crea el PR `Qa -> main`;
- la version liberada recibe una etiqueta Git acordada.

---

## 4. Estimacion resumida

| HU | Entregable | MoSCoW | Puntos | Dependencia principal |
|---|---|---|---:|---|
| HU-017 | Migraciones versionadas | Must | 5 | Ambientes actuales |
| HU-018 | Existencias y movimientos | Must | 5 | HU-017 |
| HU-019 | Pedidos persistentes | Must | 5 | HU-017 |
| HU-020 | Reserva transaccional | Must | 5 | HU-018 y HU-019 |
| HU-021 | Creacion idempotente | Must | 3 | HU-020 |
| HU-022 | Validacion y liberacion | Must | 3 | HU-017 a HU-021 |
|  | **Total comprometido restante** |  | **26** |  |

Ninguna historia supera 5 puntos. El trabajo de integracion e idempotencia se
separo en HU-020 y HU-021 para evitar una historia de 8 puntos o mas.

---

## 5. Trabajo no comprometido

| Candidato | MoSCoW | Estimacion inicial | Decision |
|---|---|---:|---|
| HU-023 - Cliente web inicial | Should | 5 | Iniciar despues de estabilizar Pedidos y si existe capacidad. |
| Flujo de despachos | Should | 8 | Dividir antes de planificar. |
| Alertas de stock bajo | Should | 3 | Mantener bajo la linea de liberacion. |
| Reportes operativos | Could | 5 | Esperar contratos estables de pedidos. |
| Autenticacion y autorizacion completa | Could | 8 | Dividir y planificar en un incremento posterior. |
| Broker y patron Outbox | Won't now | 13 | No existe necesidad de despliegue entre procesos en el MVP 2. |

El trabajo no comprometido permanece visible, pero no se inicia si pone en
riesgo una historia Must o supera el limite WIP.

---

## 6. Dependencias y secuencia

```text
HU-017 Migraciones
   |          |
   v          v
HU-018      HU-019
Inventario  Pedidos
   \          /
    \        /
     v      v
      HU-020
      Reserva
         |
         v
      HU-021
    Idempotencia
         |
         v
      HU-022
      Release
```

| Dependencia | Contrato primero | Estrategia para evitar bloqueo |
|---|---|---|
| HU-017 -> HU-018/HU-019 | Convencion de migraciones y baseline | Probar cada migracion desde una base vacia en CI. |
| HU-018 -> HU-020 | Comando y resultado de reserva | Pedidos desarrolla contra un doble del puerto acordado. |
| HU-019 -> HU-020 | Solicitud y respuesta de pedido | Inventario prueba escenarios de reserva sin depender del controlador. |
| HU-020 -> HU-021 | Resultado estable de creacion | Simular reintentos antes de agregar persistencia idempotente. |
| HU-021 -> HU-022 | Flujo completo versionado | Coleccion Postman y prueba automatizada end-to-end. |

---

## 7. Plan por ciclos

La referencia historica es un throughput de dos HU por semana. Como no existen
puntos historicos terminados, `8-10` puntos por ciclo es una hipotesis inicial
de capacidad y no una velocidad confirmada.

| Ciclo | Historias | Puntos | Objetivo |
|---|---|---:|---|
| 1 | HU-017, HU-018 | 10 | Esquema reproducible e inventario funcional. |
| 2 | HU-019, HU-020 | 10 | Pedido persistente con reserva transaccional. |
| 3 | HU-021, HU-022 | 6 | Reintentos seguros, validacion y liberacion. |

El tercer ciclo conserva cuatro puntos de margen frente a la hipotesis de diez.
Ese margen absorbe defectos de integracion, ajustes de contrato y preparacion
de la liberacion. No se rellena automaticamente con historias `Should`.

Al terminar el primer ciclo se calcula la velocidad real de puntos completados
en `Qa` y se ajusta el compromiso de los ciclos restantes sin usar puntos como
cuota individual.

---

## 8. Riesgos y controles

| Riesgo | Impacto | Control |
|---|---|---|
| Migraciones sobre volumenes existentes | Alto | Respaldo, baseline y prueba desde base vacia y con datos. |
| Reserva duplicada por reintento | Alto | Clave idempotente y restriccion unica atomica. |
| Pedido guardado sin descontar stock | Alto | Transaccion local y prueba de rollback. |
| Acoplamiento entre tablas de modulos | Alto | Puertos de aplicacion y propiedad de datos. |
| Contrato REST cambia durante frontend | Medio | OpenAPI v1, Pact y mocks. |
| Sobreestimacion de capacidad | Medio | WIP limitado, margen del ciclo 3 y replanteamiento semanal. |

---

## 9. Compromiso

El equipo compromete HU-017 a HU-022 como alcance restante del MVP 2. El
frontend y los demas modulos permanecen debajo de la linea de liberacion.

Cada historia seguira:

```text
hu-xxx-dev -> Develop
hu-xxx-qa  -> Qa
```

Solo despues de aprobar HU-022 se ejecutara:

```text
Qa -> main
```

Si la capacidad real resulta menor, se ajusta la fecha o se vuelve a dividir
una historia Must; no se omiten pruebas, contratos, QA ni controles de datos.
