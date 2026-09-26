# QA - HU-011

## Validacion de la orquestacion con Docker Compose

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 6  
**Sesion:** 1  
**Historia de usuario:** HU-011  
**Rama de desarrollo:** `hu-011-dev`  
**Rama de QA:** `hu-011-qa`

---

## 1. Objetivo de QA

Validar que RedFish y MySQL puedan ejecutarse como un sistema reproducible
mediante Docker Compose, comprobando configuracion externa, orden de inicio,
estado de salud, comunicacion por red, persistencia y compatibilidad con los
recursos funcionales del MVP 1.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Aplicacion | Monolito modular Spring Boot contenido en el servicio `app`. |
| Base de datos | MySQL 8.4 contenido en el servicio `mysql`. |
| Orquestacion | Inicio completo mediante Docker Compose. |
| Red | Comunicacion por nombre de servicio en `redfish-network`. |
| Disponibilidad | Health checks para aplicacion y base de datos. |
| Persistencia | Volumen nombrado `redfish-mysql-data`. |
| Configuracion | Variables de entorno definidas mediante `.env`. |
| Secretos | `.env` ignorado y `.env.example` versionado. |
| Regresion | Endpoints de productos y clientes disponibles. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | `docker compose up --build` inicia la aplicacion y MySQL. | PASS | Imagen construida y servicios iniciados. |
| CA-02 | Los servicios comparten una red declarada. | PASS | Red `redfish-network` visible en la configuracion efectiva. |
| CA-03 | MySQL debe estar saludable antes de iniciar la aplicacion. | PASS | `depends_on` usa `condition: service_healthy`. |
| CA-04 | La aplicacion publica su estado de salud. | PASS | `/actuator/health` respondio `UP`. |
| CA-05 | MySQL utiliza un volumen nombrado. | PASS | Volumen `redfish-mysql-data` montado en `/var/lib/mysql`. |
| CA-06 | La configuracion se obtiene del ambiente. | PASS | Datasource, puertos, JPA y logs usan variables de entorno. |
| CA-07 | Las credenciales reales no se almacenan en Git. | PASS | `.env` se encuentra ignorado por `.gitignore`. |
| CA-08 | Existe una plantilla de configuracion. | PASS | Archivo `RedFish/.env.example`. |
| CA-09 | Productos y clientes siguen funcionando. | PASS | Peticiones GET respondieron correctamente. |
| CA-10 | Las pruebas automatizadas finalizan correctamente. | PASS | `BUILD SUCCESSFUL`. |

---

## 4. Casos de prueba

### CP-001 - Validar la configuracion de Compose

Comando:

```powershell
docker compose config --quiet
```

Resultado esperado: el archivo se procesa sin errores y contiene los servicios
`app` y `mysql`.

Resultado obtenido: configuracion valida.

Estado: PASS.

---

### CP-002 - Ejecutar pruebas automatizadas

Comando:

```powershell
.\gradlew.bat test
```

Resultado obtenido:

```text
BUILD SUCCESSFUL in 4s
4 actionable tasks: 2 executed, 2 up-to-date
```

Estado: PASS.

---

### CP-003 - Construir e iniciar el sistema

Comando:

```powershell
docker compose up --build -d
```

Resultado esperado: se construye `redfish-app:local`, MySQL alcanza el estado
saludable y posteriormente inicia la aplicacion.

Resultado obtenido: construccion e inicio correctos.

Estado: PASS.

---

### CP-004 - Verificar el estado de los servicios

Comando:

```powershell
docker compose ps
```

Resultado obtenido:

| Servicio | Estado | Puerto |
|---|---|---|
| `app` | `healthy` | `8080` |
| `mysql` | `healthy` | `3307` |

Estado: PASS.

---

### CP-005 - Consultar la salud de Spring Boot

Peticion:

```http
GET http://localhost:8080/actuator/health
```

Resultado obtenido:

```json
{
  "groups": ["liveness", "readiness"],
  "status": "UP"
}
```

Estado: PASS.

---

### CP-006 - Ejecutar pruebas de regresion de la API

Peticiones:

```http
GET http://localhost:8080/api/products
GET http://localhost:8080/api/customers
```

Resultado obtenido: ambos endpoints respondieron correctamente.

Estado: PASS.

---

### CP-007 - Verificar persistencia del volumen

Procedimiento:

1. Crear el producto de prueba con codigo `HU011-TEST`.
2. Detener y recrear los contenedores sin eliminar volumenes.
3. Consultar nuevamente `/api/products`.

Resultado obtenido: el producto `HU011-TEST` continuo disponible despues de
recrear los contenedores.

Estado: PASS.

---

### CP-008 - Verificar archivos excluidos de Git

Elementos revisados:

```text
RedFish/.env
RedFish/.gradle/
RedFish/bin/
RedFish/build/
```

Resultado obtenido: todos se encuentran ignorados y ninguno sera incluido en
el Pull Request. La plantilla `RedFish/.env.example` si debe versionarse.

Estado: PASS.

---

## 5. Evidencias

| Evidencia | Resultado |
|---|---|
| Configuracion efectiva de Compose | Valida |
| Construccion de la imagen Spring Boot | Aprobada |
| Pruebas Gradle | `BUILD SUCCESSFUL` |
| Estado de `app` | `healthy` |
| Estado de `mysql` | `healthy` |
| Spring Boot Actuator | `UP` |
| API de productos | PASS |
| API de clientes | PASS |
| Persistencia MySQL | PASS |
| Archivos locales excluidos | PASS |

---

## 6. Defectos encontrados

| ID | Descripcion | Estado |
|---|---|---|
| N/A | No se identificaron defectos bloqueantes ni regresiones durante la validacion de HU-011. | N/A |

---

## 7. Elementos que no deben incluirse

No deben agregarse manualmente al commit ni al Pull Request:

```text
RedFish/.env
RedFish/.gradle/
RedFish/bin/
RedFish/build/
```

Estos elementos son locales o generados. No es necesario eliminarlos del
equipo porque `.gitignore` ya evita su versionado.

---

## 8. Resultado final

QA considera que `HU-011` cumple todos sus criterios de aceptacion. La
orquestacion mantiene el monolito modular, elimina credenciales fijas de la
configuracion activa, conserva los datos de MySQL y permite iniciar el sistema
completo mediante Docker Compose.

La historia queda aprobada para seguir el flujo:

```text
hu-011-dev -> Develop -> hu-011-qa -> Qa
```

El Pull Request desde `hu-011-qa` hacia `Qa` puede realizarse.
