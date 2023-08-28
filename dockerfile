FROM maven:3.8.3-openjdk-17

COPY  . .
RUN mvn clear package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /target/spring-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT [ "java","-jar","app.jar" ]