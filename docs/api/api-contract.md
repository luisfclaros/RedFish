# Contrato inicial de API de RedFish

## Base URL

```text
http://localhost:8080
```

---

## Productos

### Crear producto

```http
POST /api/products
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
Location: /api/products/1
```

Errores:

| Status | Causa |
|---|---|
| `400 Bad Request` | Cuerpo invalido o codigo duplicado. |

---

### Listar productos

```http
GET /api/products
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
GET /api/products/1
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
