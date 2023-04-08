# Kiskotaan backend
Kiskotaan backend is a disc golf scorekeeping application that consists of several microservices. The services are written in Java using Quarkus and Spring Boot frameworks.

## Microservices
- `Players` service on port `8081`.
  - CRUD operations for players.
- `Courses` service on port `8082`.
  - CRUD operations for courses and holes.
- `Score cards` service on port `8083`.
  - CRUD operations for score cards.
  - A score card is always for a single course and a single score card can have many players.
- `Logging` service on port `8085`.
  - Log aggregation service that listens for all Kafka events and logs them to stdout.
  - Realistically the logs should be stored into some external system, such as Kibana or Prometheus.

## Other components
- `Postgres` instance.
  - Each service has its own database, however in the interest of local development all databases are in the same Postgres instance.
  - Postgres uses the default `5432` port.
- `Kafka` message broker.
  - Kafka bootstrap server runs on `localhost:9092`. Services in the Docker network (i.e. containers) connects to port `9091` instead.
  - Kafka uses an experimental KRaft setup, which allows running Kafka without a ZooKeeper instance.

## Available Kafka topics:
Messages will be sent to the following topics when resources are modified. Modifications include adding new resources, editing existing ones and deleting resources.

- "courses"
- "holes"
- "players"
- "scorecards"

## Running the services
The entire stack, including Kafka and a Postgres database, can be spun up with Docker compose:

> docker compose up

Note that if you also start one of the services locally, you will get a port conflict. To resolve it, change the port on the local application to something else, f. ex. `8080`. The ports are defined in `application.yaml`.

The database is loaded up with a few rows of test data per service. See the sql scripts in the `db` folder.

## Calling the services
Folder `.http` has http-files that contain example payloads that can be used to test the different services.

## Discovered problems

### Score card service
`org.jboss.resteasy.reactive.ResponseStatus` annotation does nothing. It should be possible to set the status code of a response using the annotation, but in reality the annotation does nothing. The workaround is to wrap responses in `Response` or `RestResponse` and set headers that way. Unfortunately OpenAPI code generation does not support RestResponse.

## TODO
- Refactor `Score cards service` to be reactive. Requires switching from Postgres to MongoDB.
- Consider creating a new "read-only" service that consolidates all GET operations into one service.
  - Applies CQRS design pattern.
  - Listens to events from other services and duplicates their data into MongoDB.
  - Problems: `Score cards service` also has to listen to events from Courses and Players services, so there's some overlap with existing functionality.
  - Might be more reasonable to implement GET operations into `Score cards service`?
- Implement filtering by keywords in GET requests.
  - [x] Players service done.
  - [ ] Courses service
- Implement search functionality for score cards.
- Implement paging.
- Implement authentication and authorization.
- Consider adding DB migrations with Liquibase or Flyway?  
