FROM gradle:8.4-jdk21-alpine AS builder
WORKDIR /home/gradle/project
COPY build.gradle settings.gradle gradlew gradle /home/gradle/project/
RUN gradle --no-daemon build -x test || return 0
COPY . /home/gradle/project
RUN gradle --no-daemon clean bootJar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
