FROM openjdk:8-jdk-alpine

MAINTAINER Gopinath Gunasekaran <gopi.nov16@gmail.com>

EXPOSE 9000

VOLUME /tmp
COPY ./target/*.jar app.jar

RUN addgroup -S demo && adduser -S demo -G demo
USER demo

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
