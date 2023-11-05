# Builder image
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /code

COPY pom.xml /code/
COPY src /code/src

RUN mvn package -f /code/pom.xml -B -DskipTests

# Application image
FROM eclipse-temurin:21
WORKDIR /work

RUN chmod 775 /work

ARG APP=/code/target/quarkus-app
COPY --chown=185 --from=build ${APP}/lib/ /deployments/lib/
COPY --chown=185 --from=build ${APP}/*.jar /deployments/
COPY --chown=185 --from=build ${APP}/app/ /deployments/app/
COPY --chown=185 --from=build ${APP}/quarkus/ /deployments/quarkus/

USER 185
EXPOSE 8082

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
