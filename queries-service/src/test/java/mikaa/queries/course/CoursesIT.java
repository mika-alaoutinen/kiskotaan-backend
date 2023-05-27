package mikaa.queries.course;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;
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
    long courseId = 3;
    fetchByIdAndExpectNotFound(courseId);

    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_ADDED);
    source.send(new CoursePayload(courseId, "New Course", List.of()));

    fetchById(courseId).body("name", is("New Course"));
  }

  @Test
  void try_to_fetch_deleted_course() {
    long courseId = 100;
    fetchById(courseId);

    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_DELETED);
    source.send(new CoursePayload(courseId, "Delete me 1", List.of()));

    fetchByIdAndExpectNotFound(courseId);
  }

  @Test
  void fetch_updated_course() {
    long courseId = 102;
    fetchById(courseId);

    InMemorySource<CourseUpdated> source = connector.source(IncomingChannels.Course.COURSE_UPDATED);
    source.send(new CourseUpdated(courseId, "Updated name"));

    fetchById(courseId).body("name", is("Updated name"));
  }

  private ValidatableResponse fetchById(long id) {
    return fetchByIdAndVerifyStatus(id, 200);
  }

  private ValidatableResponse fetchByIdAndExpectNotFound(long id) {
    return fetchByIdAndVerifyStatus(id, 404);
  }

  private ValidatableResponse fetchByIdAndVerifyStatus(long id, int expectedStatus) {
    return given()
        .when()
        .get("/courses/%d".formatted(id))
        .then()
        .statusCode(expectedStatus)
        .contentType(ContentType.JSON);
  }

}
