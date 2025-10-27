# Multi-stage Dockerfile para construir y ejecutar la aplicación Spring Boot
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar archivos de build y fuentes
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/product-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
