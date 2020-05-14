FROM maven:3.6.0-jdk-11-slim AS BUILD

COPY src /usr/src/myapp/src

COPY pom.xml /usr/src/myapp

COPY application.properties /usr/src/myapp

RUN mvn -f /usr/src/myapp/pom.xml package -e

# FROM openjdk:8-jdk-alpine

#ENTRYPOINT ["ls","-l","/usr/src/myapp/target"]

ENTRYPOINT ["java","-jar",""]