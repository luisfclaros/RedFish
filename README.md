# 🐟 RedFish - Sistema de Gestión para Piscícolas
## Descripción del Proyecto
**RedFish** es un sistema de información orientado a la gestión y centralización de los procesos operativos de una empresa piscícola.

El sistema tiene como objetivo apoyar la administración de los procesos relacionados con la **producción, alimentación, inventario, pedidos, vehículos y despachos**, permitiendo registrar, consultar y actualizar la información desde una plataforma centralizada.

La solución busca reducir la dispersión de información, minimizar errores en los procesos operativos y facilitar el acceso a los datos necesarios para la toma de decisiones.
---
# 🎯 Objetivo General

Desarrollar un sistema de información basado en una arquitectura **Modelo-Vista-Controlador (MVC)** que permita centralizar y gestionar la información relacionada con la producción, el inventario y los despachos de una empresa piscícola.
---
# 🏗️ Arquitectura del Sistema
RedFish será desarrollado utilizando el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**.
La implementación de MVC permitirá establecer una separación clara entre:
* La representación y gestión de los datos.
* La lógica encargada de procesar las solicitudes.
* La interfaz mediante la cual los usuarios interactúan con el sistema.
Esta separación permite mejorar la organización, mantenibilidad y escalabilidad del proyecto.

```text
                         USUARIO
                            │
                            ▼
                    ┌───────────────┐
                    │     VISTA     │
                    │  Interfaz de  │
                    │    Usuario    │
                    └───────┬───────┘
                            │
                            ▼
                    ┌───────────────┐
                    │  CONTROLADOR  │
                    │ Procesamiento │
                    │ de solicitudes│
                    └───────┬───────┘
                            │
                            ▼
                    ┌───────────────┐
                    │    MODELO     │
                    │ Lógica y Datos│
                    └───────┬───────┘
                            │
                            ▼
                     ┌─────────────┐
                     │ BASE DE DATOS│
                     └─────────────┘
```

---

# 🧩 Componentes de la Arquitectura MVC

## Modelo

El **Modelo** será responsable de representar las entidades del dominio y gestionar las operaciones relacionadas con la persistencia de la información.

Las principales entidades identificadas inicialmente son:

* Producción.
* Alimentación.
* Inventario.
* Producto.
* Pedido.
* Cliente.
* Despacho.
* Vehículo.
* Usuario.

El Modelo será responsable de gestionar las reglas relacionadas con la información y su interacción con la base de datos.

---

## Vista

La **Vista** será responsable de presentar la información y proporcionar los mecanismos de interacción con los usuarios.

Inicialmente, el sistema requerirá interfaces para:

* Inicio de sesión.
* Panel principal o Dashboard.
* Gestión de producción.
* Control de alimentación.
* Gestión de inventario.
* Gestión de productos.
* Gestión de pedidos.
* Gestión de clientes.
* Gestión de despachos.
* Gestión de vehículos.
* Administración de usuarios.

La interfaz deberá diseñarse teniendo en cuenta criterios de usabilidad, claridad y facilidad de navegación.

---

## Controlador

El **Controlador** será responsable de gestionar las solicitudes generadas desde la interfaz y coordinar la comunicación entre la Vista y el Modelo.

Entre sus principales responsabilidades se encuentran:

* Recibir solicitudes de los usuarios.
* Validar la información recibida.
* Procesar las acciones solicitadas.
* Coordinar la ejecución de la lógica del sistema.
* Consultar y actualizar información.
* Gestionar las respuestas hacia la Vista.

---

# 🔄 Flujo General del Sistema

El flujo de interacción dentro de RedFish seguirá la estructura:

```text
Usuario
   │
   ▼
Vista
   │
   ▼
Controlador
   │
   ▼
Modelo
   │
   ▼
Base de Datos
   │
   ▼
Modelo
   │
   ▼
Controlador
   │
   ▼
Vista
   │
   ▼
Usuario
```

---

# ⚙️ Módulos Funcionales

## 🐟 Módulo de Producción

Permitirá gestionar la información relacionada con los procesos productivos de la piscícola.

Funciones principales:

* Registro de producción.
* Consulta de registros.
* Control de estanques o lotes.
* Registro de cantidades.
* Registro de peso.
* Consulta de historial de producción.

---

## 🌾 Módulo de Alimentación

Permitirá registrar y consultar la información relacionada con la alimentación de los peces.

Funciones principales:

* Registro de alimentación.
* Asociación de alimentación a un estanque o lote.
* Registro del tipo de alimento.
* Registro de cantidades suministradas.
* Consulta del historial de alimentación.

---

## 📦 Módulo de Inventario

Permitirá controlar los productos e insumos disponibles dentro de la organización.

Funciones principales:

* Registro de productos.
* Consulta de existencias.
* Registro de entradas.
* Registro de salidas.
* Control de movimientos.
* Identificación de productos con bajo nivel de inventario.

---

## 🛒 Módulo de Pedidos

Permitirá registrar y gestionar los pedidos realizados por los clientes.

Funciones principales:

