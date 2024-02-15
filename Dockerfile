FROM openjdk:11
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} abctelecom.jar
ADD target/*.jar abctelecom.jar
ENTRYPOINT ["java", "-jar", "/abctelecom.jar"]
EXPOSE 8082