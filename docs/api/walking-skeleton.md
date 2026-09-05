# Walking skeleton de RedFish

## Proposito

Este documento resume el primer flujo ejecutable del backend RedFish.

El objetivo no es construir el sistema completo, sino verificar que una peticion
HTTP pueda atravesar las capas principales del monolito modular.

---

## Modulo seleccionado

El walking skeleton se implementa en el modulo Inventario porque este contexto
es central para pedidos, produccion y despachos.

---

## Endpoints disponibles

### Crear producto

```http
POST /api/products
Content-Type: application/json
```

```json
{
  "code": "PROD-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT"
}
```

### Listar productos

```http
GET /api/products
```

### Consultar producto por ID

```http
GET /api/products/1
```

---

## Capas involucradas

| Capa | Elemento |
|---|---|
| Interfaces | `ProductController`, `ProductRequest`, `ProductResponse` |
| Aplicacion | `CreateProductService`, `ListProductsService`, `GetProductService` |
| Puerto | `ProductRepositoryPort` |
| Infraestructura | `InMemoryProductRepository` |
| Dominio | `Product`, `ProductType` |

---

## Validaciones

- El codigo del producto es obligatorio.
- El nombre es obligatorio.
- La unidad de medida es obligatoria.
- El tipo de producto es obligatorio.
- No se permiten codigos de producto duplicados en memoria.

---

## Limitaciones actuales

- Los datos se pierden al reiniciar la aplicacion.
- No existe persistencia MySQL todavia.
- No se implementa autenticacion real.
- No se documenta aun una coleccion Postman formal.
- Los endpoints pertenecen solo al modulo Inventario.