* Registro de pedidos.
* Asociación de pedidos a clientes.
* Registro de productos solicitados.
* Control de cantidades.
* Gestión del estado del pedido.
* Preparación de pedidos para despacho.

---

## 🚚 Módulo de Despachos

Permitirá gestionar el proceso de salida y entrega de los productos.

Funciones principales:

* Creación de despachos.
* Asociación de pedidos.
* Asignación de vehículos.
* Registro de información de entrega.
* Seguimiento del estado del despacho.

---

## 🚛 Módulo de Vehículos

Permitirá administrar los vehículos utilizados para el transporte de los productos.

Funciones principales:

* Registro de vehículos.
* Consulta de vehículos.
* Control de disponibilidad.
* Registro de capacidad.
* Gestión del estado del vehículo.
* Asignación a despachos.

---

## 👥 Módulo de Usuarios

Permitirá administrar el acceso de los usuarios al sistema.

Funciones principales:

* Registro de usuarios.
* Inicio de sesión.
* Administración de roles.
* Control de permisos.
* Activación y desactivación de usuarios.

---

# 📊 Dashboard

El sistema contará con un panel principal para facilitar la visualización de información relevante.

El Dashboard podrá presentar indicadores relacionados con:

* Producción registrada.
* Estado del inventario.
* Productos con existencias bajas.
* Pedidos pendientes.
* Despachos programados.
* Vehículos disponibles.

La información presentada deberá estar relacionada con los permisos y responsabilidades del usuario autenticado.

---

# 🔐 Seguridad y Control de Acceso

El sistema deberá implementar mecanismos de autenticación y autorización.

Cada usuario contará con un rol dentro de la plataforma, determinando las funcionalidades y módulos a los cuales podrá acceder.

Roles identificados inicialmente:

* Producción.
* Inventario.
* Despachos.
* Administrador.

El sistema deberá evitar el acceso no autorizado a módulos o funcionalidades restringidas.

---

# 🗄️ Gestión de Datos

La información será almacenada en una base de datos centralizada.

El sistema deberá garantizar:

* Integridad de los datos.
* Validación de información.
* Consistencia entre módulos.
* Control de registros duplicados.
* Disponibilidad de la información para los usuarios autorizados.

Las operaciones realizadas en los módulos estarán relacionadas entre sí. Por ejemplo, los movimientos de inventario podrán estar asociados con procesos de alimentación, producción o despacho.

---

# 🗂️ Estructura Conceptual del Proyecto

```text
redfish/
│
├── src/
│   │
│   ├── models/
│   │   ├── Produccion
│   │   ├── Alimentacion
│   │   ├── Inventario
│   │   ├── Producto
│   │   ├── Pedido
│   │   ├── Cliente
│   │   ├── Despacho
│   │   ├── Vehiculo
│   │   └── Usuario
│   │
│   ├── controllers/
│   │   ├── ProduccionController
│   │   ├── AlimentacionController
│   │   ├── InventarioController
│   │   ├── ProductoController
│   │   ├── PedidoController
│   │   ├── ClienteController
│   │   ├── DespachoController
│   │   ├── VehiculoController
│   │   └── UsuarioController
│   │
│   └── views/
│       ├── Login
│       ├── Dashboard
│       ├── Produccion
│       ├── Alimentacion
│       ├── Inventario
│       ├── Pedidos
│       ├── Despachos
│       ├── Vehiculos
│       └── Usuarios
│
├── database/
│   └── Scripts y configuración de base de datos
│
├── docs/
│   └── Documentación técnica y funcional
│
└── README.md
```

---

# 📋 Requerimientos No Funcionales

El sistema deberá cumplir inicialmente con los siguientes criterios:

## Rendimiento

Las operaciones de consulta y registro deberán ejecutarse de forma eficiente para garantizar una experiencia adecuada para los usuarios.

## Usabilidad

La interfaz deberá ser clara, intuitiva y permitir a los usuarios realizar sus tareas con una cantidad mínima de pasos.

## Seguridad

El sistema deberá implementar mecanismos para proteger la información y restringir el acceso según los roles y permisos definidos.

## Mantenibilidad

La implementación basada en MVC permitirá realizar modificaciones y mejoras manteniendo una separación adecuada de responsabilidades.

## Escalabilidad

La arquitectura deberá permitir la incorporación futura de nuevos módulos y funcionalidades sin requerir una reestructuración completa del sistema.

## Integridad
El sistema deberá implementar validaciones para evitar inconsistencias, registros duplicados y operaciones inválidas.

---

# 🚀 Estado del Proyecto

🟡 **En fase de análisis y diseño.**

Actualmente se encuentra en proceso de:

* Levantamiento de requerimientos.
* Definición de módulos.
* Diseño de procesos.
* Diseño del Mockup.
* Modelado de datos.
* Definición de la arquitectura técnica.

---

# 🐟 RedFish

**Sistema de Gestión de Producción, Inventario y Despachos para Piscícolas.**

Una solución orientada a centralizar la información operativa y mejorar el control de los procesos mediante una arquitectura organizada basada en el patrón **Modelo-Vista-Controlador (MVC)**.
