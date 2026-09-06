# QA - HU-010

## Validacion final del MVP 1

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 5  
**Sesion:** 2  
**Historia de usuario:** HU-010  
**Rama de desarrollo:** `hu-010-dev`

---

## 1. Objetivo de QA

Validar que el MVP 1 de RedFish se encuentre listo para ser promovido desde
`Qa` hacia `main`, verificando alcance funcional, documentacion, pruebas
automatizadas y pendientes conocidos.

---

## 2. Alcance evaluado

| Area | Validacion |
|---|---|
| Arquitectura | Monolito modular documentado. |
| Stack tecnico | Spring Boot + Java + MySQL. |
| Base de datos | MySQL ejecutable con Docker Compose. |
| API Inventario | Endpoints iniciales de productos. |
| API Pedidos | Endpoints iniciales de clientes. |
| Persistencia | JPA/Hibernate para `productos` y `clientes`. |
| Docker | Dockerfile y Compose disponibles. |
| Documentacion | Sesiones, ADR, dominio, API, QA y release candidate. |
| Pruebas | Suite automatizada ejecutada correctamente. |

---

## 3. Criterios de aceptacion

| ID | Criterio | Estado | Evidencia |
|---|---|---|---|
| CA-01 | Se revisa que el stack del proyecto sea consistente: Spring Boot + MySQL. | PASS | Documentacion de arquitectura y HU-009. |
| CA-02 | Se valida que el backend tenga endpoints funcionales para productos. | PASS | Contrato API y pruebas de controlador. |
| CA-03 | Se valida que el backend tenga endpoints funcionales para clientes. | PASS | Contrato API y pruebas de controlador. |
| CA-04 | Se confirma que MySQL puede ejecutarse mediante Docker Compose. | PASS | `compose.yaml`. |
| CA-05 | Se confirma que el backend puede ejecutarse localmente con Spring Boot. | PASS | Validaciones previas y pruebas automatizadas. |
| CA-06 | Se documenta el alcance real del MVP 1. | PASS | `docs/release/mvp-1-release.md`. |
| CA-07 | Se registran pendientes conocidos para historias posteriores. | PASS | Documento de sesion y release candidate. |
| CA-08 | Se ejecutan pruebas automatizadas del backend. | PASS | `./gradlew.bat test` con `BUILD SUCCESSFUL`. |
| CA-09 | Se prepara comentario para Pull Request de `Qa` hacia `main`. | PASS | Documento de sesion HU-010. |

---

## 4. Casos de prueba

### CP-001 - Ejecutar pruebas automatizadas

Comando:

```powershell
.\gradlew.bat test
```

Resultado:

```text
BUILD SUCCESSFUL
```

Estado: PASS.

---

### CP-002 - Verificar contrato de productos

Endpoints revisados:

```text
POST /api/products
GET  /api/products
GET  /api/products/{id}
```

Resultado esperado: los endpoints se encuentran documentados y cubiertos por
pruebas de controlador.

Estado: PASS.

---

### CP-003 - Verificar contrato de clientes

Endpoints revisados:

```text
POST /api/customers
GET  /api/customers
GET  /api/customers/{id}
```

Resultado esperado: los endpoints se encuentran documentados y cubiertos por
pruebas de controlador.

Estado: PASS.

---

### CP-004 - Verificar release candidate

Archivo revisado:

```text
docs/release/mvp-1-release.md
```

Resultado esperado: el documento resume alcance incluido, stack validado,
endpoints disponibles, tablas esperadas, criterio de liberacion y pendientes
post-MVP.

Estado: PASS.

---

## 5. Evidencias

| Evidencia | Resultado |
|---|---|
| Pruebas automatizadas | `BUILD SUCCESSFUL` |
| Contrato API | Productos y clientes documentados |
| Documento release | MVP 1 resumido |
| Pendientes post-MVP | Documentados |
| Comentario PR `Qa` -> `main` | Preparado |

---

## 6. Defectos encontrados

| ID | Descripcion | Estado |
|---|---|---|
| N/A | No se identifican defectos bloqueantes en el cierre documental y tecnico del MVP 1. | N/A |

---

## 7. Resultado final

QA considera que `HU-010` cumple su objetivo de cierre del MVP 1. El proyecto
queda listo para ser promovido dentro del flujo definido:

```text
hu-010-dev -> Develop -> Qa -> main
```

El Pull Request final de `Qa` hacia `main` sera realizado manualmente por el
responsable del proyecto.
