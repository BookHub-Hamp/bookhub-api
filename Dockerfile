FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/bookhub-api-0.0.1.jar
COPY ${JAR_FILE} bookhub-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bookhub-api.jar"]
