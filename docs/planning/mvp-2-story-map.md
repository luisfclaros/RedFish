# Story map del MVP 2 de RedFish

## 1. Proposito

Este mapa organiza el trabajo por el recorrido de un operador que recibe y
registra un pedido. La prioridad se lee de arriba hacia abajo y el recorrido de
izquierda a derecha. La linea de liberacion separa el minimo flujo util del
trabajo que puede esperar.

El MVP 2 no intenta completar todos los modulos de RedFish. Su objetivo es
demostrar comunicacion integrada y persistencia confiable mediante un flujo de
Pedidos e Inventario ejecutable de extremo a extremo.

---

## 2. Persona y resultado esperado

**Persona principal:** operador comercial de la empresa piscicola.

**Necesidad:** registrar un pedido para un cliente sin vender una cantidad que
no se encuentra disponible.

**Resultado observable:** el pedido y sus detalles quedan persistidos, el stock
se reserva una sola vez y el pedido puede consultarse posteriormente.

---

## 3. Backbone del recorrido

```text
Preparar datos       Controlar stock       Registrar pedido
      |                     |                     |
      v                     v                     v
Cliente y producto -> Existencia disponible -> Pedido y detalles
                                                   |
                                                   v
                                        Reservar sin duplicar
                                                   |
                                                   v
                                          Consultar resultado
                                                   |
                                                   v
                                           Validar y liberar
```

---

## 4. Mapa de historias

| Prioridad | Preparar datos | Controlar stock | Registrar pedido | Proteger integracion | Operar y liberar |
|---|---|---|---|---|---|
| Base completada | Productos y clientes REST/JPA | Puertos y modelos de inventario | Modelos, servicio y puertos de pedidos | Eventos locales, OpenAPI y Pact | Compose, ambientes y CI |
| Must | HU-017: migraciones versionadas | HU-018: existencias y movimientos | HU-019: pedidos y detalles persistentes | HU-020: reserva transaccional | HU-022: validacion y liberacion MVP 2 |
| Must |  |  |  | HU-021: reintentos idempotentes |  |
| Should |  | Alertas de stock bajo |  |  | HU-023: cliente web inicial |
| Should |  |  | Cancelacion controlada |  | Flujo inicial de despachos |
| Could |  | Proyecciones de inventario |  | Mensajeria durable con Outbox | Reportes operativos |
| Won't now | Autenticacion completa | Pronostico de demanda | Pagos | Broker externo y gRPC | Aplicacion movil |

---

## 5. Linea de liberacion

La linea de liberacion del MVP 2 incluye exclusivamente las historias `Must`:

```text
HU-017 -> HU-018 -> HU-019 -> HU-020 -> HU-021 -> HU-022
```

El flujo minimo demostrable sera:

1. preparar el esquema mediante migraciones versionadas;
2. registrar una entrada de inventario;
3. consultar la existencia disponible;
4. crear un pedido para un cliente existente;
5. reservar la cantidad solicitada dentro de una transaccion;
6. repetir la solicitud sin descontar dos veces;
7. consultar el mismo pedido y su detalle;
8. validar la imagen en Develop y QA;
9. liberar el MVP completo desde `Qa` hacia `main`.

Ninguna historia `Should` o `Could` bloquea esta liberacion.

---

## 6. Rebanada vertical

La rebanada no se divide por capas tecnicas. Cada historia funcional debe
atravesar solamente las capas necesarias para entregar comportamiento
verificable:

```text
REST v1
  -> caso de uso
    -> contrato entre modulos
      -> dominio
        -> persistencia MySQL
          -> pruebas y observabilidad
```

Por ejemplo, HU-018 no se considera terminada por crear una entidad JPA. Debe
permitir registrar un movimiento, proteger el stock no negativo, persistirlo y
consultar la existencia mediante un contrato probado.

---

## 7. Capacidades ya disponibles

Las siguientes capacidades soportan el mapa y no se estiman nuevamente:

- arquitectura de monolito modular;
- modelos de dominio y puertos iniciales;
- Productos y Clientes persistidos en MySQL;
- API versionada `/api/v1`;
- contrato OpenAPI y pruebas Pact;
- sobre estandar de errores y trazas;
- Docker Compose para Develop, QA y produccion local;
- eventos locales con consumidor idempotente;
- integracion continua y proceso QA.

---

## 8. Trabajo fuera de la linea

El frontend se mantiene como `Should`. Puede iniciar con contratos y mocks una
vez que HU-019 estabilice la API de pedidos, pero no desplaza las historias
`Must` si el limite WIP ya esta ocupado.

Despachos, Vehiculos, Produccion, autenticacion completa y reportes avanzados
permanecen visibles en el mapa, pero no forman parte del compromiso del MVP 2.
Esto reduce el riesgo de entregar muchos modulos parcialmente funcionales.

---

## 9. Criterio de exito del mapa

El story map es valido si:

- muestra un recorrido completo y no una lista aislada de componentes;
- diferencia trabajo completado, comprometido y futuro;
- conserva una linea de liberacion pequena;
- cada dependencia critica tiene una historia y un orden;
- el alcance puede reducirse retirando `Should` o `Could` sin romper el flujo.
