package mikaa.it;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import mikaa.kiskotaan.courses.HolePayload;
import mikaa.model.HoleDTO;
import mikaa.model.UpdatedHoleDTO;
import mikaa.producers.holes.HoleProducer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HolesIT {

  private static final String ENDPOINT = "/courses/3/holes";

  @InjectMock
  private HoleProducer producer;

  @Test
  void should_add_hole_for_a_course() {
    given()
        .contentType(ContentType.JSON)
        .body(new HoleDTO().number(3).par(3).distance(90))
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "number", is(3),
            "par", is(3),
            "distance", is(90));

    verify(producer, atLeastOnce()).holeAdded(any(HolePayload.class));
  }

  @Test
  void should_update_hole() {
    given()
        .contentType(ContentType.JSON)
        .body(new UpdatedHoleDTO().par(3).distance(90))
        .when()
        .put(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "number", is(1),
            "par", is(3),
            "distance", is(90));

    verify(producer, atLeastOnce()).holeUpdated(any(HolePayload.class));
  }

  @Test
  void should_delete_hole() {
    given()
        .when()
        .delete(ENDPOINT + "/2")
        .then()
        .statusCode(204);

    verify(producer, atLeastOnce()).holeDeleted(any(HolePayload.class));
  }

}
