FROM maven:3.9.3-eclipse-temurin-20 AS build
WORKDIR /app
COPY usr1/pom.xml .
COPY usr1/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:20-jre-alpine
WORKDIR /app
COPY --from=build /app/target/usr1-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]