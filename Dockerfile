FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:25-jre AS demo
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/target/*.jar release-tracker-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=demo", "-jar", "release-tracker-api.jar"]
