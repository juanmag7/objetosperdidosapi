# Objetos Perdidos y Encontrados — Documentación de la API

**Tecnologías:** Java 21, Spring Boot 4.1.1, Spring Web, Spring Data JPA, Lombok, PostgreSQL.

**Base URL local:** `http://localhost:8080`.

## Configuración

1. Crear la base de datos PostgreSQL `objetos_perdidos` (la configuración incluida usa el puerto `5433`).
2. En PowerShell y sin compartir la contraseña: `$env:DB_PASSWORD = "TU_CONTRASEÑA"`.
3. Ejecutar `./mvnw.cmd clean test` y `./mvnw.cmd spring-boot:run`.
4. Configuraciones opcionales: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
5. `spring.jpa.hibernate.ddl-auto=update` crea/actualiza tablas para desarrollo; no carga usuarios ni hallazgos automáticamente.

## Contrato de endpoints

| Método | URL | Body / parámetros | Resultado principal |
|---|---|---|---|
| GET | `/api/estado` | Ninguno | 200, estado del servicio |
| GET | `/api/objetos` | Ninguno | 200, lista de hallazgos `PUBLICADO` |
| GET | `/api/objetos/{id}` | Ninguno | 200 si `PUBLICADO`, si no 404 |
| PUT | `/api/hallazgos/{id}` | JSON `ActualizarHallazgoRequest` | 200; solo `REGISTRADO` y rol SEGURIDAD |
| PATCH | `/api/hallazgos/{id}/entregar` | JSON `EntregarHallazgoRequest` | 200; exige reclamación APROBADA y objeto PUBLICADO; cierra reclamaciones PENDIENTES |
| POST | `/api/reclamaciones` | JSON `CrearReclamacionRequest` | 201; solo usuarios COMUNIDAD activos y hallazgos PUBLICADOS |
| GET | `/api/reclamaciones/{id}?usuarioId={usuarioId}` | Query `usuarioId` | 200; propietario o SEGURIDAD (comprobación por ID), 403, 404 |
| PATCH | `/api/reclamaciones/{id}/decision` | JSON `RegistrarDecisionRequest` | 200; seguridad activa, pendiente; registra auditoría |
| DELETE | `/api/reclamaciones/{id}?reclamanteId={usuarioId}` | Query `reclamanteId` | 204; retirada lógica, solo propietario, estado PENDIENTE |

### PUT `/api/hallazgos/1`

```json
{
  "titulo": "Llaves encontradas",
  "categoria": "ACCESORIOS",
  "descripcionPublica": "Juego de llaves con llavero azul",
  "lugarHallazgo": "Biblioteca",
  "fechaHallazgo": "2026-11-12T15:00:00",
  "usuarioSeguridadId": 2
}
```

**Requisitos:** hallazgo existente en estado `REGISTRADO`; `usuarioSeguridadId` debe existir, estar activo y tener rol `SEGURIDAD`. Campos obligatorios; longitudes máximas: título 120, categoría 60, lugar 160, descripción 10000. Un hallazgo publicado no se puede editar con este endpoint.

### PATCH `/api/hallazgos/4/entregar`

```json
{
  "reclamacionAprobadaId": 1,
  "usuarioSeguridadId": 2
}
```

**Requisitos:** reclamación APROBADA del mismo hallazgo; hallazgo `PUBLICADO`; usuario `SEGURIDAD` activo. El estado del hallazgo queda `ENTREGADO`; las otras reclamaciones `PENDIENTE` se cambian a `CERRADA` dentro de una transacción.

### POST `/api/reclamaciones`

```json
{
  "hallazgoId": 4,
  "reclamanteId": 1,
  "evidenciaPrivada": "Identifico el número de serie y una marca interna"
}
```

**Requisitos:** reclamante COMUNIDAD activo; hallazgo `PUBLICADO`; evidencia no vacía (máximo 10000 caracteres). **La evidencia se guarda en la base de datos pero NO se retorna en los DTO públicos.**

### GET `/api/reclamaciones/1?usuarioId=1`

Sin JSON. `usuarioId` identifica un usuario que debe ser el reclamante asociado o de rol `SEGURIDAD`.

### PATCH `/api/reclamaciones/1/decision`

```json
{
  "decision": "APROBADA",
  "decididoPorId": 2,
  "observacion": "Se confirmó el dato de verificación"
}
```

`decision` admite `APROBADA` o `RECHAZADA`. Solo una decisión por reclamación `PENDIENTE`. Se persiste en `decisiones_reclamacion`.

### DELETE `/api/reclamaciones/2?reclamanteId=1`

Sin JSON. **No borra físicamente**: cambia `PENDIENTE` a `RETIRADA` y retorna 204 sin cuerpo. Si ya existe decisión o retiro previo, retorna 409.

## Errores frecuentes

- **400 Bad Request:** datos ausentes/inválidos o JSON incompatible (enums y fechas).
- **403 Forbidden:** rol, actividad o propiedad no admitidos para el ID indicado.
- **404 Not Found:** identificador inexistente o hallazgo no publicado en la consulta pública.
- **409 Conflict:** operación incompatible con el estado actual (ya decidido, ya entregado, retiro inválido, etc.).

## Privacidad y limitaciones de seguridad

- Los DTO de objetos omiten `descripcionVerificacionPrivada`.
- Los DTO de reclamaciones omiten `evidenciaPrivada`.
- **No hay autenticación real ni sesiones/JWT implementadas.** Los IDs de usuario en la solicitud son autodeclarados y se pueden suplantar. La comprobación de roles y propiedad es solo una validación de reglas de negocio, **no** un control de acceso seguro para producción.
- Para producción: usar Spring Security con identidad autenticada, derivar el usuario del token y restringir los endpoints privados.

## Datos de ejemplo

Los identificadores `1`, `2` y `4` son ilustrativos y deben ajustarse a los datos realmente existentes en PostgreSQL. El proyecto no incluye un seeder automático. No ejecutar un PUT sobre un hallazgo `PUBLICADO` ni entregar un hallazgo con una reclamación de otro objeto.
