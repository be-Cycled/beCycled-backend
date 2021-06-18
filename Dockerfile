FROM adoptopenjdk:11-jdk-hotspot
MAINTAINER Ivan Muratov <binakot@gmail.com>

EXPOSE 8080

ADD build/libs/service.jar /

CMD echo "Service is starting..." && \
    \
    java -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -Djavax.xml.accessExternalDTD=all $JAVA_OPTIONS \
    -jar /service.jar
