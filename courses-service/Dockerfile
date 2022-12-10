# Builder image
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /code

COPY pom.xml /code/
COPY src /code/src

RUN mvn package -f /code/pom.xml -B -DskipTests

# Application image
FROM registry.access.redhat.com/ubi8/openjdk-17
WORKDIR /work

RUN chmod 775 /work

COPY --chown=185 --from=build /code/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=build /code/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=build /code/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=build /code/target/quarkus-app/quarkus/ /deployments/quarkus/

USER 185
EXPOSE 8082

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# CMD ["./app", "-Dquarkus.http.host=0.0.0.0"]

# COPY --chown=185 --from=build /code/target/*-runner /application
# RUN chmod 775 /app

# EXPOSE 8082
# USER 185

# ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]
