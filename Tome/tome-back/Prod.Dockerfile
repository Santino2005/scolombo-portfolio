# Etapa 1: Construcción
FROM gradle:8.8-jdk21 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre

COPY --from=build /home/gradle/src/build/libs/tome-backend-0.0.1-SNAPSHOT.jar /app/TOMEBack.jar

WORKDIR /app
EXPOSE ${API_PORT}

ENTRYPOINT ["java", "-jar", "/app/TOMEBack.jar"]
