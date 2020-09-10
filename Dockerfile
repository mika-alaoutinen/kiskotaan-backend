FROM adoptopenjdk/openjdk14:alpine-slim as build
WORKDIR /app
ARG JAR_FILE=target/kiskotaan-backend*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]