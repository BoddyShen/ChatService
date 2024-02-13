FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} chatservice.jar

ENTRYPOINT ["java", "-jar", "/chatservice.jar"]

EXPOSE 8081