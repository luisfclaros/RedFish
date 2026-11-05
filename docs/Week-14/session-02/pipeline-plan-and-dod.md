# Semana 14 — Sesión 2: planificación del pipeline, DoD y ramas

## Flujo de integración propuesto

```text
feature/*
   |
   | Pull Request
   v
develop  ---- CI: build + tests + JaCoCo + CodeQL + Trivy
   |
   | Pull Request / promoción validada
   v
Qa       ---- CI + validación funcional / smoke test
   |
   | Pull Request de release
   v
main     ---- CI obligatorio + revisión aprobada
   |
   | Tag vX.Y.Z
   v
Imagen versionada en GitHub Container Registry (GHCR)
```

Este flujo conserva los nombres de ramas documentados en el README del proyecto. Las reglas de protección deben configurarse en GitHub; el workflow no puede activarlas por sí solo.

## Puertas requeridas por rama

| Rama destino | Checks requeridos | Revisión |
|---|---|---|
| develop | Build and tests; Static analysis (CodeQL); Dependency, secret and configuration scan | Al menos una aprobación antes de integrar. |
| Qa | Los mismos checks de CI más pruebas funcionales y smoke test cuando exista entorno desplegado | Aprobación de QA. |
| main | Todos los checks obligatorios y validación previa en Qa | Pull Request desde Qa y aprobación. |

## Configuración manual de protección

En GitHub: Settings → Branches (o Rulesets) → New branch ruleset.

Aplicar a `develop`, `Qa` y `main`:

- Require a pull request before merging.
- Require approvals (mínimo 1).
- Require status checks to pass before merging.
- Seleccionar los checks que publique el workflow: `Build, tests and coverage`, `Static analysis (CodeQL)` y `Dependency, secret and configuration scan`.
- Require conversation resolution.
- Bloquear force pushes y borrado de ramas.
- Para `main), permitir promoción únicamente mediante Pull Request desde `Qa`.

Nota: los nombres exactos de los status checks se seleccionan en GitHub después de que cada job haya ejecutado al menos una vez. No afirmar que están activos hasta verificarlo en Settings.

## Smoke test y contratos

- Smoke test: pendiente de un entorno QA desplegado con URL estable y credenciales/secretos configurados. Debe comprobar como mínimo que el servicio responde en su endpoint de salud.
- Contratos: pendiente de separar servicios y publicar contratos versionados. No se inventan pruebas entre microservicios mientras el repositorio personal siga siendo una aplicación monolítica.
- En el estado actual, las pruebas unitarias existentes son la validación automatizada ejecutable dentro del pipeline.

## Historias de usuario para MVP 3

| ID | Historia | Criterios de aceptación |
|---|---|---|
| CI-01 | Como desarrollador, quiero que cada PR compile y ejecute pruebas para bloquear regresiones. | Gradle finaliza correctamente; un fallo bloquea el PR cuando el check sea requerido. |
| CI-02 | Como responsable de calidad, quiero generar cobertura para conocer qué partes están probadas. | JaCoCo genera HTML/XML y el artefacto queda disponible. |
| CI-03 | Como equipo, queremos análisis estático y de seguridad para detectar riesgos antes de integrar. | CodeQL y Trivy se ejecutan en PR; fallos del job bloquean la integración. |
| CI-04 | Como responsable del repositorio, quiero proteger develop, Qa y main. | Las reglas exigen PR, aprobación y checks obligatorios. |
| CI-05 | Como responsable de QA, quiero una prueba de humo en el entorno de Qa. | El despliegue de QA responde correctamente en el endpoint de salud. |
| CI-06 | Como equipo, queremos verificar contratos entre servicios. | Los contratos publicados se validan automáticamente cuando los servicios estén separados. |
| CI-07 | Como responsable de release, quiero publicar imágenes versionadas. | Un tag semántico genera y publica una imagen identificable en GHCR. |

## Estado de implementación

- Implementado en esta entrega: workflow inicial de CI, compilación/pruebas, generación de cobertura, análisis CodeQL y escaneo Trivy.
- Pendiente de ejecutar/verificar: resultado real de Actions y selección de checks obligatorios.
- Pendiente de permisos administrativos: reglas de protección de ramas.
- Pendiente de infraestructura: smoke test contra QA desplegado.
- Pendiente de arquitectura de microservicios: pruebas de contratos entre servicios.
- Pendiente de release: publicación de imágenes versionadas en GHCR.

## Evidencias para Moodle

1. Pull Request con los archivos de CI y documentación.
2. Captura del workflow ejecutado y resultado de los jobs.
3. Informe de cobertura JaCoCo.
4. Capturas de las reglas de protección de ramas, si los permisos permiten configurarlas.
5. Registro de las historias CI-01 a CI-07 y sus criterios de aceptación.
