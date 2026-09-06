# Estructura del monolito modular

## Proposito

Este documento define una estructura inicial para organizar RedFish como
monolito modular usando Spring Boot en el backend.

---

## Principio base

El sistema se organiza por modulos o bounded contexts, no por entidades
aisladas.

Un modulo puede contener varias entidades relacionadas con una misma capacidad
de negocio.

Ejemplo:

```text
orders/
├── domain/
│   ├── Pedido
│   ├── Cliente
│   └── DetallePedido
├── application/
├── infrastructure/
└── interfaces/
```

En este ejemplo, `orders` es el modulo. `Pedido`, `Cliente` y `DetallePedido`
son entidades o modelos internos del dominio de pedidos.

---

## Estructura backend propuesta

```text
src/
└── main/
    ├── java/
    │   └── com/redfish/
    │       ├── security/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── production/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── inventory/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── orders/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── dispatches/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       ├── vehicles/
    │       │   ├── domain/
    │       │   ├── application/
    │       │   ├── infrastructure/
    │       │   └── interfaces/
    │       └── reporting/
    │           ├── application/
    │           ├── infrastructure/
    │           └── interfaces/
    └── resources/
```

---

## Responsabilidad de carpetas internas

| Carpeta | Responsabilidad |
|---|---|
| `domain` | Entidades, Value Objects, servicios de dominio, reglas e invariantes. |
| `application` | Casos de uso, servicios de aplicacion y coordinacion del modulo. |
| `infrastructure` | Persistencia, repositorios concretos, adaptadores externos y configuracion tecnica. |
| `interfaces` | Controladores HTTP, DTOs de entrada/salida y adaptadores de interfaz. |

---

## Reglas de dependencia

```text
interfaces -> application -> domain
infrastructure -> application -> domain
```

Reglas:

- `domain` no depende de Spring Boot, controladores, repositorios concretos ni
  base de datos.
- `application` coordina casos de uso y depende del dominio.
- `interfaces` expone operaciones hacia fuera del modulo.
- `infrastructure` implementa persistencia e integraciones.
- Un modulo no debe modificar directamente datos de otro modulo.

---

## Relacion con los bounded contexts

| Bounded context | Modulo sugerido |
|---|---|
| Seguridad y Usuarios | `security` |
| Produccion y Alimentacion | `production` |
| Inventario | `inventory` |
| Pedidos | `orders` |
| Despachos | `dispatches` |
| Vehiculos | `vehicles` |
| Reportes y Consultas | `reporting` |

La estructura final puede cambiar cuando inicie la implementacion, pero debe
mantener la separacion por responsabilidad de negocio.
