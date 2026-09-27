# Politica de versionado y compatibilidad de API

## 1. Version actual

El contrato REST vigente de RedFish es `v1` y utiliza rutas con este prefijo:

```text
/api/v1
```

La version funcional del contrato se publica como `1.0.0` dentro de
`openapi-v1.yaml`. El numero de la ruta solo cambia cuando existe una ruptura
incompatible para los consumidores.

## 2. Fuente de verdad

El contrato legible por herramientas se encuentra en:

```text
RedFish/src/main/resources/static/openapi-v1.yaml
```

Al ejecutar la aplicacion tambien puede consultarse en:

```text
GET /openapi-v1.yaml
```

Swagger UI utiliza este archivo directamente. Si una operacion o un campo no
esta publicado en el contrato, no se considera parte de la API soportada.

## 3. Cambios compatibles

Pueden realizarse dentro de `v1`:

- agregar un campo opcional;
- agregar un endpoint nuevo;
- agregar un valor opcional que no invalide consumidores existentes;
- ampliar documentacion o ejemplos;
- corregir implementaciones sin cambiar solicitud, respuesta ni semantica.

Todo campo nuevo debe ser opcional hasta comprobar que los consumidores puedan
enviarlo o recibirlo.

## 4. Cambios incompatibles

Requieren una nueva version, por ejemplo `/api/v2`:

- eliminar un endpoint o campo;
- renombrar un campo;
- cambiar el tipo o significado de un campo;
- convertir un campo opcional en obligatorio;
- cambiar un codigo HTTP esperado;
- modificar una estructura de respuesta de forma incompatible.

La version anterior debe mantenerse durante un periodo de migracion.

## 5. Deprecacion

Las rutas originales `/api/products` y `/api/customers` permanecen disponibles
como aliases temporales. Sus respuestas incluyen:

```text
Deprecation: true
Sunset: Wed, 30 Jun 2027 23:59:59 GMT
Link: </api/v1/...>; rel="successor-version"
```

Los clientes nuevos deben utilizar exclusivamente `/api/v1`. Antes de retirar
una version se debe anunciar su fecha, verificar la migracion de consumidores y
publicar la alternativa.

## 6. Convenciones del contrato

- JSON como formato de intercambio.
- Fechas y horas en UTC con ISO-8601.
- Identificadores internos numericos en el MVP actual.
- UUID para `trace_id` y claves de idempotencia.
- Errores con `code`, `message`, `details` y `trace_id`.
- Encabezado `X-Trace-Id` para correlacion de errores.

## 7. Verificacion

Pact valida las expectativas del consumidor `redfish-web-client` frente al
proveedor `redfish-api`. La suite contiene:

- una prueba consumidora que genera el pacto V4;
- un pacto versionado dentro de los recursos de prueba;
- una prueba proveedora ejecutada contra `ProductController` mediante MockMvc;
- un workflow de GitHub Actions que ejecuta `gradlew test` en cada Pull Request.

Un cambio incompatible en la respuesta de productos provoca un fallo antes de
que el cambio pueda promoverse a QA.
