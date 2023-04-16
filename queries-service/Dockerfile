# Builder image
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /code

COPY pom.xml /code/
COPY src /code/src

RUN mvn package -f /code/pom.xml -B -DskipTests

# Application image
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.6
WORKDIR /work/
RUN chown 1001 /work \
  && chmod "g+rwX" /work \
  && chown 1001:root /work
COPY --chown=1001:root build/*-runner /work/application

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
