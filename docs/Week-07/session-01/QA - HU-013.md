# QA - HU-013

## Validacion de comunicacion entre modulos e idempotencia

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 7  
**Sesion:** 1  
**Historia de usuario:** HU-013  
**Rama de desarrollo:** `hu-013-dev`  
**Rama de QA:** `hu-013-qa`

---

## 1. Objetivo de QA

Validar que RedFish implemente una comunicacion dirigida por eventos entre los
modulos Inventario y Reportes, que el consumidor tolere la entrega repetida de
un mismo evento y que las bases de Develop, QA y produccion local puedan
inspeccionarse mediante puertos independientes.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Inventario | Publicacion de `ProductCreatedEvent` despues de crear un producto. |
| Contrato | Evento con UUID, producto, codigo y fecha de ocurrencia. |
| Reportes | Consumo sin acceso directo a la tabla `productos`. |
| Idempotencia | Registro unico por `eventId`. |
| Transaccion | Creacion y consumo sincrono dentro de una transaccion local. |
| Persistencia | Tabla `eventos_producto_procesados`. |
| Docker | Puertos MySQL distintos para los tres ambientes. |
| Seguridad | Archivos reales de entorno excluidos de Git. |
| Regresion | Pruebas existentes, salud y documentacion anterior. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | Se documentan interacciones sincronas y asincronas. | PASS | `communication-strategy.md` diferencia REST, puertos, eventos locales y mensajeria futura. |
| CA-02 | REST se usa externamente y los puertos Java entre modulos. | PASS | Controladores REST y puertos de aplicacion permanecen separados. |
| CA-03 | Inventario publica un evento al crear un producto. | PASS | `CreateProductService` publica `ProductCreatedEvent`. |
| CA-04 | Reportes consume el evento sin usar el repositorio de Inventario. | PASS | `ProductCreatedEventConsumer` depende de `ProcessedProductEventPort`. |
| CA-05 | El evento tiene una clave de idempotencia. | PASS | `eventId` es un UUID obligatorio. |
| CA-06 | El consumidor evita registrar dos veces el mismo evento. | PASS | Llave primaria e `INSERT IGNORE` atomico. |
| CA-07 | Existen pruebas de publicacion e idempotencia. | PASS | `ProductServiceTests` y `ProductCreatedEventConsumerTests`. |
| CA-08 | Cada ambiente publica MySQL en un puerto distinto. | PASS | `3307`, `3308` y `3309`. |
| CA-09 | Configuracion y restricciones quedan documentadas. | PASS | Matriz de ambientes, ejemplos `.env` y documento de sesion. |

---

## 4. Casos de prueba

### CP-001 - Ejecutar pruebas automatizadas

Comando:

```powershell
.\gradlew.bat test
```

Resultado:

```text
BUILD SUCCESSFUL
4 actionable tasks: 4 up-to-date
```

Estado: PASS.

---

### CP-002 - Verificar publicacion del evento

La prueba de aplicacion crea un producto y captura el evento publicado.

Comprobaciones:

- el evento existe;
- `productId` coincide con el producto guardado;
- `productCode` conserva el codigo de negocio;
- `eventId` es unico;
- `occurredAt` esta informado.

Estado: PASS.

---

### CP-003 - Verificar consumo idempotente

Se entrega dos veces la misma instancia de `ProductCreatedEvent` al consumidor.

Resultado esperado y obtenido:

```text
Entregas recibidas: 2
Eventos registrados: 1
```

El registro persistente utiliza `event_id` como llave primaria y el adaptador
ejecuta `INSERT IGNORE`, por lo que tambien evita duplicados concurrentes en
MySQL.

Estado: PASS.

---

### CP-004 - Probar el flujo en el ambiente QA

Peticion ejecutada:

```http
POST http://localhost:8081/api/products
Content-Type: application/json
```

Datos de evidencia:

```text
productId: 1
productCode: QA013-152252
eventId: 23f7a211-61a3-4d5a-83d8-08f378ebe926
```

Se consulto `eventos_producto_procesados` dentro de
`redfish-qa-mysql-1` y se encontro un unico registro asociado al producto.

Estado: PASS.

---

### CP-005 - Validar configuraciones Compose

Comandos:

```powershell
docker compose --env-file .env -f compose.yaml -f compose.override.yaml config --quiet
docker compose --env-file .env.qa -f compose.yaml -f compose.qa.yaml config --quiet
docker compose --env-file .env.prod -f compose.yaml -f compose.prod.yaml config --quiet
```

