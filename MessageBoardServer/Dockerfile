FROM openjdk:8-jdk-alpine
MAINTAINER storytel.com
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} messageboard-0.0.1.jar
ENTRYPOINT ["java","-jar","/messageboard-0.0.1.jar"]
