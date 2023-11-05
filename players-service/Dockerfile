# Build jar package with Maven:
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package -f /app/pom.xml -B -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Create the Docker image:
FROM eclipse-temurin:21

# Run application as user "spring"
RUN addgroup --system spring && useradd -g spring spring
USER spring:spring

# Copy dependencies from build stage
ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","mikaa.players.PlayersApplication"]
