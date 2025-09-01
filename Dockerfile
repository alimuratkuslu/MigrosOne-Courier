FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

COPY src ./src
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/courier-tracking-0.0.1-SNAPSHOT.jar"]