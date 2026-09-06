# Week 5 - Sesion 1

## Contenerizacion de la aplicacion y base de datos

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 5  
**Sesion:** 1  
**Historia de usuario:** HU-009

---

## 1. Objetivo de la sesion

Contenerizar el backend RedFish y la base de datos MySQL para disponer de un
entorno local reproducible mediante Docker Compose.

Esta historia tambien incorpora `Cliente` como segunda entidad persistida, con
el fin de validar que el monolito modular puede manejar mas de un recurso de
negocio conectado a la misma base de datos.

---

## 2. Historia de usuario

### HU-009 - Contenerizar la aplicacion y la base de datos

**Como** responsable del desarrollo de RedFish,  
**quiero** ejecutar el backend y la base de datos mediante Docker Compose,  
**para** contar con un entorno local reproducible usando Spring Boot y MySQL.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se define un contenedor para MySQL. |
| CA-02 | Se define un contenedor para la aplicacion Spring Boot. |
| CA-03 | La aplicacion se conecta a MySQL usando variables de entorno. |
| CA-04 | Docker Compose permite levantar backend y base de datos juntos. |
| CA-05 | Se implementa `Cliente` como segunda entidad persistida. |
| CA-06 | Se exponen endpoints `POST` y `GET` para clientes. |
| CA-07 | Se valida que productos y clientes funcionan como recursos independientes. |
| CA-08 | Se corrige la documentacion que mencionaba PostgreSQL. |
| CA-09 | Las pruebas automatizadas finalizan correctamente. |

---

## 3. Decision sobre PostgREST y PostgreSQL

La guia mencionaba un entorno basado en PostgreSQL/PostgREST. En RedFish esa
referencia se adapta porque el stack definido para el proyecto es:

```text
Spring Boot + MySQL
```

PostgREST no se incorpora porque esta orientado a PostgreSQL y no corresponde a
la arquitectura seleccionada para RedFish. El contrato REST se expone desde
controladores propios de Spring Boot.

---

## 4. Contenerizacion

Se agregan los siguientes archivos:

```text
RedFish/Dockerfile
RedFish/.dockerignore
RedFish/compose.yaml
```

El archivo `compose.yaml` mantiene dos modos de uso:

| Modo | Comando | Proposito |
|---|---|---|
| Solo base de datos | `docker compose up -d mysql` | Desarrollo local con `./gradlew.bat bootRun`. |
| Backend y base de datos | `docker compose --profile container up --build` | Ejecucion completa en Docker. |

El servicio de aplicacion usa variables de entorno para conectarse a MySQL
dentro de la red de Docker:

```text
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/redfish
SPRING_DATASOURCE_USERNAME=myuser
SPRING_DATASOURCE_PASSWORD=secret
```

Para ejecucion local, la aplicacion mantiene valores por defecto:

```text
jdbc:mysql://localhost:3307/redfish
```

---

## 5. Segunda entidad persistida

Se implementa `Cliente` dentro del modulo `orders`.

Tabla esperada en MySQL:

```text
clientes
```

Endpoints:

| Metodo | Ruta | Proposito |
|---|---|---|
| `POST` | `/api/customers` | Crear cliente. |
| `GET` | `/api/customers` | Listar clientes. |
| `GET` | `/api/customers/{id}` | Consultar cliente por identificador. |

Ejemplo de creacion:

```http
POST /api/customers
Content-Type: application/json
```

```json
{
  "name": "Restaurante El Lago",
  "phone": "3001234567",
  "address": "Calle 10 # 15-20",
  "email": "compras@ellago.com"
}
```

---

## 6. Relacion entre clientes y productos

En esta HU, `Cliente` y `Producto` funcionan como recursos independientes:

```text
/api/products  -> tabla productos
/api/customers -> tabla clientes
```

No se crea una relacion directa entre clientes y productos porque esa asociacion
pertenece al flujo de pedidos. La relacion esperada para una historia posterior
sera:

```text
Cliente -> Pedido -> DetallePedido -> Producto
```

---

## 7. Ejecucion

### Ejecutar solo MySQL

```powershell
cd D:\descargas\pago\RedFish\RedFish
docker compose up -d mysql
.\gradlew.bat bootRun
```

### Ejecutar backend y MySQL con Docker

```powershell
cd D:\descargas\pago\RedFish\RedFish
docker compose --profile container up --build
```

---

## 8. Validacion esperada

Pruebas automatizadas:

```powershell
.\gradlew.bat test
```

Resultado esperado:

```text
BUILD SUCCESSFUL
```

Validacion manual:

```text
POST http://localhost:8080/api/products
GET  http://localhost:8080/api/products
POST http://localhost:8080/api/customers
GET  http://localhost:8080/api/customers
```

En MySQL Workbench deben visualizarse las tablas:

```text
productos
clientes
```

---

## 9. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| Dockerfile del backend | Implementado |
| Docker Compose con MySQL | Implementado |
| Docker Compose con backend | Implementado con perfil `container` |
| Variables de entorno para datasource | Implementadas |
| Entidad `Cliente` persistida | Implementada |
| Endpoints de clientes | Implementados |
| Documentacion PostgreSQL/PostgREST | Corregida hacia MySQL/Spring Boot |
| Pruebas automatizadas | Pendientes de ejecucion |
| Validacion QA | Pendiente en rama `hu-009-qa` |

---

## 10. Siguiente paso

Ejecutar pruebas automatizadas, validar la ejecucion completa con Docker y
promover `HU-009` al ambiente QA mediante la rama `hu-009-qa`.