Las tres configuraciones finalizaron sin errores.

Estado: PASS.

---

### CP-006 - Verificar puertos publicados

| Ambiente | API | MySQL | Estado |
|---|---:|---:|---|
| Develop | `8080` | `3307` | `healthy` |
| QA | `8081` | `3308` | `healthy` |
| Produccion local | `8082` | `3309` | `healthy` |

Los puertos internos de MySQL permanecen en `3306` y no presentan conflicto
porque cada ambiente utiliza una red Docker independiente.

Estado: PASS.

---

### CP-007 - Verificar promocion de la imagen

Los tres contenedores de aplicacion ejecutan:

```text
sha256:81846e8d0dcded7882b4216318d8ec76c89dbda1c675c16e8b26012580774598
```

Estado: PASS.

---

### CP-008 - Verificar secretos y archivos generados

Archivos locales excluidos del repositorio:

```text
RedFish/.env
RedFish/.env.qa
RedFish/.env.prod
RedFish/.gradle/
RedFish/bin/
RedFish/build/
```

Los archivos `.env.example`, `.env.qa.example` y `.env.prod.example` contienen
marcadores `change_me` y pueden versionarse de forma segura.

Estado: PASS.

---

## 5. Revision de arquitectura

La implementacion conserva el monolito modular:

- Inventario define y publica el contrato del evento.
- Reportes consume el contrato publico, no clases de persistencia de Inventario.
- Spring funciona como adaptador de entrega dentro del proceso.
- MySQL conserva la deduplicacion despues de reinicios.
- No se introduce gRPC ni un broker sin una necesidad operativa actual.

`@EventListener` realiza entrega sincrona local. Esto permite compartir la
transaccion, pero no equivale a mensajeria asincrona durable. La documentacion
fue corregida para expresar esta diferencia.

---

## 6. Defectos encontrados y corregidos

| ID | Severidad | Descripcion | Correccion | Estado |
|---|---|---|---|---|
| DEF-001 | Media | Al copiar Develop se regresaron los documentos de HU-011 y HU-012 de QA aprobada a pendiente. | Se restauro el contenido aprobado existente en `Qa`. | Corregido |
| DEF-002 | Baja | La estrategia describia el evento Spring como asincrono, aunque `@EventListener` se ejecuta de forma sincrona. | Se documento como entrega sincrona local dirigida por eventos. | Corregido |
| DEF-003 | Baja | La documentacion indicaba captura de violacion de unicidad, pero la implementacion usa `INSERT IGNORE`. | Se alineo la descripcion con el codigo. | Corregido |

No se identificaron defectos bloqueantes despues de las correcciones.

---

## 7. Limitaciones conocidas

| ID | Limitacion | Tratamiento futuro |
|---|---|---|
| OBS-01 | Los eventos locales no sobreviven a la caida del proceso. | Evaluar Outbox y RabbitMQ o Kafka cuando se requiera entrega durable. |
| OBS-02 | No existen reintentos automaticos para eventos. | Incorporarlos junto con la infraestructura de mensajeria. |
| OBS-03 | Produccion usa `ddl-auto=validate` y requiere aprovisionamiento previo. | Incorporar Flyway o Liquibase en una HU posterior. |
| OBS-04 | El puerto `3309` se expone para validacion local. | Mantener MySQL sin puerto publico en produccion real. |

Estas observaciones no bloquean el alcance de HU-013.

---

## 8. Evidencias finales

| Evidencia | Resultado |
|---|---|
| Suite Gradle | PASS |
| Publicacion de evento | PASS |
| Idempotencia | PASS |
| Flujo REST en QA | PASS |
| Persistencia en MySQL QA | PASS |
| Compose Develop | PASS |
| Compose QA | PASS |
| Compose produccion local | PASS |
| Seis contenedores saludables | PASS |
| Misma imagen promovida | PASS |
| Secretos fuera de Git | PASS |
| Regresiones documentales corregidas | PASS |

---

## 9. Resultado final

QA considera que HU-013 cumple todos sus criterios de aceptacion. La
comunicacion entre Inventario y Reportes permanece desacoplada mediante un
contrato de evento, el consumidor evita procesamiento duplicado y los tres
ambientes publican sus bases de datos mediante puertos locales diferentes.

La historia queda aprobada para continuar el flujo:

```text
hu-013-dev -> Develop -> hu-013-qa -> Qa
```

El Pull Request desde `hu-013-qa` hacia `Qa` puede realizarse.
