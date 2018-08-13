FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD user-rest-api-0.1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]