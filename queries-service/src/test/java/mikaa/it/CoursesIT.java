package mikaa.it;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.CoursePayload;
import mikaa.CourseUpdated;
import mikaa.config.IncomingChannels;

import static org.hamcrest.Matchers.is;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CoursesIT {

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void fetch_added_course() {
    fetchByIdAndExpectNotFound(3);

    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_ADDED);
    source.send(new CoursePayload(3l, "New Course", List.of()));

    fetchById(3).body("name", is("New Course"));
  }

  @Test
  void try_to_fetch_deleted_course() {
    fetchById(100);

    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_DELETED);
    source.send(new CoursePayload(100l, "Delete me 1", List.of()));

    fetchByIdAndExpectNotFound(100);
  }

  @Test
  void fetch_updated_course() {
    fetchById(2);

    InMemorySource<CourseUpdated> source = connector.source(IncomingChannels.Course.COURSE_UPDATED);
    source.send(new CourseUpdated(2l, "Updated name"));

    fetchById(2).body("name", is("Updated name"));
  }

  private ValidatableResponse fetchById(int id) {
    return fetchByIdAndVerifyStatus(id, 200);
  }

  private ValidatableResponse fetchByIdAndExpectNotFound(int id) {
    return fetchByIdAndVerifyStatus(id, 404);
  }

  private ValidatableResponse fetchByIdAndVerifyStatus(int id, int expectedStatus) {
    return given()
        .when()
        .get("/courses/%d".formatted(id))
        .then()
        .statusCode(expectedStatus)
        .contentType(ContentType.JSON);
  }

}
