# Contrato REST v1 de RedFish

## Base URL

```text
Develop:          http://localhost:8080/api/v1
QA:               http://localhost:8081/api/v1
Produccion local: http://localhost:8082/api/v1
```

La fuente de verdad legible por herramientas es
`RedFish/src/main/resources/static/openapi-v1.yaml`. Las reglas de evolucion se
encuentran en `docs/api/versioning-policy.md`.

---

## Productos

### Crear producto

```http
POST /api/v1/products
Content-Type: application/json
```

Request:

```json
{
  "code": "PROD-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT"
}
```

Response `201 Created`:

```json
{
  "id": 1,
  "code": "PROD-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT",
  "active": true
}
```

Headers:

```text
Location: /api/v1/products/1
```

Errores:

| Status | Causa |
|---|---|
| `400 Bad Request` | Cuerpo invalido o codigo duplicado. |

---

### Listar productos

```http
GET /api/v1/products
```

Response `200 OK`:

```json
[
  {
    "id": 1,
    "code": "PROD-001",
    "name": "Tilapia Roja",
    "unitOfMeasure": "kg",
    "type": "PRODUCT",
    "active": true
  }
]
```

---

### Consultar producto por ID

```http
GET /api/v1/products/1
```

Response `200 OK`:

```json
{
  "id": 1,
  "code": "PROD-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT",
  "active": true
}
```

Errores:

| Status | Causa |
|---|---|
| `400 Bad Request` | Producto no encontrado. |

---

## Tipos permitidos

```text
PRODUCT
INPUT
```

---

## Persistencia

Los productos se almacenan en MySQL usando la tabla:

```text
productos
```

La tabla se genera automaticamente durante desarrollo mediante Hibernate
`ddl-auto: update`.

---

## Clientes

### Crear cliente

```http
POST /api/v1/customers
Content-Type: application/json
```

Request:

```json
{
  "name": "Restaurante El Lago",
  "phone": "3001234567",
  "address": "Calle 10 # 15-20",
  "email": "compras@ellago.com"
}
```

Response `201 Created`:

```json
{
  "id": 1,
  "name": "Restaurante El Lago",
  "phone": "3001234567",
  "address": "Calle 10 # 15-20",
  "email": "compras@ellago.com",
  "active": true
}
```

Headers:

```text
Location: /api/v1/customers/1
```

Errores:

| Status | Causa |
|---|---|
| `400 Bad Request` | Cuerpo invalido o correo duplicado. |

---

### Listar clientes

```http
GET /api/v1/customers
```

Response `200 OK`:

```json
[
  {
    "id": 1,
    "name": "Restaurante El Lago",
    "phone": "3001234567",
    "address": "Calle 10 # 15-20",
    "email": "compras@ellago.com",
    "active": true
  }
]
```

---

### Consultar cliente por ID

```http
GET /api/v1/customers/1
```

Response `200 OK`:

```json
{
  "id": 1,
  "name": "Restaurante El Lago",
  "phone": "3001234567",
  "address": "Calle 10 # 15-20",
  "email": "compras@ellago.com",
  "active": true
}
```

Errores:

| Status | Causa |
|---|---|
| `400 Bad Request` | Cliente no encontrado. |

---

## Persistencia de clientes

Los clientes se almacenan en MySQL usando la tabla:

```text
clientes
```

---

## Sobre estandar de error

Todos los errores REST utilizan esta estructura:

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "invalid request body",
    "details": {
      "code": "must not be blank"
    },
    "trace_id": "9e289660-5f90-4c65-9f8f-939c91128295"
  }
}
```

La respuesta tambien incluye `X-Trace-Id` con el mismo identificador.

---

## Rutas deprecadas

Las rutas `/api/products` y `/api/customers` continuan disponibles durante la
migracion, pero incluyen los encabezados `Deprecation`, `Sunset` y `Link`. No
deben utilizarse en integraciones nuevas.
