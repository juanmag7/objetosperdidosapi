# Lista de verificación de entrega

## 1. No perder trabajo ni ramas

1. Mantener una copia del ZIP original.
2. Esta versión corregida NO incluye `.git`: **no** borrar el `.git` de la carpeta existente.
3. Copiar `src`, `pom.xml` y `docs` encima del proyecto existente; conservar `.git` y `mvnw*`.
4. Abrir PowerShell en el directorio del repositorio y confirmar `git branch --show-current` (`feature/lombok` en el ZIP recibido).

## 2. Pruebas locales (obligatorias)

```powershell
$env:DB_PASSWORD = "TU_CONTRASEÑA_LOCAL"
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

No compartir el password ni subirlo a GitHub. Si `clean test` falla, revisar el **primer error** y corregirlo antes de entregar; el ZIP no sustituye la prueba en tu máquina.

Desde otra terminal PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/api/estado
Invoke-RestMethod http://localhost:8080/api/objetos
```

Luego probar en Postman, con IDs realmente existentes:

- GET objeto PUBLICADO: 200; otro no publicado: 404.
- POST reclamación sobre PUBLICADO: 201; sobre ENTREGADO: 409; con SEGURIDAD como reclamante: 403.
- GET reclamación con propio ID: 200; con otro usuario COMUNIDAD: 403; sin `usuarioId`: 400.
- PATCH decisión con SEGURIDAD: 200; repetir la misma decisión: 409.
- DELETE reclamación recién creada PENDIENTE: 204; repetir: 409; con ID de otro usuario: 403.
- PUT hallazgo REGISTRADO con SEGURIDAD: 200; intentar editar PUBLICADO: 409.
- PATCH entregar usando reclamación aprobada del mismo hallazgo: 200; comprobar `ENTREGADO` en la tabla `hallazgos` y `CERRADA` en reclamaciones pendientes.
- Después de entregar, GET `/api/objetos/{id}`: 404 (listado público solo PUBLICADOS); POST nueva reclamación: 409.
- Verificar que ningún GET público incluya `evidenciaPrivada` ni `descripcionVerificacionPrivada`.

## 3. GitHub

Sin comprometer los cambios de otra persona ni hacer merge sin autorización de la profesora:

```powershell
git branch --show-current
git status -sb
git add src pom.xml docs
git commit -m "fix: corrige servicios, controladores y documenta API"
git push origin feature/lombok
```

**Antes de los comandos**, confirmar que estás en la rama `feature/lombok`; si no lo estás, no ejecutar `git push` hasta revisar las ramas. La PR de esta rama debe apuntar a `develop`, sujeto a aprobación de `@cataUAM` según CODEOWNERS. No hacer merge a `develop`/`main` sin esa aprobación.

**Compañero Miguel:** subir su documentación SQL/diagrama mediante su propia rama `feature/documentacion-modelo-datos`, un commit propio y PR a `develop`. Coordinar para no sobrescribir las carpetas `docs` de ambos: revisar y resolver conflictos de nombres si los hay.

## 4. Correo de entrega

**Asunto propuesto:** `Entrega proyecto Backend — Objetos Perdidos y Encontrados — [integrantes]`

**Cuerpo:**

Profesora, buenas noches.

Adjuntamos la documentación de los endpoints de nuestro proyecto **Objetos Perdidos y Encontrados** y compartimos el repositorio de GitHub:

https://github.com/juanmag7/objetosperdidosapi

En el archivo `docs/ENDPOINTS.md` se describen los métodos HTTP, las rutas, los cuerpos JSON de las solicitudes y los resultados esperados.

El proyecto fue trabajado en ramas con aportes de ambos integrantes; las solicitudes de cambio se encuentran disponibles para su revisión y aprobación.

Integrantes: [NOMBRE 1] y [NOMBRE 2].

Muchas gracias.

**Nota:** No afirmar que todo pasó las pruebas si no se comprobó realmente. Adjuntar `docs/ENDPOINTS.md` o convertirlo en PDF solo si la profesora exige archivo formal; también se puede enviar como documento del repositorio.
