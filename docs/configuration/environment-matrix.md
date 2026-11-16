# Matriz de configuracion por ambientes

## 1. Proposito

Definir un conjunto unico y consistente de variables para ejecutar la misma
imagen de RedFish en desarrollo, QA y produccion. Los ambientes cambian su
configuracion, pero no el codigo ni el contenido de la imagen promovida.

---

## 2. Principio de promocion

```text
Construir una vez -> validar en Develop -> promover cada HU a Qa
                                               |
                                               v
                          al completar el MVP: promover Qa a main
```

`APP_IMAGE` debe conservar exactamente la misma etiqueta inmutable o digest
durante la promocion. QA y produccion no contienen una instruccion `build`.

En un despliegue remoto, la imagen debera publicarse en un registro y
referenciarse mediante una version inmutable. No se recomienda usar `latest`.

---

## 3. Relacion entre ramas y ambientes

| Rama | Ambiente | Configuracion | Construye imagen |
|---|---|---|---|
| `Develop` | `develop` | `.env` + `compose.override.yaml` | Si |
| `Qa` | `qa` | `.env.qa` + `compose.qa.yaml` | No |
| `main` | `prod` | `.env.prod` + `compose.prod.yaml` | No |

El flujo del proyecto permanece:

```text
Por cada HU: hu-xxx-dev -> Develop -> hu-xxx-qa -> Qa
Fin del MVP: Qa -> main
```

`main` recibe un Pull Request directo desde `Qa` solamente cuando se completa
el MVP; no se crea una rama hija de produccion.

---

## 4. Matriz de variables

| Variable | Develop | QA | Produccion | Sensible |
|---|---|---|---|---|
| `COMPOSE_PROJECT_NAME` | `redfish-develop` | `redfish-qa` | `redfish-prod` | No |
| `SPRING_PROFILES_ACTIVE` | `develop` | `qa` | `prod` | No |
| `APP_IMAGE` | Misma version promovida | Misma version promovida | Misma version promovida | No |
| `APP_PORT` | `8080` | `8081` | `8082` para validacion local | No |
| `APP_MEMORY_LIMIT` | `768M` sugerido | `768M` sugerido | `1G` sugerido | No |
| `MYSQL_IMAGE` | `mysql:8.4` | `mysql:8.4` | `mysql:8.4` | No |
| `MYSQL_PORT` | `3307` | `3308` | `3309` para validacion local | No |
| `MYSQL_VOLUME_NAME` | `redfish-develop-mysql-data` | `redfish-qa-mysql-data` | `redfish-prod-mysql-data` | No |
| `NETWORK_NAME` | `redfish-develop-network` | `redfish-qa-network` | `redfish-prod-network` | No |
| `MYSQL_DATABASE` | `redfish` | Base aislada de QA | Base de produccion | No |
| `MYSQL_USER` | Usuario local | Inyectado en QA | Inyectado en produccion | Si |
| `MYSQL_PASSWORD` | Secreto local | Secreto de QA | Gestor de secretos | Si |
| `MYSQL_ROOT_PASSWORD` | Secreto local | Secreto de QA | Gestor de secretos | Si |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | `update` temporal | `validate` | No |
| `SPRING_JPA_SHOW_SQL` | `false` | `false` | `false` | No |
| `LOG_LEVEL` | `INFO` o `DEBUG` | `INFO` | `WARN` | No |

`validate` en produccion requiere que el esquema haya sido creado previamente.
La incorporacion de migraciones con Flyway o Liquibase permanece como trabajo
posterior antes de un despliegue productivo real.

---

## 5. Archivos de entorno

El repositorio contiene ejemplos separados para los tres ambientes. Los
archivos reales se crean localmente y estan excluidos por `.gitignore`:

```text
.env.example
.env.qa.example
.env.prod.example
```

Los archivos locales son:

```text
.env
.env.qa
.env.prod
```

Preparacion de desarrollo:

```powershell
Copy-Item .env.example .env
```

Preparacion local de QA y produccion:

```powershell
Copy-Item .env.qa.example .env.qa
Copy-Item .env.prod.example .env.prod
```

Los puertos `3307`, `3308` y `3309` permiten inspeccionar cada base desde
MySQL Workbench sin conflictos. La exposicion de MySQL en produccion es solo
para la demostracion local; un despliegue productivo real no debe publicar el
puerto de la base de datos y debe usar una red privada.

---

## 6. Comandos por ambiente

### Desarrollo

`compose.override.yaml` se carga automaticamente:

```powershell
docker compose up --build -d
```

### QA

```powershell
docker compose --env-file .env.qa -f compose.yaml -f compose.qa.yaml up -d
```

### Produccion

```powershell
docker compose --env-file .env.prod -f compose.yaml -f compose.prod.yaml up -d
```

Los comandos de QA y produccion no deben incluir `--build`.

---

## 7. Validacion obligatoria

Las configuraciones pueden comprobarse sin iniciar servicios:

```powershell
docker compose --env-file .env -f compose.yaml -f compose.override.yaml config --quiet
docker compose --env-file .env.qa -f compose.yaml -f compose.qa.yaml config --quiet
docker compose --env-file .env.prod -f compose.yaml -f compose.prod.yaml config --quiet
```

Si falta una variable requerida, Compose detiene la ejecucion con un mensaje
claro. Spring Boot tambien falla al inicio si no recibe las variables del
datasource.
