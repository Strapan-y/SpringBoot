# Despliegue en Railway (desde Git)

Este proyecto Spring Boot (Maven / Java 21) puede desplegarse en Railway conectando tu repositorio y usando el `Dockerfile` incluido o el builder de Maven de Railway.

Opciones recomendadas:

1) Deploy con Dockerfile (recomendado — control total)

- Railway detectará el `Dockerfile` en el repo y construirá la imagen. El Dockerfile incluido hace un build con Maven en etapa de construcción y empaqueta el JAR en una imagen con JRE 21.

Pasos (interfaz web de Railway):

- Conecta tu repositorio Git (GitHub/GitLab/Bitbucket) desde Railway.
- Crea un nuevo proyecto y selecciona "Deploy from Git" apuntando a la rama `main`.
- Asegúrate de que Railway use el `Dockerfile` (debe detectarlo automáticamente).
- Si necesitas una base de datos, agrega el plugin Postgres desde Railway (Add Plugin -> Postgres).
- Configura variables de entorno (Environment Variables). Railway te proveerá valores para la base de datos; mapea a las propiedades de Spring Boot:

  - SPRING_DATASOURCE_URL = jdbc:postgresql://<HOST>:<PORT>/<DB>
  - SPRING_DATASOURCE_USERNAME = <USER>
  - SPRING_DATASOURCE_PASSWORD = <PASSWORD>

  Nota: Railway suele exponer una variable `DATABASE_URL` con formato `postgres://user:pass@host:port/dbname`. Puedes construir la `SPRING_DATASOURCE_URL` a partir de ella o establecer las tres variables anteriores manualmente.

2) Deploy usando detección Maven (sin Dockerfile)

- Railway puede detectar proyectos Java/Maven y ejecutar `mvn package`. Si prefieres esto, simplemente conecta el repo y deja que Railway use el build command predeterminado (o pon `mvn -DskipTests package`).

Comandos útiles (Railway CLI, opcional):

```powershell
# Login
railway login

# Inicializar un proyecto en directorio actual (opcional)
railway init

# Desplegar localmente (sube y despliega)
railway up
```

Notas y recomendaciones:

- Variables de entorno: usa las mismas claves que Spring espera (`SPRING_DATASOURCE_*`) para evitar cambiar código.
- Puerto: la app expone 8080 por defecto; Railway suele mapear internamente la ruta pública a tu servicio. Si necesitas usar el puerto que Railway proporciona dinámicamente, puedes leer `PORT` y pasarlo como `--server.port=${PORT}` en el ENTRYPOINT o como variable JVM en el comando de inicio.
- Si quieres que la app use el `PORT` provisto por Railway, modifica el `Dockerfile` o la variable de ejecución así: `ENTRYPOINT ["sh","-c","java -jar /app/app.jar --server.port=$PORT"]` y añade `PORT` en env vars.
- Si ves problemas con agentes (Mockito warnings), son advertencias no bloqueantes — no afectan al despliegue.

Si quieres, puedo:

- Añadir soporte para que la imagen use la variable `PORT` de Railway automáticamente. (p. ej. cambiar ENTRYPOINT para respetar $PORT)
- Crear un pequeño script `railway-deploy.md` con pasos exactos para conectar GitHub y asignar variables.

