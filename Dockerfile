# Build com Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copia o pom primeiro para aproveitar o cache de camadas do Docker
COPY pom.xml pom.xml
RUN mvn dependency:go-offline -q

COPY src src
RUN mvn clean package -DskipTests

# Runtime enxuto (apenas JRE)
FROM eclipse-temurin:17-jre

LABEL maintainer="Grupo VitalLink — FIAP"
LABEL description="VitalLink API — Agendamento de consultas médicas"

# Criar usuário dedicado
RUN groupadd --system appgroup && \
    useradd  --system --gid appgroup --no-create-home appuser

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]