# Week 4 - Sesion 1

## Walking skeleton inicial del backend RedFish

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 4  
**Sesion:** 1  
**Historia de usuario:** HU-007

---

## 1. Objetivo de la sesion

Construir el primer flujo ejecutable del backend RedFish usando Spring Boot y la
arquitectura de monolito modular definida en semanas anteriores.

El walking skeleton se implementa sobre el modulo Inventario con endpoints REST
minimos para crear, listar y consultar productos.

---

## 2. Historia de usuario

### HU-007 - Construir el walking skeleton

**Como** responsable del desarrollo de RedFish,  
**quiero** construir un flujo minimo ejecutable del backend,  
**para** validar que la estructura modular puede recibir una peticion HTTP,
ejecutar un caso de uso, aplicar reglas de dominio y devolver una respuesta.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se crea un endpoint REST funcional para crear productos. |
| CA-02 | Se crea un endpoint REST funcional para listar productos. |
| CA-03 | Se crea un endpoint REST funcional para consultar producto por ID. |
| CA-04 | El flujo pasa por controlador, servicio de aplicacion, dominio y repositorio. |
| CA-05 | La persistencia temporal se implementa en memoria. |
| CA-06 | Se mantiene separacion entre interfaces, aplicacion, dominio e infraestructura. |
| CA-07 | Se agregan pruebas para servicios y controlador. |
| CA-08 | La suite de pruebas finaliza correctamente. |

---

## 3. Endpoints implementados

| Metodo | Ruta | Proposito |
|---|---|---|
| `POST` | `/api/products` | Crear un producto. |
| `GET` | `/api/products` | Listar productos creados en memoria. |
| `GET` | `/api/products/{id}` | Consultar un producto por identificador. |

Ejemplo de creacion:

```http
POST /api/products
Content-Type: application/json

{
  "code": "PROD-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT"
}
```

Respuesta esperada:

```http
HTTP/1.1 201 Created
Location: /api/products/1
```

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

---

## 4. Flujo implementado

```text
HTTP Request
    |
    v
ProductController
    |
    v
CreateProductService / ListProductsService / GetProductService
    |
    v
ProductRepositoryPort
    |
    v
InMemoryProductRepository
    |
    v
Product
```

Este flujo valida el camino principal de la arquitectura sin incorporar todavia
JPA ni MySQL.

---

## 5. Archivos principales

| Capa | Archivo / ruta |
|---|---|
| Dominio | `RedFish/src/main/java/com/RedFish/RedFish/inventory/domain/model/Product.java` |
| Puerto | `RedFish/src/main/java/com/RedFish/RedFish/inventory/application/port/ProductRepositoryPort.java` |
| Aplicacion | `RedFish/src/main/java/com/RedFish/RedFish/inventory/application/service/` |
| Infraestructura | `RedFish/src/main/java/com/RedFish/RedFish/inventory/infrastructure/persistence/inmemory/InMemoryProductRepository.java` |
| Interfaces | `RedFish/src/main/java/com/RedFish/RedFish/inventory/interfaces/rest/` |
| Manejo de errores | `RedFish/src/main/java/com/RedFish/RedFish/shared/interfaces/rest/RestExceptionHandler.java` |
| Seguridad temporal | `RedFish/src/main/java/com/RedFish/RedFish/shared/config/SecurityConfiguration.java` |

---

## 6. Pruebas implementadas

| Prueba | Proposito |
|---|---|
| `ProductServiceTests` | Validar servicios de aplicacion y repositorio en memoria. |
| `ProductControllerTests` | Validar peticiones HTTP sobre el controlador de productos. |

Comando ejecutado:

```bash
./gradlew.bat test
```

Resultado:

```text
BUILD SUCCESSFUL
```

---

## 7. Pendientes

Esta historia no implementa:

- persistencia MySQL;
- repositorios JPA;
- migraciones;
- autenticacion real;
- coleccion Postman formal;
- CRUD completo de productos;
- endpoints de otros modulos.

Estos elementos quedan para historias posteriores, especialmente `HU-008` para
contrato inicial de API y pruebas manuales de peticiones.

---

## 8. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| Endpoint `POST /api/products` | Implementado |
| Endpoint `GET /api/products` | Implementado |
| Endpoint `GET /api/products/{id}` | Implementado |
| Repositorio en memoria | Implementado |
| Pruebas automatizadas | Implementadas y aprobadas |
| Validacion QA | Pendiente en rama `hu-007-qa` |

---

## 9. Siguiente paso

Promover `HU-007` al ambiente QA mediante `hu-007-qa`, validar el walking
skeleton y continuar con `HU-008`, donde se documentara el contrato inicial de
API y se prepararan pruebas de peticiones tipo Postman.
