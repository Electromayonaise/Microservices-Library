# Biblioteca — Microservices Library

Sistema de biblioteca compuesto por 4 microservicios Spring Boot, todos protegidos con JWT emitido por **Keycloak** (OAuth2 Resource Server) y documentados con **Swagger/OpenAPI**.

## Microservicios

| Servicio | Puerto | Responsabilidad | Cliente Keycloak (`azp`) |
|---|---|---|---|
| [`microservicio-circulacion`](microservicio-circulacion/) | `8083` | Préstamo y devolución de libros. Orquesta llamadas a `catalogo-service` y `notificacion-service` vía Feign, propagando el JWT del usuario autenticado. | `circulacion-service` |
| [`microservicio-catalogo`](microservicio-catalogo/) | `8082` | Consulta y actualización del catálogo de libros (disponibilidad, búsqueda). | `catalogo-service` |
| [`microservicio-usuarios`](microservicio-usuarios/) | `8081` | Consulta y actualización de datos de usuarios. | `usuario-service` |
| [`microservicio-notificacion`](microservicio-notificacion/) | `8084` | Envío de notificaciones, invocado internamente por `circulacion-service`. | `notificacion-service` |

Todos corren contra el realm `biblioteca` de Keycloak, expuesto en `http://localhost:8095`.

### Modelo de autorización

- Roles de realm: **`ROLE_LIBRARIAN`** y **`ROLE_USER`**.
- Cada endpoint usa `@PreAuthorize` según el rol requerido (ver el detalle completo, con capturas de Swagger por endpoint, en [docs/swagger-endpoints.md](docs/swagger-endpoints.md)).
- Cada servicio valida no solo el emisor (`iss`) del token sino también el cliente que lo solicitó (claim `azp`), contra una whitelist (`keycloak.allowed-clients`):
  - `circulacion-service` y `usuario-service` solo confían en su propio cliente.
  - `catalogo-service` y `notificacion-service` también confían en `circulacion-service`, porque reciben el token propagado vía Feign cuando `circulacion-service` los llama internamente.

## Requisitos previos

- Docker y Docker Compose
- [Newman](https://www.npmjs.com/package/newman) (CLI de Postman) para correr la colección: `npm install -g newman`

## 1. Levantar Keycloak

Keycloak corre por fuera del `docker-compose.yml` de los microservicios, como contenedor independiente:

**bash / zsh:**

```bash
docker run -p 127.0.0.1:8095:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:26.7.3 start-dev
```

**PowerShell:**

```powershell
docker run -p 127.0.0.1:8095:8080 `
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin `
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin `
  quay.io/keycloak/keycloak:26.7.3 start-dev
```

Queda disponible en `http://localhost:8095` (usuario admin: `admin` / `admin`).

## 2. Importar el realm `biblioteca`

El realm ya configurado (4 clientes confidenciales, roles `ROLE_LIBRARIAN`/`ROLE_USER`) está exportado en [entregables/biblioteca-realm-export.json](entregables/biblioteca-realm-export.json).

1. Entra a la Admin Console: `http://localhost:8095/admin` → login con `admin`/`admin`.
2. En el selector de realms (arriba a la izquierda) → **Create Realm**.
3. En **Resource file**, sube `entregables/biblioteca-realm-export.json`.
4. Click **Create**.

Esto crea el realm `biblioteca` con los 4 clientes (`catalogo-service`, `circulacion-service`, `notificacion-service`, `usuario-service`) y los roles `ROLE_LIBRARIAN` / `ROLE_USER` ya definidos.

> **Nota:** la exportación parcial de Keycloak **no incluye usuarios humanos** (solo las cuentas de servicio internas de cada cliente). Después de importar, crea manualmente los 2 usuarios de prueba en **Users → Add user** (realm `biblioteca`):
>
> | Username | Password | Rol asignado (Role mapping) |
> |---|---|---|
> | `librarian1` | `Librarian@2026` | `ROLE_LIBRARIAN` |
> | `user1` | `User@2026` | `ROLE_USER` |
>
> Al crear la contraseña en la pestaña **Credentials**, desmarca **Temporary** para que no pida cambiarla en el primer login. Estas credenciales son las que ya vienen precargadas como variables en la colección de Postman.

### Obtener los client secrets

Por seguridad, los secretos no viajan en el export (`entregables/biblioteca-realm-export.json` los trae enmascarados). Para cada uno de los 4 clientes:

1. **Clients** → selecciona el cliente (p. ej. `circulacion-service`) → pestaña **Credentials**.
2. Copia el **Client secret**, o dale a **Regenerate** si prefieres uno nuevo.

Necesitarás los 4 secretos (uno por cliente) para el paso de Postman/newman más abajo.

## 3. Levantar los microservicios

Desde la raíz del repo:

```bash
docker compose up -d --build
```

Esto construye y levanta los 4 microservicios (`docker-compose.yml`), cada uno configurado para alcanzar Keycloak en `host.docker.internal:8095`. Verifica que arrancaron bien:

```bash
curl http://localhost:8081/v3/api-docs   # usuario-service
curl http://localhost:8082/v3/api-docs   # catalogo-service
curl http://localhost:8083/v3/api-docs   # circulacion-service
curl http://localhost:8084/v3/api-docs   # notificacion-service
```

Swagger UI de cada uno queda en `http://localhost:808{1,2,3,4}/swagger-ui.html` (capturas en [docs/swagger-screenshots/](docs/swagger-screenshots/)).

Para bajar la stack: `docker compose down`.

## 4. Correr la colección de Postman con newman

La colección [entregables/Biblioteca-Keycloak.postman_collection.json](entregables/Biblioteca-Keycloak.postman_collection.json) obtiene tokens contra cada uno de los 4 clientes y prueba, por servicio: acceso con el rol correcto (200), acceso con rol incorrecto (403) y token inválido/malformado (401).

Los secretos se pasan como variables de entorno de newman (nunca se escriben en el archivo de la colección):

**bash / zsh:**

```bash
newman run entregables/Biblioteca-Keycloak.postman_collection.json \
  --env-var client_secret=<SECRET_CIRCULACION> \
  --env-var client_secret_catalogo=<SECRET_CATALOGO> \
  --env-var client_secret_usuarios=<SECRET_USUARIO> \
  --env-var client_secret_notificacion=<SECRET_NOTIFICACION>
```

**PowerShell:**

```powershell
newman run entregables/Biblioteca-Keycloak.postman_collection.json `
  --env-var client_secret=<SECRET_CIRCULACION> `
  --env-var client_secret_catalogo=<SECRET_CATALOGO> `
  --env-var client_secret_usuarios=<SECRET_USUARIO> `
  --env-var client_secret_notificacion=<SECRET_NOTIFICACION>
```

Debe terminar con **22/22 assertions** en verde. El ítem "Token inválido/malformado" incluye en su descripción instrucciones adicionales para simular un token expirado (bajando temporalmente el *Access Token Lifespan* del realm en Keycloak).

## Estructura del repositorio

```
microservicio-catalogo/       # Spring Boot service (puerto 8082)
microservicio-circulacion/    # Spring Boot service (puerto 8083)
microservicio-notificacion/   # Spring Boot service (puerto 8084)
microservicio-usuarios/       # Spring Boot service (puerto 8081)
docker-compose.yml            # Orquesta los 4 microservicios
entregables/                  # Export del realm de Keycloak + colección Postman
docs/
  swagger-screenshots/        # Capturas generales de Swagger UI por servicio
  swagger-screenshots/detail/ # Capturas al detalle de cada endpoint
  swagger-endpoints.md        # Documentación de cada endpoint con su captura
```
