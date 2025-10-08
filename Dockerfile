FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /app

COPY . .

RUN gradle clean bootJar -x test

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
