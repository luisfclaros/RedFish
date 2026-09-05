# Week 4 - Sesion 2

## Contrato inicial de API con MySQL

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 4  
**Sesion:** 2  
**Historia de usuario:** HU-008

---

## 1. Objetivo de la sesion

Definir y validar el contrato inicial de API del MVP usando endpoints reales del
modulo Inventario, conectados a MySQL mediante Docker Compose.

Esta sesion evoluciona el walking skeleton de `HU-007`: los productos dejan de
guardarse solo en memoria y pasan a persistirse en la base de datos `redfish`.

---

## 2. Historia de usuario

### HU-008 - Definir el contrato inicial de la API del MVP

**Como** responsable del desarrollo de RedFish,  
**quiero** definir y validar peticiones HTTP iniciales del MVP,  
**para** probar la API con herramientas como Postman y confirmar persistencia
real en MySQL.

### Criterios de aceptacion

| ID | Criterio |
|---|---|
| CA-01 | Se configura MySQL mediante Docker Compose. |
| CA-02 | Se configura Spring Boot para conectarse a la base de datos `redfish`. |
| CA-03 | Se crea mapeo JPA para productos. |
| CA-04 | Se crea repositorio JPA que implementa el puerto de productos. |
| CA-05 | Se documentan endpoints `POST` y `GET` del modulo Inventario. |
| CA-06 | Se crea coleccion Postman inicial. |
| CA-07 | Las pruebas automatizadas finalizan correctamente. |

---

## 3. Configuracion de MySQL con Docker

Archivo:

```text
RedFish/compose.yaml
```

Configuracion principal:

```text
image: mysql:8.4
database: redfish
user: myuser
password: secret
port: 3306
```

Spring Boot usa Docker Compose Support para levantar el servicio de MySQL cuando
se ejecuta:

```bash
./gradlew.bat bootRun
```

Docker Desktop debe estar iniciado antes de ejecutar la aplicacion.

---

## 4. Persistencia JPA

Se agregan componentes de infraestructura para productos:

```text
ProductJpaEntity
SpringDataProductJpaRepository
JpaProductRepository
```

`JpaProductRepository` implementa `ProductRepositoryPort`, por lo que los
servicios de aplicacion no dependen directamente de Spring Data JPA.

---

## 5. Contrato inicial de API

Los endpoints documentados son:

| Metodo | Ruta | Proposito |
|---|---|---|
| `POST` | `/api/products` | Crear producto. |
| `GET` | `/api/products` | Listar productos persistidos. |
| `GET` | `/api/products/{id}` | Consultar producto por identificador. |

El detalle del contrato se encuentra en:

```text
docs/api/api-contract.md
```

La coleccion Postman se encuentra en:

```text
postman/RedFish.postman_collection.json
```

---

## 6. Ejecucion esperada

1. Abrir Docker Desktop.
2. Ejecutar el backend:

```bash
cd RedFish
./gradlew.bat bootRun
```

3. Probar las peticiones en Postman usando:

```text
http://localhost:8080
```

---

## 7. Validacion realizada

Pruebas automatizadas:

```bash
./gradlew.bat test
```

Resultado:

```text
BUILD SUCCESSFUL
```

Validacion runtime con Docker:

```text
Pendiente de ejecutar cuando Docker Desktop tenga el daemon activo.
```

Durante la preparacion de la HU se intento consultar Docker, pero el daemon no
estaba disponible en el entorno local. Una vez Docker Desktop este activo, se
debe ejecutar `./gradlew.bat bootRun` y validar las peticiones con la coleccion
Postman.

---

## 8. Resultado de la sesion

| Elemento | Resultado |
|---|---|
| MySQL con Docker Compose | Configurado |
| Conexion Spring Boot a MySQL | Configurada |
| Mapeo JPA de productos | Implementado |
| Repositorio JPA de productos | Implementado |
| Contrato inicial de API | Documentado |
| Coleccion Postman | Creada |
| Pruebas automatizadas | Aprobadas |
| Validacion runtime con Docker | Pendiente por daemon Docker no disponible |
| Validacion QA | Pendiente en rama `hu-008-qa` |

---

## 9. Siguiente paso

Promover `HU-008` al ambiente QA mediante `hu-008-qa`, validar las peticiones
con Postman y continuar con las siguientes historias de persistencia o endpoints
del MVP.
