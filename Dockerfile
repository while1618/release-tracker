FROM maven:sapmachine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre AS demo
WORKDIR /app
COPY --from=build /app/target/*.jar release-tracker-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=demo", "-jar", "release-tracker-api.jar"]
