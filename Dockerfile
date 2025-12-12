FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mkdir -p /root/.m2 && echo '\
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 \
          https://maven.apache.org/xsd/settings-1.0.0.xsd"> \
  <mirrors> \
    <mirror> \
      <id>central</id> \
      <mirrorOf>central</mirrorOf> \
      <url>https://repo1.maven.org/maven2/</url> \
    </mirror> \
    <mirror> \
      <id>jboss</id> \
      <mirrorOf>central</mirrorOf> \
      <url>https://repository.jboss.org/nexus/content/repositories/releases/</url> \
    </mirror> \
  </mirrors> \
</settings>' > /root/.m2/settings.xml
RUN mvn clean package -DskipTests

FROM eclipse-temurin:20-jre-alpine
WORKDIR /app
COPY --from=build /app/target/usr1-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]