# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy gradle wrapper and config files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY config config

RUN chmod +x ./gradlew

# Copy source code and build jar without running tests
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
