# QA - HU-008

## Validacion del contrato inicial de API con MySQL

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 4  
**Sesion:** 2  
**Historia de usuario:** HU-008  
**Rama de desarrollo:** `hu-008-dev`

---

## 1. Objetivo de QA

Validar que el backend RedFish pueda iniciar correctamente con MySQL mediante
Docker Compose y que el contrato inicial de API del modulo Inventario permita
crear y consultar productos persistidos en base de datos.

---

## 2. Alcance de la validacion

La validacion cubre:

- ejecucion de MySQL con Docker Compose;
- conexion de Spring Boot a la base de datos `redfish`;
- creacion automatica de la tabla `productos`;
- creacion de productos mediante `POST /api/products`;
- consulta de productos mediante `GET /api/products`;
- consulta de producto por identificador mediante `GET /api/products/{id}`;
- ejecucion de pruebas automatizadas del proyecto.

No se valida en esta HU:

- autenticacion real de usuarios;
- CRUD completo de productos;
- persistencia de otros modulos;
- migraciones formales con Flyway o Liquibase.

---

## 3. Ambiente de prueba

| Elemento | Valor |
|---|---|
| Backend | Spring Boot |
| Lenguaje | Java |
| Base de datos | MySQL |
| Contenedor | Docker Compose |
| Base de datos | `redfish` |
| Usuario | `myuser` |
| Puerto local MySQL | `3307` |
| Puerto interno MySQL | `3306` |
| URL API | `http://localhost:8080` |

El puerto local `3307` se usa porque el puerto `3306` se encontraba ocupado en
el equipo local.

---

## 4. Precondiciones

1. Estar ubicado en la carpeta del backend:

```powershell
cd D:\descargas\pago\RedFish\RedFish
```

2. Tener Docker Desktop iniciado.

3. Verificar que MySQL este activo:

```powershell
docker compose ps
```

Resultado esperado:

```text
redfish-mysql-1   mysql:8.4   Up   0.0.0.0:3307->3306/tcp
```

4. Ejecutar la aplicacion:

```powershell
.\gradlew.bat bootRun
```

Resultado esperado:

```text
Tomcat started on port 8080
Started RedFishApplication
```

---

## 5. Casos de prueba

### CP-001 - Verificar conexion de Spring Boot con MySQL

**Accion:** iniciar la aplicacion con `.\gradlew.bat bootRun`.

**Resultado esperado:** la aplicacion inicia sin errores y se conecta a MySQL en:

```text
jdbc:mysql://127.0.0.1:3307/redfish
```

**Estado:** Aprobado.

---

### CP-002 - Crear producto

**Metodo:** `POST`  
**Ruta:** `/api/products`  
**URL completa:** `http://localhost:8080/api/products`

Body:

```json
{
  "code": "PROD-008-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT"
}
```

**Resultado esperado:** respuesta `201 Created` con el producto creado.

Ejemplo de respuesta:

```json
{
  "id": 1,
  "code": "PROD-008-001",
  "name": "Tilapia Roja",
  "unitOfMeasure": "kg",
  "type": "PRODUCT",
  "active": true
}
```

**Estado:** Aprobado.

---

### CP-003 - Listar productos

**Metodo:** `GET`  
**Ruta:** `/api/products`  
**URL completa:** `http://localhost:8080/api/products`

**Resultado esperado:** respuesta `200 OK` con la lista de productos
persistidos.

Ejemplo de respuesta:

```json
[
  {
    "id": 1,
    "code": "PROD-008-001",
    "name": "Tilapia Roja",
    "unitOfMeasure": "kg",
    "type": "PRODUCT",
    "active": true
  }
]
```

**Estado:** Aprobado.

---

### CP-004 - Consultar producto por ID

**Metodo:** `GET`  
**Ruta:** `/api/products/{id}`  
**URL completa:** `http://localhost:8080/api/products/1`

**Resultado esperado:** respuesta `200 OK` con el producto solicitado.

**Estado:** Aprobado.

---

### CP-005 - Verificar persistencia en MySQL Workbench

**Accion:** conectar MySQL Workbench con los siguientes datos:

```text
Hostname: 127.0.0.1
Port: 3307
Username: myuser
Password: secret
Default Schema: redfish
```

Consulta:

```sql
SELECT * FROM redfish.productos;
```

**Resultado esperado:** se visualiza la tabla `productos` con los datos creados
desde la API.

**Estado:** Aprobado.

---

### CP-006 - Ejecutar pruebas automatizadas

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
| Docker Compose | MySQL activo en `localhost:3307` |
| Spring Boot | Aplicacion iniciada en `localhost:8080` |
| JPA/Hibernate | Tabla `productos` creada en MySQL |
| Postman / HTTP | `POST /api/products` aprobado |
| Postman / HTTP | `GET /api/products` aprobado |
| MySQL Workbench | Schema `redfish` y tabla `productos` visibles |
| Gradle Test | `BUILD SUCCESSFUL` |

---

## 7. Defectos encontrados

| ID | Descripcion | Estado |
|---|---|---|
| DEF-001 | El puerto local `3306` estaba ocupado y bloqueaba el arranque de MySQL en Docker. | Corregido usando `3307:3306` |

---

## 8. Resultado final

La historia de usuario `HU-008` queda aprobada para el ambiente de desarrollo.
El backend inicia correctamente, se conecta a MySQL mediante Docker, expone el
contrato inicial de productos y permite validar las peticiones principales con
Postman.

La validacion formal en rama `hu-008-qa` queda como siguiente paso dentro del
flujo de ramas definido para el proyecto.
