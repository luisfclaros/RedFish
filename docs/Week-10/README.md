# Semana 10 — Persistencia distribuida y preparación de MVP 2

Esta carpeta resume los temas trabajados durante la semana 10 de Sistemas Distribuidos. Los patrones y listas de validación documentados aquí deben implementarse y probarse antes de afirmarlos como funcionalidades operativas de RedFish.

## Sesión 1: Patrones y consistencia
- **Database per service:** separar la propiedad de los datos por servicio cuando la arquitectura lo requiera.
- **Saga:** coordinar una operación de varios pasos y definir acciones compensatorias cuando uno falla.
- **Outbox:** registrar el cambio de negocio y el evento a publicar dentro de una misma transacción; publicar el evento por un proceso separado y diseñar consumidores idempotentes.
- **CQRS:** separar el modelo de escritura de las proyecciones de lectura cuando el caso de uso lo justifique.
- **Consistencia eventual:** considerar que una proyección o consumidor puede actualizarse después de la transacción inicial.

## Sesión 2: Validación y liberación de MVP 2
- Definir criterios de aceptación claros y pruebas reproducibles.
- Verificar el flujo de pedido e inventario, incluyendo el comportamiento ante fallos.
- Validar la compensación de Saga y la entrega/idempotencia de eventos Outbox si están implementados.
- Usar la misma imagen inmutable entre entornos y cambiar la configuración por entorno.
- Registrar versión, cambios, instrucciones de despliegue y plan de rollback.
- Cerrar la revisión con evidencia y una retrospectiva.

## Lista de comprobación — estado por confirmar
- [ ] Pruebas automatizadas ejecutadas y resultados registrados.
- [ ] Compose saludable y prueba de extremo a extremo documentada.
- [ ] Fallo inyectado y compensación verificada, si Saga está implementado.
- [ ] Publicación Outbox repetible e idempotencia del consumidor verificada, si aplica.
- [ ] Secretos ausentes del repositorio y de las imágenes.
- [ ] Versión, changelog y pasos de despliegue/rollback registrados.

## Relación con RedFish
El plan de MVP 2 contempla persistencia e integración progresiva de los procesos de inventario y pedidos. No debe afirmarse que Saga, Outbox, CQRS o el flujo completo están implementados sin evidencia ejecutable.
