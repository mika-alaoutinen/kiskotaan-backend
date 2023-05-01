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


# Schema management
`Kiskotaan backend` solves the common problem of keeping the domain models in sync between services by lifting the models into a shared library. This library is called `Kiskotaan domain`.

## Kiskotaan domain
The domain library is published in GitHub packages and included as a dependancy in all Kiskotaan services. Aside from simply copy pasting, using a shared library is probably the easiest approach to sharing models between applications.

I chose to upload the library to GitHub packages because it is free and using the registry is a relatively straightforward process. The downside of GitHub packages is that it does not allow unauthorized access. Accessing GitHub packages requires two steps:

1. Create a personal access token with `read:packages` permission. This will be used to authenticate to GitHub. See instructions [here](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).
2. Add GitHub packages as a Maven repository. Following GitHub's [instructions](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-to-github-packages), we need to modify `settings.xml` as follows:

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/mika-alaoutinen/kiskotaan-backend</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <username>mika-alaoutinen</username>
      <password>personal access token from step 1 here</password>
    </server>
  </servers>
</settings>
```

## Using GitHub packages is a pretty bad idea
Using GitHub as a package registry means that it is not really possible for others to fork the code and try it out, because the code will not build due to a missing dependency. Another downside is that CI pipelines are slower, because downloading dependencies from GH is noticeably slower than downloading them from Maven Central.

Overall, publishing shared code as a library to GitHub is probably not worth the trouble in small projects such as this. It would be easier and more convenient to simply copy paste the shared code as needed.

## Other approaches to schema management
For schema management, a popular way to go is to use a schema registry service, such as the one from [Confluent](https://docs.confluent.io/platform/current/schema-registry/index.html). This has a couple of major downsides:
- You need to host and manage the schema registry or use a cloud solution.
- There does not seem to be a good way to generate code from the schemas stored in the registry. To generate code, you would need a Maven plugin to first download the schema files from the registry and then another plugin to do the code generation.

A schema registry is probably worth using for a production system, but it's not even remotely worth the trouble here.


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
