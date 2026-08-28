# 🐟 RedFish | Rama de Desarrollo

> 🚧 **Esta es la rama principal de desarrollo activo del proyecto RedFish.**

## 📌 Acerca de `develop`

La rama **`develop`** es utilizada como el entorno principal para la integración de nuevas funcionalidades y cambios en el proyecto.

En esta rama se realiza el desarrollo continuo de los módulos que conforman el sistema **RedFish – Gestión de Producción, Inventario y Despachos para Piscícolas**.

Los cambios implementados en las diferentes funcionalidades son integrados y evaluados en esta rama antes de ser preparados para pruebas y posteriormente incorporados a la versión estable del sistema.

---

# 🎯 Propósito de la Rama

La rama `develop` tiene como objetivo centralizar el desarrollo de las nuevas funcionalidades del sistema.

En esta rama se integrarán los avances relacionados con:

* 🐟 Gestión de producción.
* 🌾 Control de alimentación.
* 📦 Gestión de inventario.
* 🛒 Gestión de pedidos.
* 🚚 Gestión de despachos.
* 🚛 Gestión de vehículos.
* 👥 Gestión de usuarios.
* 📊 Dashboard del sistema.

---

# 🌿 Estrategia de Ramas

El proyecto utiliza una estrategia de desarrollo basada en diferentes ramas para mantener un flujo de trabajo organizado.

```text
main
 │
 └── qa
      │
      └── develop
           │
           ├── feature/produccion
           ├── feature/alimentacion
           ├── feature/inventario
           ├── feature/pedidos
           ├── feature/despachos
           └── feature/vehiculos
```

## 🔵 `main`

Representa la versión estable del proyecto.

Solo contiene funcionalidades que han sido desarrolladas, integradas y validadas.

---

## 🟢 `develop`

Representa la rama principal de desarrollo.

Aquí se integran las nuevas funcionalidades antes de ser enviadas al entorno de pruebas.

---

## 🟡 `qa`

Representa la rama destinada a la validación y pruebas del sistema.

Las funcionalidades desarrolladas en `develop` deberán ser evaluadas antes de ser incorporadas a la rama `main`.

---

## 🟣 `feature/*`

Las nuevas funcionalidades deberán desarrollarse en ramas independientes.

Ejemplo:

```text
feature/produccion
feature/inventario
feature/pedidos
feature/despachos
```

Una vez finalizada una funcionalidad, esta podrá integrarse en la rama `develop`.

---

# 🏗️ Arquitectura del Proyecto

RedFish será desarrollado utilizando el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**.

La arquitectura permitirá separar las responsabilidades del sistema en tres componentes principales:

* **Modelo:** Gestión de datos y entidades del sistema.
* **Vista:** Interfaces e interacción con los usuarios.
* **Controlador:** Procesamiento de solicitudes y coordinación entre la Vista y el Modelo.

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
```

---

# ⚙️ Estado Actual del Desarrollo

🚧 **Proyecto en desarrollo activo**

Actualmente, esta rama es utilizada para la implementación progresiva de:

* Análisis de requerimientos.
* Diseño del sistema.
* Diseño del modelo de datos.
* Desarrollo del mockup.
* Implementación de módulos.
* Integración de funcionalidades.

> ⚠️ Los cambios presentes en esta rama pueden estar en proceso de desarrollo y no representan necesariamente una versión estable del sistema.

---

# 📋 Flujo de Trabajo Recomendado

Para desarrollar una nueva funcionalidad:

### 1️⃣ Actualizar la rama `develop`

```bash
git checkout develop
git pull origin develop
```

### 2️⃣ Crear una nueva rama

```bash
git checkout -b feature/nombre-funcionalidad
```

Ejemplo:

```bash
git checkout -b feature/inventario
```

### 3️⃣ Realizar el desarrollo

Implementar los cambios correspondientes a la funcionalidad.

### 4️⃣ Registrar los cambios

```bash
git add .
git commit -m "feat: implementar modulo de inventario"
```

### 5️⃣ Subir la rama

```bash
git push origin feature/inventario
```

### 6️⃣ Integrar en `develop`

Una vez validada la funcionalidad, se realizará la integración correspondiente mediante un Pull Request.

---

# 📝 Convención de Commits

El proyecto utilizará una estructura de commits basada en **Conventional Commits**.

| Prefijo     | Descripción                              |
| ----------- | ---------------------------------------- |
| `feat:`     | Nueva funcionalidad                      |
| `fix:`      | Corrección de errores                    |
| `docs:`     | Cambios en documentación                 |
| `style:`    | Cambios de estilos                       |
| `refactor:` | Reestructuración del código              |
| `test:`     | Implementación o modificación de pruebas |
| `chore:`    | Configuración o tareas generales         |

### Ejemplos

```text
feat: implementar registro de producción

feat: agregar control de inventario

fix: corregir validación de existencias

docs: actualizar requerimientos funcionales

refactor: reorganizar estructura MVC
```

---

# 🚀 RedFish

**Sistema de Gestión de Producción, Inventario y Despachos para Piscícolas.**

Esta rama representa el entorno principal de desarrollo e integración de las nuevas funcionalidades del proyecto.

> 🐟 **RedFish | Tecnología para centralizar y optimizar la gestión de las operaciones piscícolas.**

---

## ⚠️ Importante

Esta rama puede contener funcionalidades en desarrollo, cambios experimentales o componentes pendientes de validación.

Para consultar la versión estable del sistema, se debe utilizar la rama:

```text
main
```

