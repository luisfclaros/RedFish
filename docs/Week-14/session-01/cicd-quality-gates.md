# Semana 14 — Sesión 1: CI y puertas de calidad

## Objetivo

Ejecutar validaciones automáticas en cada Pull Request que proponga cambios al backend RedFish, para evitar integrar código que no compile, tenga pruebas fallidas, presente hallazgos estáticos relevantes o contenga vulnerabilidades/secrets de severidad alta o crítica.

## Contexto verificado del repositorio

- Proyecto Spring Boot con Gradle.
- Java Toolchain 17.
- Aplicación ubicada en `RedFish/`.
- Pruebas JUnit 5 mediante `useJUnitPlatform()`.
- Existen pruebas unitarias de dominio y servicios.
- `RedFishApplicationTests` está deshabilitada porque requiere Docker/Testcontainers; por tanto, la carga completa del contexto no se considera validada por este pipeline.
- Se incorpora JaCoCo para generar informes HTML y XML. En esta primera iteración no se impone un porcentaje mínimo de cobertura: primero se debe obtener y revisar una línea base real.

## Pipeline implementado

Archivo: `.github/workflows/ci.yml`

| Puerta | Herramienta / ejecución | Criterio de aprobación |
|---|---|---|
| Compilación y pruebas | Gradle: `clean test` | El comando termina correctamente. |
| Cobertura | JaCoCo: `jacocoTestReport` | Se generan informes HTML y XML; se publican como artefacto. |
| Análisis estático | GitHub CodeQL | El análisis debe finalizar correctamente. Los hallazgos se revisan en Security. |
| Seguridad | Trivy (vulnerabilidades, configuración y secretos) | No se permiten hallazgos HIGH o CRITICAL en el escaneo configurado. |

El pipeline se activa en Pull Requests y pushes hacia `develop`, `Qa` y `main`, y también permite ejecución manual. Se filtran cambios del backend y de los propios workflows.

## Definition of Done (DoD) para un cambio de backend

- [ ] El código compila con Java 17.
- [ ] Las pruebas automatizadas aplicables pasan.
- [ ] El informe JaCoCo se genera y se revisa.
- [ ] CodeQL finaliza sin errores de ejecución y sus alertas se revisan.
- [ ] Trivy finaliza sin vulnerabilidades HIGH/CRITICAL ni secretos detectados.
- [ ] El Pull Request describe el cambio y enlaza la HU correspondiente.
- [ ] El cambio recibe revisión y aprobación antes de integrarse.
- [ ] No se integran cambios con una puerta obligatoria fallida.

## Limitaciones y pendientes

1. La prueba de contexto Spring está deshabilitada y no debe reportarse como ejecutada.
2. No se define umbral de cobertura hasta medir la línea base y acordar un valor con el equipo/docente.
3. El análisis de contratos entre microservicios no aplica todavía al monolito personal; se debe incorporar cuando existan servicios desplegables y contratos ejecutables.
4. La ejecución real de GitHub Actions debe verificarse después de abrir el Pull Request. La creación del archivo YAML por sí sola no demuestra que el pipeline haya pasado.
5. La protección obligatoria de ramas se configura en Settings > Branches / Rulesets y requiere permisos administrativos. Debe activarse después de que GitHub registre los checks al menos una vez.

## Evidencias

- Archivo de workflow: `.github/workflows/ci.yml`.
- Configuración JaCoCo: `RedFish/build.gradle`.
- Captura de Actions con el resultado de cada job.
- Artefacto `redfish-test-and-coverage-reports`.
- Captura de reglas de protección de ramas, cuando estén habilitadas.
