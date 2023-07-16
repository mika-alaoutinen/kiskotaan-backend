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

### `Apicurio`
- The microservices use Apicurio schema registry to store event schemas.
- Apicurio registry run on `localhost:8002`.
- Schemas are described as Avro documents that are stored under `src/main/avro`.
- Message payload Java classes are generated from the Avro schemas.

### `Debezium connector for Postgres`
- Debezium connector is used for change data capture to share data between the different microservices. The connector streams changes to database into Kafka topics.
- The connector is used to implement the [transactional outbox pattern](https://microservices.io/patterns/data/transactional-outbox.html).
- Runs on `localhost:8001`.

### `Kafka`
- The microservices use Kafka as a message broker.
- Kafka bootstrap server runs on `localhost:9092`. Services in the Docker network (i.e. containers) connects to port `9091` instead.
- Kafka is run with KRaft, which allows using it without a ZooKeeper instance.

### `Kafka UI`
- Pretty self-explanatory. Used to conveniently view information about the Kafka cluster.
- Runs on `localhost:8000`

### `MongoDB`
- Microservices that want to operate in a non-blocking manner use MongoDB instead of Postgres as their database.
- At the moment, used in `Queries` service.

### `Postgres`
- Microservices primarily use Postgres as their database.
- Each service has its own database. In the interest of local development all databases are in the same Postgres instance.
- Postgres uses the default `5432` port.


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
- Scorecards.scorecard_updated

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

## No reactive support with code generation
There doesn't seem to be a sensible way to generate reactive interfaces from an OpenAPI document. At best it's possible to generate interfaces that wrap return values in `CompletionStage`, but I definitely want to take advantage of Mutiny with Quarkus. I could generate the model classes and do the API interfaces by hand, but I'd rather just ditch code generation entirely. Spring probably has more mature code generation tools available.

## Score card service
`org.jboss.resteasy.reactive.ResponseStatus` annotation does nothing. It should be possible to set the status code of a response using the annotation, but in reality the annotation does nothing. The workaround is to wrap responses in `Response` or `RestResponse` and set headers that way. Unfortunately OpenAPI code generation does not support RestResponse.

# TODO
- Try out the `outbox` pattern in `Courses service` to share course state to a Kafka topic. Use the Debezium connector for Postgres. 
- Implement `Queries service`. 
  - Use Kafka Streams to query the state of different domain entities.
  - MongoDB won't be needed anymore.
  - Figure out how to send course, player and score card events to Kafka on application launch to create initial test data.
- Implement search functionality for score cards.

# Ideas for further development
1. Add `Analytics service` that aggregates facts about played rounds. 
  - Try out Kafka Streams.
  - Who has the most bogies and birdies? 
  - What are the most popular courses?

2. Add `Tournament service` that assigns players to scorecards and keeps track
   of overall player scores.

