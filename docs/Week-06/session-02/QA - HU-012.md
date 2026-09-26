# QA - HU-012

## Validacion de ambientes y estrategia de configuracion

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 6  
**Sesion:** 2  
**Historia de usuario:** HU-012  
**Rama de desarrollo:** `hu-012-dev`  
**Rama de QA:** `hu-012-qa`

---

## 1. Objetivo de QA

Validar que RedFish disponga de configuraciones independientes para Develop,
QA y produccion, que los tres ambientes utilicen la misma imagen de la
aplicacion y que los secretos, redes, volumenes y puertos permanezcan aislados.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Compose base | Topologia comun de aplicacion y MySQL. |
| Develop | Construccion local mediante `compose.override.yaml`. |
| QA | Consumo de imagen existente mediante `compose.qa.yaml`. |
| Produccion | Consumo de imagen existente mediante `compose.prod.yaml`. |
| Configuracion | Mismos nombres de variables en los tres ambientes. |
| Secretos | Archivos reales de entorno excluidos de Git. |
| Aislamiento | Proyectos, redes y volumenes independientes. |
| Promocion | Misma imagen ejecutada sin reconstruccion en QA y produccion. |
| Regresion | Salud y endpoints principales disponibles. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | Se definen `develop`, `qa` y `prod`. | PASS | Proyectos `redfish-develop`, `redfish-qa` y `redfish-prod`. |
| CA-02 | Existe un Compose base compartido. | PASS | `RedFish/compose.yaml`. |
| CA-03 | Develop puede construir la imagen. | PASS | `compose.override.yaml` contiene `build`. |
| CA-04 | QA y produccion no reconstruyen la imagen. | PASS | Sus configuraciones efectivas no contienen `build`. |
| CA-05 | Existe una matriz unica de variables. | PASS | `docs/configuration/environment-matrix.md`. |
| CA-06 | Los secretos reales permanecen fuera de Git. | PASS | `.env`, `.env.qa` y `.env.prod` estan ignorados. |
| CA-07 | Compose valida las variables requeridas. | PASS | Se utiliza `${VARIABLE:?mensaje}`. |
| CA-08 | La relacion rama-ambiente esta documentada. | PASS | Develop, Qa y main se asocian con develop, qa y prod. |
| CA-09 | Se conserva el flujo `Qa -> main`. | PASS | No se define una rama hija de produccion. |
| CA-10 | Las tres configuraciones tienen sintaxis valida. | PASS | Los tres comandos `config --quiet` finalizaron correctamente. |
| CA-11 | Las pruebas automatizadas finalizan correctamente. | PASS | `BUILD SUCCESSFUL`. |

---

## 4. Casos de prueba

### CP-001 - Validar sintaxis de los ambientes

Comandos:

```powershell
docker compose --env-file .env -f compose.yaml -f compose.override.yaml config --quiet
docker compose --env-file .env.qa -f compose.yaml -f compose.qa.yaml config --quiet
docker compose --env-file .env.prod -f compose.yaml -f compose.prod.yaml config --quiet
```

Resultado obtenido:

```text
DEVELOP_CONFIG=PASS
QA_CONFIG=PASS
PROD_CONFIG=PASS
```

Estado: PASS.

---

### CP-002 - Verificar donde se construye la imagen

Resultado obtenido:

| Ambiente | Contiene `build` |
|---|---|
| Develop | Si |
| QA | No |
| Produccion | No |

Estado: PASS.

---

### CP-003 - Verificar promocion de la misma imagen

Contenedores revisados:

```text
redfish-develop-app-1
redfish-qa-app-1
redfish-prod-app-1
```

ID obtenido en los tres ambientes:

```text
sha256:80832abba980df8c8d23191155f376706edc5e1e6ba3191980a2fd10043ba01b
```

Estado: PASS.

---

### CP-004 - Verificar estado de los contenedores

| Ambiente | Aplicacion | MySQL |
|---|---|---|
| Develop | `healthy` | `healthy` |
| QA | `healthy` | `healthy` |
| Produccion | `healthy` | `healthy` |

Estado: PASS.

---

