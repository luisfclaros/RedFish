# Estado de historias de usuario - Week 1

## Informacion general

**Proyecto:** RedFish  
**Responsable:** LUIS FERNANDO CLAROS RAMOS  
**Usuario GitHub:** luisfclaros  
**Asignatura:** Sistemas Distribuidos  
**Semana:** 1

---

## Estado de historias de usuario

| HU | Descripcion | Rama de desarrollo | Estado |
|---|---|---|---|
| HU-001 | Definicion inicial y fundamentos de RedFish. | `docs/week-01-session-01` | Completada |
| HU-002 | Estandares de ingenieria y flujo de trabajo. | `hu-002-dev` | En validacion documental |

---

## Evidencias de Week 1

| Evidencia | Ubicacion | Estado |
|---|---|---|
| Documentacion de Sesion 1 | `docs/Week-01/session-01/` | Registrada |
| Documentacion de Sesion 2 | `docs/Week-01/session-02/README.md` | Registrada |
| ADR-001 | `docs/adr/adr-001-architecture.md` | Propuesto |
| Backlog inicial | `docs/backlog.md` | Registrado |
| Flujo Git | `README.md` y `docs/Week-01/session-02/README.md` | Registrado |

---

## Notas

RedFish se trabaja como proyecto individual. La trazabilidad de las historias se
mantiene mediante ramas por ambiente:

```text
Develop -> hu-002-dev -> Pull Request -> Develop
Qa -> hu-002-qa -> Pull Request -> Qa
Qa -> Pull Request -> main
```

La evidencia QA de `HU-002` debe generarse en la rama `hu-002-qa`, no en la rama
de desarrollo.
