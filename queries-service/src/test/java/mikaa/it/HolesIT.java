package mikaa.it;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.HolePayload;
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

    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_ADDED);
    source.send(new HolePayload(100l, 1l, 19, 5, 200));

    fetchLaajis().body(
        "holes[18].par", is(5),
        "holes[18].distance", is(200));
  }

  @Test
  void fetch_course_after_hole_upde() {
    fetchLaajis().body(
        "holes[0].par", is(3),
        "holes[0].distance", is(107));

    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_UPDATED);
    source.send(new HolePayload(1l, 1l, 1, 4, 123));

    fetchLaajis().body(
        "holes[0].par", is(4),
        "holes[0].distance", is(123));
  }

  @Test
  void fetch_course_after_hole_delete() {
    fetchKeljo().body("holes[0].par", is(3));

    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_DELETED);
    source.send(new HolePayload(19l, 2l, 1, 3, 92));

    // First hole is now number 2 after hole 1 was deleted
    fetchKeljo().body("holes[0].number", is(2));
  }

  private ValidatableResponse fetchLaajis() {
    return fetchCourse(1);
  }

  private ValidatableResponse fetchKeljo() {
    return fetchCourse(2);
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
