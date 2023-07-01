package mikaa.it;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import mikaa.kiskotaan.domain.HolePayload;
import mikaa.model.HoleDTO;
import mikaa.producers.holes.HoleProducer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HolesIT {

  private static final String ENDPOINT = "/holes";

  @InjectMock
  private HoleProducer producer;

  @Test
  void should_update_hole() {
    given()
        .contentType(ContentType.JSON)
        .body(new HoleDTO().number(2).par(4).distance(100))
        .when()
        .put(ENDPOINT + "/3")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(3),
            "number", is(2),
            "par", is(4),
            "distance", is(100));

    verify(producer, atLeastOnce()).holeUpdated(any(HolePayload.class));
  }

  @Test
  void should_delete_hole() {
    given()
        .when()
        .delete(ENDPOINT + "/4")
        .then()
        .statusCode(204);

    verify(producer, atLeastOnce()).holeDeleted(any(HolePayload.class));
  }

}
