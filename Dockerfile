FROM maven:3.9-eclipse-temurin-23-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install

FROM eclipse-temurin:23-alpine

WORKDIR /app

COPY --from=build /app/target/brokerage-firm-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]