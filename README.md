# 🐟 RedFish

Sistema de información orientado a la gestión de los procesos operativos de empresas piscícolas.

RedFish busca centralizar información relacionada con producción, alimentación, inventario, pedidos, despachos, vehículos y usuarios, reduciendo la dispersión de datos y facilitando el control de las operaciones.

---

## 🎯 Problema

Los procesos operativos de una empresa piscícola pueden involucrar información distribuida entre diferentes registros, herramientas o responsables.

Esta dispersión puede dificultar el seguimiento de producción, alimentación, existencias, pedidos y despachos, además de incrementar la posibilidad de inconsistencias o pérdida de información.

RedFish busca proporcionar una plataforma centralizada desde la cual estos procesos puedan gestionarse de manera organizada.

---

## 🎯 Objetivo General

Diseñar e implementar un sistema de información para apoyar la gestión de los procesos de producción, alimentación, inventario, pedidos y despachos de una empresa piscícola, manteniendo una separación clara de responsabilidades entre los diferentes módulos del dominio.

---

# 🧩 Módulos del Sistema

RedFish estará organizado inicialmente alrededor de los siguientes módulos funcionales:

| Módulo          | Responsabilidad general                                         |
| --------------- | --------------------------------------------------------------- |
| 🐟 Producción   | Gestión de información asociada a los procesos productivos.     |
| 🌾 Alimentación | Registro y seguimiento de la alimentación suministrada.         |
| 📦 Inventario   | Gestión de productos, insumos, entradas, salidas y existencias. |
| 🛒 Pedidos      | Registro y seguimiento de pedidos realizados por clientes.      |
| 🚚 Despachos    | Preparación y seguimiento de la salida de pedidos.              |
| 🚛 Vehículos    | Administración de los vehículos utilizados para los despachos.  |
| 👥 Usuarios     | Gestión de usuarios, autenticación, roles y permisos.           |

Los límites, responsabilidades y relaciones entre estos módulos serán refinados progresivamente durante el desarrollo del proyecto.

---

# 🏗️ Arquitectura

RedFish tendrá como arquitectura objetivo un **Monolito Modular**.

La aplicación se desplegará inicialmente como una única unidad, pero su lógica será organizada mediante módulos con responsabilidades claramente delimitadas.

```text
                   ┌──────────────────────┐
                   │       RedFish        │
                   │   Monolito Modular   │
                   │                      │
                   │ ┌──────────────────┐ │
                   │ │    Producción    │ │
                   │ ├──────────────────┤ │
                   │ │   Alimentación   │ │
                   │ ├──────────────────┤ │
                   │ │    Inventario    │ │
                   │ ├──────────────────┤ │
                   │ │     Pedidos      │ │
                   │ ├──────────────────┤ │
                   │ │    Despachos     │ │
                   │ ├──────────────────┤ │
                   │ │    Vehículos     │ │
                   │ ├──────────────────┤ │
                   │ │     Usuarios     │ │
                   │ └──────────────────┘ │
                   └──────────────────────┘
```

El diseño interno de los módulos y las decisiones arquitectónicas adicionales serán documentados progresivamente a medida que avance el proyecto.

---

# 🌿 Estrategia de Ramas

El proyecto utiliza las siguientes ramas principales:

```text
feature/*
    │
    ▼
Develop
    │
    ▼
Qa
    │
    ▼
main
```

### `main`

Representa la versión estable del proyecto.

Solo debe contener cambios que hayan superado el proceso de validación correspondiente.

### `Develop`

Rama utilizada para integrar los cambios y nuevas funcionalidades que se encuentran en desarrollo.

### `Qa`

Rama destinada a pruebas, revisión y aseguramiento de calidad.

Todo cambio candidato a incorporarse a `main` deberá ser validado previamente en esta rama.

### `feature/*`

Ramas utilizadas para desarrollar funcionalidades o cambios específicos antes de su integración en `Develop`.

### Ramas hijas por HU

A partir de **Semana 1 - Sesión 2**, cada historia de usuario deberá mantener trazabilidad mediante ramas hijas en los ambientes de desarrollo y QA.

Ejemplo para `HU-002`:

```text
Develop
└── hu-002-dev
      └── Pull Request → Develop

Qa
└── hu-002-qa
      └── Pull Request → Qa

Qa
└── Pull Request → main

```

Las ramas de ambiente (`Develop` y `Qa`) no deben usarse para desarrollar cambios directamente. La rama `main` recibe cambios promovidos desde `Qa` mediante Pull Request directo.

---

# 🧪 Aseguramiento de Calidad

Cada entregable será evaluado mediante criterios de aceptación definidos previamente.

El proceso general será:

```text
Desarrollo
    │
    ▼
Integración en Develop
    │
    ▼
Validación en Qa
    │
    ├── FAIL ──► Corrección
    │
    └── PASS
         │
         ▼
        main
```

La documentación de QA podrá incluir:

* Criterios de aceptación.
* Casos de prueba.
* Registro de defectos.
* Evidencias.
* Informe de validación.

---

# 📝 Convención de Commits

El proyecto utiliza una convención basada en Conventional Commits.

| Prefijo     | Uso                           |
| ----------- | ----------------------------- |
| `feat:`     | Nueva funcionalidad           |
| `fix:`      | Corrección                    |
| `docs:`     | Documentación                 |
| `test:`     | Pruebas                       |
| `refactor:` | Refactorización               |
| `chore:`    | Configuración o mantenimiento |

Ejemplos:

```text
docs: definir arquitectura inicial de RedFish
docs: agregar criterios de aceptación week 1
test: documentar validación de arquitectura
fix: corregir inconsistencias del README
```

---

# 📚 Documentación

La documentación técnica y de calidad del proyecto se organizará progresivamente dentro del repositorio.

```text
docs/
├── adr/
├── domain/
├── backlog.md
├── Week-01/
│   ├── session-01/
│   └── session-02/
└── Week-02/
    └── session-01/

```

La estructura podrá evolucionar de acuerdo con las necesidades del proyecto.

---

# 🔗 Recursos Relacionados

### Mesa de trabajo

Jira del proyecto RedFish.

### Base de Datos

Repositorio destinado al diseño y documentación de la base de datos de RedFish.

---

# 🚧 Estado del Proyecto

RedFish se encuentra actualmente en etapa de definición y construcción progresiva.

Las decisiones arquitectónicas, requisitos y módulos serán refinados y documentados durante las diferentes sesiones de desarrollo.

---

# 👨‍💻 Autor

**LUIS FERNANDO CLAROS RAMOS**

Proyecto académico — Sistemas Distribuidos.
