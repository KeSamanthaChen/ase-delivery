#reference example 1 from: https://github.com/spring-guides/gs-spring-boot-docker
FROM openjdk:11-jre-slim
ARG JAR_FILE=target/*.jar
#copy into the image
COPY ${JAR_FILE} app.jar
#The array form of the Dockerfile ENTRYPOINT is used so that there is no shell wrapping the java process
ENTRYPOINT ["java","-jar","/app.jar"]
