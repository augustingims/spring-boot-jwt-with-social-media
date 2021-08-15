FROM openjdk:8-jdk-alpine
WORKDIR /app
VOLUME /tmp
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS
ARG JAR_FILE
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]