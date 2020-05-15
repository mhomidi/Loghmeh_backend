
FROM maven:3.5-jdk-11 as BUILD
COPY src /usr/loghmeh/src
COPY pom.xml /usr/loghmeh
RUN mvn -f /usr/loghmeh/pom.xml clean package

FROM tomcat:9.0.20-jre11
COPY --from=BUILD ???? /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]







