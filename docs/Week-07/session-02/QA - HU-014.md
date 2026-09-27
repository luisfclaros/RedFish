# QA - HU-014

## Validacion de contratos versionados y pruebas de contrato

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 7  
**Sesion:** 2  
**Historia de usuario:** HU-014  
**Rama de desarrollo:** `hu-014-dev`  
**Rama de QA:** `hu-014-qa`

---

## 1. Objetivo de QA

Validar que RedFish publique contratos REST versionados, mantenga compatibilidad
temporal con los consumidores de las rutas anteriores, entregue errores con una
estructura uniforme y verifique mediante Pact que el proveedor conserva el
contrato esperado por el consumidor.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Versionado | Rutas canonicas bajo `/api/v1`. |
| Compatibilidad | Alias temporales bajo `/api`. |
| Deprecacion | Encabezados `Deprecation`, `Sunset` y `Link`. |
| OpenAPI | Contrato 3.1 legible y publicado como YAML. |
| Swagger UI | Interfaz configurada con el contrato versionado. |
| Errores | Sobre estandar y correlacion mediante `X-Trace-Id`. |
| Pact | Contrato del consumidor y verificacion del proveedor. |
| CI | Ejecucion de pruebas de contrato mediante GitHub Actions. |
| Regresion | Suite existente, documentacion y ambiente QA. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | Los endpoints soportados se publican bajo `/api/v1`. | PASS | Productos y clientes exponen rutas versionadas. |
| CA-02 | Las rutas originales permanecen disponibles. | PASS | `/api/products` y `/api/customers` responden como alias. |
| CA-03 | Las rutas antiguas anuncian su retiro. | PASS | Se verificaron `Deprecation`, `Sunset` y `Link`. |
| CA-04 | Existe un contrato OpenAPI versionado. | PASS | `openapi-v1.yaml`, OpenAPI 3.1.0 y version 1.0.0. |
| CA-05 | El contrato declara operaciones, solicitudes, respuestas y errores. | PASS | Esquemas y respuestas reutilizables definidos en OpenAPI. |
| CA-06 | Los errores usan un sobre estandar. | PASS | Respuesta con `code`, `message`, `details` y `trace_id`. |
| CA-07 | Se documentan reglas de compatibilidad. | PASS | `docs/api/versioning-policy.md`. |
| CA-08 | Existe una prueba Pact del consumidor. | PASS | `ProductApiConsumerPactTests`. |
| CA-09 | El proveedor verifica el pacto. | PASS | `ProductApiProviderPactTests` prueba el controlador real. |
| CA-10 | Las pruebas de contrato estan configuradas en CI. | PASS | `.github/workflows/contract-tests.yml`. |
| CA-11 | Las pruebas existentes permanecen aprobadas. | PASS | 27 pruebas, 0 fallos y 0 errores. |

---

## 4. Casos de prueba

### CP-001 - Ejecutar la suite completa

Comando:

```powershell
.\gradlew.bat clean test --no-daemon
```

Resultado:

```text
BUILD SUCCESSFUL
27 tests, 0 failures, 0 errors, 1 skipped
```

La prueba omitida corresponde al contexto completo con Testcontainers y ya se
encontraba deshabilitada antes de HU-014.

Estado: PASS.

---

### CP-002 - Consultar el contrato OpenAPI en QA

Peticion:

```http
GET http://localhost:8081/openapi-v1.yaml
```

Resultado:

```text
HTTP 200
Content-Type: application/yaml
OpenAPI: 3.1.0
Version: 1.0.0
```

Estado: PASS.

---

### CP-003 - Consultar Swagger UI

Peticion:

```http
GET http://localhost:8081/swagger-ui.html
```

Resultado: `HTTP 200`.

Estado: PASS.

---

### CP-004 - Crear un producto mediante la API v1

Peticion:

```http
POST http://localhost:8081/api/v1/products
Content-Type: application/json
```

Cuerpo utilizado:

```json
{
  "code": "QA014-205046",
  "name": "Tilapia QA Contract",
  "unitOfMeasure": "kg",
  "type": "PRODUCT"
}
```

Resultado:

```text
HTTP 201
Location: /api/v1/products/2
```

Estado: PASS.

---

### CP-005 - Verificar compatibilidad y deprecacion

Peticion:

```http
GET http://localhost:8081/api/products
```

Encabezados obtenidos:

```text
Deprecation: true
Sunset: Wed, 30 Jun 2027 23:59:59 GMT
Link: </api/v1/products>; rel="successor-version"
```

La ruta anterior continuo respondiendo con `HTTP 200`.

Estado: PASS.

---

### CP-006 - Validar el sobre de errores

