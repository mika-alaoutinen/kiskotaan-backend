# Kiskotaan backend
`Kiskotaan backend` is a (disc) golf scorekeeping application that consists of several microservices. The services are written in Java using Quarkus framework.


# Microservices
`Kiskotaan backend` consists of multiple microservices that implement a part of the overall domain context. The services are:

### Queries service
- Port `8080`.
- Consolidates GET requests to different resources into one service.
- Maintains a read-only replica of the datasets (courses, players and score cards) in other services.

### Players service
- Port `8081`.
- CRUD operations for players.

### Courses service
- Port `8082`.
- CRUD operations for courses and holes.

### Score cards service
- Port `8083`.
- CRUD operations for score cards.
- A score card is always for a single course and a single score card can have many players.


# Other components
In addition to the microservices listed above, `Kiskotaan backend` requires several infrastructure components.

### Apicurio
- The microservices use Apicurio schema registry to store event schemas.
- Apicurio registry run on `localhost:8001`.
- Schemas are described as Avro documents that are stored under `src/main/avro`.
- Message payload Java classes are generated from the Avro schemas.

### Kafka
- The microservices use Kafka as a message broker.
- Kafka bootstrap server runs on `localhost:9092`. Services in the Docker network (i.e. containers) connects to port `9091` instead.
- Kafka is run with KRaft, which allows using it without a ZooKeeper instance.

### Kafka UI
- Pretty self-explanatory. Used to conveniently view information about the Kafka cluster.
- Runs on `localhost:8000`

### MongoDB
- Microservices that want to operate in a non-blocking manner use MongoDB instead of Postgres as their database.
- Used in `Queries` service.

### Postgres
- Microservices primarily use Postgres as their database.
- Each service has its own database. In the interest of local development all databases are in the same Postgres instance.
- Postgres uses the default `5432` port.


# Kafka topics
Messages will be sent to the following topics when resources are modified. Modifications include adding new resources, editing existing ones and deleting resources.

## Topic naming pattern:
Topic name consists of the domain/service name, a descriptive event type and a version number. The version number `v1` may be omitted from the topic name. Hence, topic names follow the pattern of

> Domain-event_type-version

## Current topics
The list of Kafka topics published by the different services:
- Courses-course_state
- Players-player_state
- Scorecards-scorecard_state
- Scorecards-score_entries
- Scorecards-scorecard_by_hole_state
- Scorecards-scorecard_by_player_state

# Running Kiskotaan backend
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
- Add `CourseSummary.avsc` to Courses service.
  - A course summary contains course name, hole count and course par.
  - Try reading emitted course events from Kafka and map them to a new `Courses-course_summary` topic?
- Play around with GraphQL in Queries service. It should be possible to dynamically create summarized or full views using GraphQL's `@Source` feature.

# Ideas for further development
1. Replace dual writes with the `transactional outbox` pattern.

2. Add `Analytics service` that aggregates facts about played rounds. 
  - Who has the most bogies and birdies? 
  - What are the most popular courses?
