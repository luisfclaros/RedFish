# QA - Week 3 Sesion 1

## Validacion de HU-005

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Historia de usuario:** HU-005  
**Sesion:** Week 3 - Sesion 1  
**Tipo de validacion:** Modelo de dominio y pruebas unitarias  
**Rama esperada de QA:** `hu-005-qa`

---

## 1. Alcance de la validacion

Esta validacion revisa que `HU-005` implemente el modelo inicial de dominio de
RedFish de acuerdo con los bounded contexts definidos en Week 2.

La historia se enfoca en dominio puro. No incluye persistencia JPA, controladores
REST, servicios de aplicacion, migraciones ni seguridad funcional.

---

## 2. Archivos revisados

| Archivo / ruta | Proposito | Estado |
|---|---|---|
| `RedFish/src/main/java/com/RedFish/RedFish/security/domain/model/` | Modelos de usuarios y roles. | PASS |
| `RedFish/src/main/java/com/RedFish/RedFish/production/domain/model/` | Modelos de estanques y alimentacion. | PASS |
| `RedFish/src/main/java/com/RedFish/RedFish/inventory/domain/model/` | Modelos de productos, inventario y movimientos. | PASS |
| `RedFish/src/main/java/com/RedFish/RedFish/orders/domain/model/` | Modelos de clientes, pedidos y detalles. | PASS |
| `RedFish/src/main/java/com/RedFish/RedFish/dispatches/domain/model/` | Modelo de despachos. | PASS |
| `RedFish/src/main/java/com/RedFish/RedFish/vehicles/domain/model/` | Modelo de vehiculos. | PASS |
| `RedFish/src/main/java/com/RedFish/RedFish/shared/domain/DomainValidation.java` | Validaciones compartidas del dominio. | PASS |
| `RedFish/src/test/java/com/RedFish/RedFish/modules/domain/DomainModelTests.java` | Pruebas unitarias del dominio. | PASS |
| `docs/Week-03/session-01/Week 3 - Session 1.md` | Documento principal de la sesion. | PASS |
| `docs/domain/domain-model.md` | Resumen del modelo de dominio. | PASS |

---

## 3. Criterios de aceptacion

| ID | Criterio | Resultado | Evidencia |
|---|---|---|---|
| CA-01 | Se crean entidades iniciales para los bounded contexts principales. | PASS | Modelos creados en `domain/model`. |
| CA-02 | Las entidades se ubican dentro de sus modulos de dominio. | PASS | Estructura por modulos: `security`, `production`, `inventory`, `orders`, `dispatches`, `vehicles`. |
| CA-03 | El modelo evita depender de JPA, controladores o infraestructura. | PASS | Las clases de dominio no usan anotaciones JPA ni Spring MVC. |
| CA-04 | Se implementan invariantes iniciales del negocio. | PASS | Validaciones en constructores y metodos de dominio. |
| CA-05 | Se agregan pruebas unitarias para reglas criticas. | PASS | `DomainModelTests`. |
| CA-06 | El modelo respeta la arquitectura de monolito modular. | PASS | Las entidades viven dentro del modulo correspondiente. |
| CA-07 | Se documenta que queda pendiente la persistencia y exposicion HTTP. | PASS | Documento de Week 3 Sesion 1. |

---

## 4. Validacion de reglas de negocio

| Regla validada | Resultado | Evidencia |
|---|---|---|
| El inventario no puede quedar con stock negativo. | PASS | Prueba `inventoryCannotBecomeNegative`. |
| Un pedido calcula su total desde sus items. | PASS | Prueba `orderCalculatesTotalFromItems`. |
| Un estanque inactivo no puede recibir alimentacion. | PASS | Prueba `inactivePondCannotReceiveFeedingRecord`. |
| Un vehiculo inactivo no puede asignarse a despacho. | PASS | Prueba `inactiveVehicleCannotBeAssignedToDispatch`. |

---

## 5. Ejecucion de pruebas

Comando ejecutado:

```bash
./gradlew.bat test
```

Resultado:

```text
BUILD SUCCESSFUL
```

Observacion:

```text
RedFishApplicationTests.contextLoads() esta desactivada temporalmente porque
requiere Docker/Testcontainers. Esta validacion corresponde a infraestructura y
sera retomada en una historia posterior.
```

---

## 6. Hallazgos

No se identifican defectos bloqueantes en el modelo de dominio inicial.

El modelo cumple el alcance de `HU-005`. Quedan pendientes persistencia,
repositorios, servicios de aplicacion, controladores REST, DTOs, migraciones y
validacion completa de infraestructura.

---

## 7. Resultado final

```text
QA STATUS: PASS
```

La entrega `HU-005` cumple los criterios de aceptacion definidos para Week 3
Sesion 1 y puede continuar el flujo de integracion del proyecto.
