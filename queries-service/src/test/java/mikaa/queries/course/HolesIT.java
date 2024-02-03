package mikaa.queries.course;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.courses.HoleEvent;
import mikaa.kiskotaan.courses.HolePayload;
import mikaa.config.IncomingChannels;

import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HolesIT {

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void fetch_course_with_new_hole() {
    fetchLaajis().body("holes.size()", is(18));

    var payload = new HolePayload(100l, 1l, 19, 5, 200);
    sendEvent(new HoleEvent(Action.ADD, payload));

    fetchLaajis().body(
        "holes[18].par", is(5),
        "holes[18].distance", is(200));
  }

  @Test
  void fetch_course_after_hole_upde() {
    fetchLaajis().body(
        "holes[0].par", is(3),
        "holes[0].distance", is(107));

    var payload = new HolePayload(1l, 1l, 1, 4, 123);
    sendEvent(new HoleEvent(Action.UPDATE, payload));

    fetchLaajis().body(
        "holes[0].par", is(4),
        "holes[0].distance", is(123));
  }

  @Test
  void fetch_course_after_hole_delete() {
    fetchCourse(101).body("holes[0].par", is(4));

    var payload = new HolePayload(201l, 101l, 1, 4, 110);
    sendEvent(new HoleEvent(Action.DELETE, payload));

    // First hole was deleted, hole 2 is now the only hole in the course
    fetchCourse(101).body("holes[0].number", is(2));
  }

  private void sendEvent(HoleEvent event) {
    InMemorySource<HoleEvent> source = connector.source(IncomingChannels.HOLE_STATE);
    source.send(event);
  }

  private ValidatableResponse fetchLaajis() {
    return fetchCourse(1);
  }

  private ValidatableResponse fetchCourse(int id) {
    return given()
        .when()
        .get("/courses/" + id)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
  }

}