Se envio una solicitud con campos obligatorios vacios a
`POST /api/v1/products`.

Resultado:

```text
HTTP 400
error.code: VALIDATION_ERROR
X-Trace-Id: 0e359a6c-3cb0-40f6-a4a4-b91c1bed256b
error.trace_id: 0e359a6c-3cb0-40f6-a4a4-b91c1bed256b
```

El identificador de la cabecera coincide con el incluido en el cuerpo.
Adicionalmente, un identificador de producto no numerico respondio `HTTP 400`.

Estado: PASS.

---

### CP-007 - Verificar los contratos Pact

La suite ejecuto:

- `ProductApiConsumerPactTests`, que genera el pacto V4 esperado por
  `redfish-web-client`;
- `ProductApiProviderPactTests`, que reproduce ese pacto contra
  `ProductController`.

El pacto versionado permanece en:

```text
RedFish/src/test/resources/pacts/redfish-web-client-redfish-api.json
```

Estado: PASS.

---

### CP-008 - Revisar la integracion continua

El workflow `.github/workflows/contract-tests.yml` se activa para Pull Requests
y envios hacia `Develop`, `Qa` y `main`, y ejecuta:

```bash
./gradlew test --no-daemon
```

La revision local confirma que el workflow y la suite estan incluidos. Su
ejecucion remota se producira al publicar el Pull Request.

Estado: PASS.

---

### CP-009 - Validar el ambiente QA

Contenedores revisados:

| Contenedor | Estado |
|---|---|
| `redfish-qa-app-1` | `healthy` |
| `redfish-qa-mysql-1` | `healthy` |

La API respondio `UP` en `http://localhost:8081/actuator/health`.

La aplicacion de QA utilizo la imagen promovida sin reconstruccion:

```text
sha256:44b434d3527eb63c782d19656931f268b43bde30835d4074eec832950caa19bd
```

Estado: PASS.

---

### CP-010 - Verificar secretos y archivos generados

Los siguientes elementos locales permanecen excluidos de Git:

```text
RedFish/.env
RedFish/.env.qa
RedFish/.env.prod
RedFish/.gradle/
RedFish/bin/
RedFish/build/
```

Estado: PASS.

---

## 5. Defectos encontrados y corregidos

| ID | Severidad | Descripcion | Correccion | Estado |
|---|---|---|---|---|
| DEF-001 | Media | Al copiar desde Develop se regresaron documentos aprobados de HU-011, HU-012 y HU-013 a estados pendientes. | Se conservaron las evidencias aprobadas existentes en `Qa`. | Corregido |
| DEF-002 | Baja | El README recibido no incluia las rutas de API v1 ni los enlaces al contrato. | Se restauro la seccion de API REST v1. | Corregido |

No se identificaron defectos funcionales bloqueantes.

---

## 6. Limitaciones conocidas

| ID | Limitacion | Tratamiento futuro |
|---|---|---|
| OBS-01 | El pacto se versiona en Git y no se publica en un Pact Broker. | Incorporar un broker cuando existan varios consumidores o despliegues independientes. |
| OBS-02 | Las rutas antiguas permanecen activas hasta la fecha de retiro. | Migrar consumidores a `/api/v1` antes del `Sunset`. |
| OBS-03 | Una prueba de contexto completo con Testcontainers esta deshabilitada desde historias anteriores. | Reactivarla cuando el entorno automatizado garantice Docker. |

Estas observaciones no bloquean el alcance de HU-014.

---

## 7. Evidencias finales

| Evidencia | Resultado |
|---|---|
| Suite Gradle limpia | PASS |
| Contrato OpenAPI publicado | PASS |
| Swagger UI | PASS |
| Rutas `/api/v1` | PASS |
| Compatibilidad de rutas anteriores | PASS |
| Encabezados de deprecacion | PASS |
| Sobre estandar de errores | PASS |
| Correlacion de trazas | PASS |
| Contrato Pact del consumidor | PASS |
| Verificacion Pact del proveedor | PASS |
| Workflow de contratos | PASS |
| Aplicacion y MySQL de QA | `healthy` |
| Secretos y artefactos fuera de Git | PASS |

---

## 8. Resultado final

QA considera que HU-014 cumple todos sus criterios de aceptacion. La API cuenta
con una version estable, conserva compatibilidad temporal con las rutas
anteriores, publica un contrato OpenAPI verificable y protege la integracion
del consumidor mediante Pact.

La historia queda aprobada para continuar el flujo:

```text
hu-014-dev -> Develop -> hu-014-qa -> Qa
```

El Pull Request desde `hu-014-qa` hacia `Qa` puede realizarse.
