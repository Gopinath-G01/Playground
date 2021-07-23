FROM openjdk:8-jdk-alpine

MAINTAINER Gopinath Gunasekaran <gopi.nov16@gmail.com>

VOLUME /tmp
RUN CHMOD -R 777 *
COPY ./target/*.jar app.jar

RUN addgroup -S demo && adduser -S demo -G demo
USER demo

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
