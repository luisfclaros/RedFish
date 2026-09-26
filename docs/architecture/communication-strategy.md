# Estrategia de comunicacion de RedFish

## 1. Contexto

RedFish es un monolito modular: sus modulos se despliegan juntos, pero sus
responsabilidades y contratos permanecen separados. Por esta razon, la
tecnologia se elige por tipo de interaccion y no se obliga a los modulos a
comunicarse por red cuando viven en el mismo proceso.

## 2. Matriz de decisiones

| Interaccion | Modo | Tecnologia | Justificacion |
|---|---|---|---|
| Postman o frontend hacia RedFish | Sincrono | REST/JSON sobre HTTP | Es legible, interoperable y cuenta con buen soporte de herramientas. |
| Caso de uso entre modulos que necesita respuesta inmediata | Sincrono | Puerto Java de aplicacion | Evita latencia de red y conserva un contrato explicito entre modulos. |
| Notificacion de un hecho ya ocurrido | Asincrono | Eventos de aplicacion de Spring | Reduce el acoplamiento y permite agregar consumidores sin cambiar al productor. |
| Servicio interno futuro de alto rendimiento | Sincrono | gRPC, sujeto a ADR | Solo se justificara si RedFish se divide en servicios desplegables. |
| Integracion futura durable o entre procesos | Asincrono | RabbitMQ o Kafka, sujeto a ADR | Se incorporara cuando se requieran reintentos, retencion o consumidores externos. |

## 3. Interaccion implementada en HU-013

```text
POST /api/products
        |
        v
CreateProductService
        |
        +--> ProductRepositoryPort --> MySQL
        |
        +--> ProductCreatedEventPublisher
                  |
                  v
            ProductCreatedEvent
                  |
                  v
        ProductCreatedEventConsumer
                  |
                  v
       eventos_producto_procesados
```

El evento contiene:

- `eventId`: UUID usado como clave de idempotencia;
- `productId`: identificador del producto creado;
- `productCode`: codigo de negocio del producto;
- `occurredAt`: fecha y hora UTC del hecho.

## 4. Idempotencia

El consumidor intenta registrar el evento en
`eventos_producto_procesados`. `event_id` es la llave primaria, por lo que un
evento entregado nuevamente no crea un segundo registro. El adaptador captura
la violacion de unicidad y devuelve `false`; el consumidor reconoce el
duplicado y termina sin repetir el procesamiento.

Esta estrategia implementa procesamiento idempotente para la entrega repetida
del mismo `eventId`. Spring publica el evento dentro del proceso actual. Si en
el futuro se incorpora un broker, la clave y el registro de deduplicacion se
conservan.

## 5. Limites de la solucion actual

- Los eventos de Spring no sobreviven a la caida del proceso.
- No existe reintento automatico ni cola de mensajes.
- La atomicidad actual depende de una transaccion local y de que el evento de
  Spring se procese de forma sincrona dentro de la misma aplicacion.

Cuando RedFish necesite entrega asincrona durable se evaluara el patron Outbox
junto con RabbitMQ o Kafka. Esa evolucion no cambia el contrato conceptual del
evento, pero reemplazara la transaccion local por publicacion confiable.
