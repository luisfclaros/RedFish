# ADR-001: Arquitectura inicial de RedFish

## Estado

Aceptado

## Fecha

2026-09-05

## Contexto

RedFish es un sistema de informacion orientado a apoyar procesos operativos de
una empresa piscicola, incluyendo produccion, alimentacion, inventario, pedidos,
vehiculos y despachos.

El proyecto se encuentra en etapa inicial de analisis y planeacion. Los limites
del dominio aun no han sido validados formalmente mediante analisis de bounded
contexts.

El sistema debe evitar complejidad distribuida innecesaria, pero tambien debe
mantener limites internos claros para que los modulos puedan evolucionar de
forma ordenada.

## Decision

La arquitectura seleccionada para RedFish es un **Monolito Modular**.

La aplicacion se desplegara inicialmente como una sola unidad, mientras su
logica interna se organiza en modulos con responsabilidades delimitadas. Los
modulos iniciales identificados son:

- Produccion.
- Alimentacion.
- Inventario.
- Pedidos.
- Despachos.
- Vehiculos.
- Usuarios.

Cada modulo debera proteger sus responsabilidades y evitar dependencias
innecesarias con otros modulos. La estructura interna podra aplicar principios
de Domain-Driven Design y arquitectura hexagonal cuando la complejidad del
dominio lo justifique.

## Justificacion

El monolito modular se propone porque:

- el proyecto es desarrollado por un solo responsable;
- no existe todavia una necesidad demostrada de despliegue independiente por
  modulo;
- la complejidad distribuida no debe introducirse sin una razon concreta;
- el sistema si necesita limites de dominio claros;
- la separacion modular permite ordenar responsabilidades sin agregar el costo
  operativo de microservicios;
- la extraccion futura de un modulo seguira siendo posible si aparece una
  necesidad real de escalabilidad, propiedad o despliegue independiente.

## Alternativas consideradas

### MVC como arquitectura principal

MVC puede ser util como patron de interaccion o presentacion, pero organizar
todo el sistema alrededor de modelos, vistas y controladores no protege por si
solo los limites del dominio que RedFish necesita.

Por esta razon, MVC no se selecciona como arquitectura principal del sistema.

### Microservicios

Los microservicios pueden aportar despliegue y escalabilidad independiente. Sin
embargo, RedFish no tiene actualmente un requisito demostrado que justifique la
complejidad adicional de:

- transacciones distribuidas;
- fallos de red entre capacidades de negocio;
- descubrimiento de servicios;
- observabilidad distribuida;
- despliegues independientes;
- contratos entre servicios;
- mayor infraestructura.

Por esta razon, los microservicios no se seleccionan en esta etapa.

### Monolito tradicional por capas

Un monolito tradicional por capas seria simple de implementar, pero puede
incentivar que la aplicacion se organice globalmente por capas tecnicas y no por
responsabilidades de negocio.

El monolito modular ofrece una mejor base para separar el dominio sin perder la
simplicidad de una sola unidad desplegable.

## Consecuencias

### Positivas

- Menor complejidad operativa.
- Desarrollo local mas sencillo.
- Una sola unidad de despliegue.
- Posibilidad de usar transacciones de base de datos cuando corresponda.
- Limites explicitos entre modulos de negocio.
- Evolucion mas manejable para un proyecto individual.
- Posibilidad futura de extraer modulos si se justifica.

### Negativas

- Los limites modulares deben cuidarse mediante disciplina de ingenieria.
- Dependencias incorrectas entre modulos pueden convertir el sistema en un
  monolito altamente acoplado.
- Todos los modulos comparten inicialmente el mismo ciclo de despliegue.
- No existe escalado independiente inmediato por modulo.

## Validacion requerida

Antes de que este ADR pase a estado **Aceptado**, Week 2 debe validar:

- bounded contexts;
- mapa de contexto;
- limites reales entre modulos;
- alternativas arquitectonicas;
- acoplamiento entre dominios;
- necesidades de escalabilidad independiente;
- propiedad de datos por modulo.

## Avance de validacion

En `HU-003` se documentan los bounded contexts iniciales, el mapa de contexto y
la propiedad preliminar de datos. Este avance respalda la decision de monolito
modular.

En `HU-004` se formaliza la decision arquitectonica y se documenta el stack
tecnologico inicial:

- Spring Boot para backend.
- Java como lenguaje principal del backend.
- JavaScript para capa cliente o scripts de apoyo.
- MySQL como base de datos relacional inicial.

Con esta validacion, el ADR pasa a estado **Aceptado**.

Si analisis futuros contradicen esta decision, el ADR debera revisarse mediante
una nueva decision arquitectonica.
