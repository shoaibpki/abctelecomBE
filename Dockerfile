FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} abctelecom.jar
ENTRYPOINT ["java", "-jar", "/abctelecom.jar"]
EXPOSE 8082