### CP-005 - Verificar puertos externos

| Ambiente | Aplicacion | MySQL |
|---|---|---|
| Develop | `8080 -> 8080` | `3307 -> 3306` |
| QA | `8081 -> 8080` | No publicado |
| Produccion | `8082 -> 8080` | No publicado |

Los puertos internos pueden repetirse porque cada contenedor esta aislado. Los
puertos externos de las aplicaciones son distintos para permitir la ejecucion
simultanea en el mismo equipo.

Estado: PASS.

---

### CP-006 - Consultar salud por ambiente

Peticiones:

```http
GET http://localhost:8080/actuator/health
GET http://localhost:8081/actuator/health
GET http://localhost:8082/actuator/health
```

Resultado obtenido: los tres ambientes respondieron `UP`.

Estado: PASS.

---

### CP-007 - Verificar aislamiento

| Recurso | Develop | QA | Produccion |
|---|---|---|---|
| Proyecto | `redfish-develop` | `redfish-qa` | `redfish-prod` |
| Red | `redfish-develop-network` | `redfish-qa-network` | `redfish-prod-network` |
| Volumen | `redfish-develop-mysql-data` | `redfish-qa-mysql-data` | `redfish-prod-mysql-data` |

Resultado obtenido: redes y volumenes independientes.

Estado: PASS.

---

### CP-008 - Verificar exclusion de secretos y artefactos

Elementos ignorados:

```text
RedFish/.env
RedFish/.env.qa
RedFish/.env.prod
RedFish/.gradle/
RedFish/bin/
RedFish/build/
```

Resultado obtenido: ninguno sera incluido en el Pull Request.

Estado: PASS.

---

### CP-009 - Ejecutar pruebas automatizadas

Comando:

```powershell
.\gradlew.bat test
```

Resultado obtenido:

```text
BUILD SUCCESSFUL in 1s
4 actionable tasks: 4 up-to-date
```

Estado: PASS.

---

## 5. Aprovisionamiento de produccion

La configuracion productiva utiliza:

```text
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

Para la prueba local se aprovisionaron una sola vez las tablas `clientes` y
`productos`. Despues se inicio el contenedor definitivo con `validate`, sin
permitir que Hibernate modificara el esquema durante el arranque productivo.

Esta prueba confirma el comportamiento esperado, pero no sustituye una
herramienta de migraciones versionadas.

---

## 6. Evidencias

| Evidencia | Resultado |
|---|---|
| Configuraciones Compose | Tres ambientes validos |
| Contenedores activos | Seis contenedores `healthy` |
| Imagen de aplicacion | Mismo ID en los tres ambientes |
| Construccion restringida a Develop | PASS |
| Redes aisladas | PASS |
| Volumenes aislados | PASS |
| Puertos externos sin conflicto | PASS |
| Endpoints de salud | Tres respuestas `UP` |
| Pruebas Gradle | `BUILD SUCCESSFUL` |
| Secretos fuera de Git | PASS |

---

## 7. Defectos y observaciones

| ID | Tipo | Descripcion | Estado |
|---|---|---|---|
| N/A | Defecto | No se identificaron defectos bloqueantes ni regresiones. | N/A |
| OBS-01 | Limitacion conocida | Produccion requiere aprovisionamiento previo del esquema porque el proyecto aun no incorpora Flyway o Liquibase. | Pendiente para una HU posterior |

---

## 8. Elementos que no deben incluirse

No deben agregarse al commit:

```text
RedFish/.env
RedFish/.env.qa
RedFish/.env.prod
RedFish/.gradle/
RedFish/bin/
RedFish/build/
```

No es necesario eliminarlos del equipo. `.gitignore` evita su versionado.

---

## 9. Resultado final

QA considera que `HU-012` cumple todos sus criterios de aceptacion. Los tres
ambientes son independientes, ejecutan la misma imagen y mantienen sus
secretos fuera del repositorio.

La historia queda aprobada para seguir el flujo:

```text
hu-012-dev -> Develop -> hu-012-qa -> Qa
```

El Pull Request desde `hu-012-qa` hacia `Qa` puede realizarse.
