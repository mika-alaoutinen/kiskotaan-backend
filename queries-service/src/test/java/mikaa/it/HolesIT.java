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
    source.send(new HolePayload(19l, 2l, 19, 5, 200));

    fetchLaajis().body(
        "holes[18].par", is(15),
        "holes[18].distance", is(200));
  }

  @Test
  void fetch_course_after_hole_upde() {
    fetchLaajis().body(
        "holes[0].par", is(3),
        "holes[0].distance", is(107));

    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_UPDATED);
    source.send(new HolePayload(20l, 1l, 1, 4, 123));

    fetchLaajis().body(
        "holes[0].par", is(4),
        "holes[0].distance", is(123));
  }

  @Test
  void fetch_course_after_hole_delete() {
    fetchLaajis().body("holes[1].par", is(3));

    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_DELETED);
    source.send(new HolePayload(21l, 1l, 2, 3, 127));

    // Second hole is now number 3 after hole 2 was deleted
    fetchLaajis().body("holes[1].number", is(3));
  }

  private ValidatableResponse fetchLaajis() {
    return given()
        .when()
        .get("/courses/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
  }

}
