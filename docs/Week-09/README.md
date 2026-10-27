# Semana 9 — Configuración segura y entrega progresiva

Esta carpeta resume los temas trabajados durante la semana 9 de Sistemas Distribuidos. El contenido documenta conceptos, decisiones y verificaciones propuestas; no implica que todas las prácticas descritas estén implementadas en RedFish.

## Sesión 1: Configuración segura
- Separar la configuración por entorno (desarrollo, pruebas y producción).
- Mantener credenciales y secretos fuera del repositorio y de las imágenes.
- Definir un procedimiento de rotación para credenciales.
- Verificar configuración y secretos antes del despliegue.

## Sesión 2: Feature flags y entrega progresiva
- Distinguir entre desplegar una versión y habilitar una funcionalidad para usuarios.
- Usar feature flags para controlar la activación de funcionalidades.
- Planificar entregas progresivas, criterios de observación y recuperación.
- Definir pasos de rollback y verificaciones posteriores.

## Validaciones pendientes
- Confirmar que no existan secretos en Git o en las imágenes publicadas.
- Comprobar las configuraciones por entorno.
- Probar la desactivación de una funcionalidad mediante una feature flag si aplica.
- Documentar y ensayar el procedimiento de rollback antes de declarar que está validado.

## Relación con RedFish
Estas prácticas pueden servir para preparar el despliegue seguro de RedFish. Cada práctica debe verificarse en el entorno real antes de declararse implementada.
