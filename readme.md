# Kiskotaan backend
Kiskotaan backend is a disc golf scorekeeping application that consists of several microservices. The services are written in Java using Quarkus and Spring Boot frameworks.


# Microservices
`Kiskotaan backend` consists of multiple microservices that implement a part of the overall domain context. The services are:

### `Queries` service
- Port `8080`.
- Consolidates GET requests to different resources into one service.
- Maintains a read-only replica of the datasets (courses, players and score cards) in other services.

### `Players` service
- Port `8081`.
- CRUD operations for players.

### `Courses` service
- Port `8082`.
- CRUD operations for courses and holes.

### `Score cards` service
- Port `8083`.
- CRUD operations for score cards.
- A score card is always for a single course and a single score card can have many players.

### `Logging` service
- Port `8085`.
- Log aggregation service that listens for all Kafka events and logs them to stdout.
- Realistically the logs should be stored into some external system, such as Kibana or Prometheus.


# Other components
In addition to the microservices listed above, `Kiskotaan backend` requires several infrastructure components.

### `Postgres`
- Microservices primarily use Postgres as their database.
- Each service has its own database. In the interest of local development all databases are in the same Postgres instance.
- Postgres uses the default `5432` port.

### `MongoDB`
- Microservices that want to operate in a non-blocking manner use MongoDB instead of Postgres as their database.
- At the moment, used in `Queries` service.

### `Kafka`
- The microservices use Kafka as a message broker.
- Kafka bootstrap server runs on `localhost:9092`. Services in the Docker network (i.e. containers) connects to port `9091` instead.
- Kafka is run with KRaft, which allows using it without a ZooKeeper instance.


# Kafka topics
Messages will be sent to the following topics when resources are modified. Modifications include adding new resources, editing existing ones and deleting resources.

## Topic naming pattern:
Topic name consists of the domain/service name, a descriptive event type and a version number. The version number `v1` may be omitted from the topic name. Hence, topic names follow the pattern of

> Domain.event-type.version

## Current topics
The list of Kafka topics published by the different services.

### Courses service produces
- Courses.course_added
- Courses.course_deleted
- Courses.course_updated
- Courses.hole_added
- Courses.hole_deleted
- Courses.hole_updated

### Players service produces
- Players.player_added
- Players.player_deleted
- Players.player_updated

### Score cards service produces
- Scorecards.scorecard_added
- Scorecards.scorecard_deleted
- Scorecards.score_added
- Scorecards.score_deleted


# Running `Kiskotaan backend`
The entire stack, including Kafka and a Postgres database, can be spun up with Docker compose:

```bash
docker compose up
```

Note that if you also start one of the services locally, you will get a port conflict. To resolve it, change the port on the local application to something else, f. ex. `8080`. The ports are defined in `application.yaml`.

The database is loaded up with a few rows of test data per service. See the sql scripts in the `db` folder.

## Calling the services
Folder `.http` has http-files that contain example payloads that can be used to test the different services.

# Discovered problems

## Score card service
`org.jboss.resteasy.reactive.ResponseStatus` annotation does nothing. It should be possible to set the status code of a response using the annotation, but in reality the annotation does nothing. The workaround is to wrap responses in `Response` or `RestResponse` and set headers that way. Unfortunately OpenAPI code generation does not support RestResponse.

# TODO
- Implement Queries service.
- Implement paging in Queries service.
- Implement search functionality for score cards.
- Implement authentication and authorization.
