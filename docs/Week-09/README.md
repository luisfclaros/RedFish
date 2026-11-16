# Semana 9 — Configuración segura y entrega progresiva

Esta semana reúne los temas de configuración por entorno, gestión de secretos, feature flags y prácticas de despliegue seguro para RedFish. El contenido documenta conceptos, decisiones y verificaciones propuestas; no implica que todas las prácticas descritas estén implementadas en el sistema.

## Contenido

- [Sesión 1 — Configuración segura y gestión de secretos](session-01/Week%209%20-%20Session%201.md)
- [Sesión 2 — Feature flags, despliegue y entrega progresiva](session-02/Week%209%20-%20Session%202.md)

## Contexto del proyecto

RedFish es una aplicación web para apoyar la gestión de producción, operaciones, procesos comerciales y administración de una piscícola. La documentación propone prácticas para separar la configuración de los entornos y reducir riesgos durante la entrega de cambios.

## Alcance y estado

Este documento registra decisiones y recomendaciones de diseño. No afirma que todas las medidas descritas ya estén implementadas en el código. La implementación y su verificación deben registrarse únicamente después de realizar las tareas correspondientes y aportar evidencia.

## Criterios de trabajo

1. No guardar contraseñas, tokens ni credenciales en el repositorio.
2. Mantener la configuración específica de cada entorno fuera del código fuente.
3. Verificar los cambios antes de integrarlos mediante el flujo de ramas y Pull Request del equipo.
4. Documentar pruebas, responsables y evidencia cuando las medidas se implementen.

## Validaciones pendientes

- Confirmar que no existan secretos en Git o en las imágenes publicadas.
- Comprobar las configuraciones por entorno.
- Probar la desactivación de una funcionalidad mediante una feature flag si aplica.
- Documentar y ensayar el procedimiento de rollback antes de declarar que está validado.

## Material de referencia

- [OVA — Semana 9, Sesión 1](https://code-corhuila.github.io/ova-web/2026-B/distribuidos/09-week/01-session/)
- [OVA — Semana 9, Sesión 2](https://code-corhuila.github.io/ova-web/2026-B/distribuidos/09-week/02-session/)

## Relación con RedFish

Estas prácticas pueden servir para preparar el despliegue seguro de RedFish. Cada práctica debe verificarse en el entorno real antes de declararse implementada.
