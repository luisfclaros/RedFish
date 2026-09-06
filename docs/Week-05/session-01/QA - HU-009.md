# QA - HU-009

## Validacion de contenedorizacion y segunda entidad persistida

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 5  
**Sesion:** 1  
**Historia de usuario:** HU-009  
**Rama base:** `Develop`

---

## 1. Objetivo de QA

Validar que RedFish pueda ejecutarse en un entorno local reproducible con Docker
Compose, usando Spring Boot como backend y MySQL como base de datos.

Tambien se valida que el sistema maneje una segunda entidad persistida:
`Cliente`, expuesta mediante endpoints REST propios del modulo `orders`.

---

## 2. Alcance de la validacion

La validacion cubre:

- contenedor MySQL mediante Docker Compose;
- contenedor de la aplicacion Spring Boot mediante Dockerfile;
- conexion del backend a MySQL usando variables de entorno;
- funcionamiento de productos como recurso persistido;
- funcionamiento de clientes como segunda entidad persistida;
- endpoints `POST`, `GET` y `GET by id` para clientes;
- verificacion de tablas `productos` y `clientes` en MySQL;
- ejecucion de pruebas automatizadas.

No se valida en esta HU:

- relacion directa entre clientes y productos;
- creacion de pedidos;
- autenticacion real;
- despliegue en ambiente productivo;
- migraciones formales con Flyway o Liquibase.

---

## 3. Ambiente de prueba

| Elemento | Valor |
|---|---|
| Backend | Spring Boot |
| Lenguaje | Java |
| Base de datos | MySQL |
| Orquestacion local | Docker Compose |
| Imagen base backend | Eclipse Temurin 17 |
| Base de datos | `redfish` |
| Usuario MySQL | `myuser` |
| Puerto local MySQL | `3307` |
| Puerto interno MySQL | `3306` |
| Puerto API | `8080` |

---

## 4. Precondiciones

1. Estar ubicado en la carpeta del backend:

```powershell
cd D:\descargas\pago\RedFish\RedFish
```

2. Tener Docker Desktop iniciado.

3. Verificar que el puerto `8080` este disponible.

4. Levantar la base de datos o el entorno completo:

```powershell
docker compose up -d mysql
```

o:

```powershell
docker compose --profile container up --build
```

---

## 5. Casos de prueba

### CP-001 - Ejecutar MySQL con Docker Compose

**Accion:** ejecutar:

```powershell
docker compose up -d mysql
docker compose ps
```

**Resultado esperado:** el servicio `redfish-mysql-1` queda activo y publica el
puerto local `3307`.

**Estado:** Aprobado.

---

### CP-002 - Construir imagen del backend

**Accion:** ejecutar:

```powershell
docker compose --profile container build app
```

**Resultado esperado:** Docker construye la imagen del backend usando
`RedFish/Dockerfile` sin errores de compilacion.

**Estado:** Pendiente de validacion en rama QA.

---

### CP-003 - Ejecutar backend y MySQL con Docker Compose

**Accion:** ejecutar:

```powershell
docker compose --profile container up --build
```

**Resultado esperado:** MySQL inicia correctamente y el backend queda disponible
en:

```text
http://localhost:8080
```

**Estado:** Pendiente de validacion en rama QA.

---

### CP-004 - Crear cliente

**Metodo:** `POST`  
**Ruta:** `/api/customers`  
**URL completa:** `http://localhost:8080/api/customers`

Body:

```json
{
  "name": "Restaurante El Lago",
  "phone": "3001234567",
  "address": "Calle 10 # 15-20",
  "email": "compras@ellago.com"
}
```

**Resultado esperado:** respuesta `201 Created` con el cliente creado.

Ejemplo de respuesta:

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

**Estado:** Aprobado en pruebas automatizadas. Pendiente de validacion manual en
Postman durante QA.

---

### CP-005 - Listar clientes

**Metodo:** `GET`  
**Ruta:** `/api/customers`  
**URL completa:** `http://localhost:8080/api/customers`

**Resultado esperado:** respuesta `200 OK` con la lista de clientes persistidos.

**Estado:** Aprobado en pruebas automatizadas. Pendiente de validacion manual en
Postman durante QA.

---

### CP-006 - Consultar cliente por ID

**Metodo:** `GET`  
**Ruta:** `/api/customers/{id}`  
**URL completa:** `http://localhost:8080/api/customers/1`

**Resultado esperado:** respuesta `200 OK` con el cliente solicitado.

**Estado:** Aprobado en pruebas automatizadas. Pendiente de validacion manual en
Postman durante QA.

---

### CP-007 - Verificar productos y clientes como recursos independientes

**Accion:** ejecutar peticiones sobre ambos recursos:

```text
GET http://localhost:8080/api/products
GET http://localhost:8080/api/customers
```

**Resultado esperado:** ambos endpoints responden correctamente y cada recurso
mantiene su propia tabla.

**Estado:** Pendiente de validacion manual en QA.

---

### CP-008 - Verificar tablas en MySQL Workbench

**Accion:** conectar MySQL Workbench con:

```text
Hostname: 127.0.0.1
Port: 3307
Username: myuser
Password: secret
Default Schema: redfish
```

Consultas:

```sql
SHOW TABLES;
SELECT * FROM redfish.productos;
SELECT * FROM redfish.clientes;
```

**Resultado esperado:** se visualizan las tablas `productos` y `clientes`.

**Estado:** Pendiente de validacion manual en QA.

---

### CP-009 - Ejecutar pruebas automatizadas

Comando:

```powershell
.\gradlew.bat test
```

**Resultado esperado:**

```text
BUILD SUCCESSFUL
```

**Estado:** Aprobado.

---

## 6. Evidencias

| Evidencia | Resultado |
|---|---|
| Dockerfile backend | Implementado |
| `.dockerignore` | Implementado |
| Docker Compose MySQL | Implementado |
| Docker Compose backend | Implementado con perfil `container` |
| Variables de entorno datasource | Implementadas |
| Endpoint `POST /api/customers` | Implementado |
| Endpoint `GET /api/customers` | Implementado |
| Endpoint `GET /api/customers/{id}` | Implementado |
| Tabla `clientes` | Configurada mediante JPA/Hibernate |
| Pruebas automatizadas | `BUILD SUCCESSFUL` |

---

## 7. Defectos encontrados

| ID | Descripcion | Estado |
|---|---|---|
| DEF-001 | La documentacion previa mencionaba PostgreSQL/PostgREST, aunque el stack seleccionado es Spring Boot + MySQL. | Corregido |

---

## 8. Resultado final

La historia de usuario `HU-009` queda lista para validacion en QA. La
implementacion permite ejecutar RedFish con Docker Compose, mantiene MySQL como
base de datos oficial del proyecto y agrega `Cliente` como segunda entidad
persistida.

Durante la rama `hu-009-qa` se deben validar manualmente los endpoints con
Postman y verificar en MySQL Workbench que existan las tablas `productos` y
`clientes`.
