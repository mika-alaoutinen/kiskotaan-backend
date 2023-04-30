package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.events.holes.HoleProducer;
import mikaa.HolePayload;
import mikaa.model.NewHoleDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HoleResourceTest {

  private static final String ENDPOINT = "/holes";

  @InjectMock
  private HoleProducer producer;

  @InjectMock
  private HoleRepository repository;

  @Test
  void should_find_hole_by_id() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));
    
    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "number", is(1),
            "par", is(3),
            "distance", is(90));
  }

  @Test
  void get_returns_404() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());
    
    var response = given()
        .when()
        .get(ENDPOINT + "/1")
        .then();
        
    assertNotFoundResponse(response, 1);
  }

  @Test
  void should_update_hole() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));

    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO().number(2).par(4).distance(100))
        .when()
        .put(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "number", is(2),
            "par", is(4),
            "distance", is(100));

    verify(producer, atLeastOnce()).holeUpdated(any(HolePayload.class));
  }

  @Test
  void should_reject_update_with_invalid_payload() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO().number(0).par(3).distance(120))
        .when()
        .put(ENDPOINT + "/1")
        .then()
        .statusCode(400);

    verifyNoInteractions(repository);
    verifyNoInteractions(producer);
  }

  @Test
  void put_returns_404() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    var response = given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO().number(2).par(4).distance(100))
        .when()
        .put(ENDPOINT + "/1")
        .then();

    assertNotFoundResponse(response, 1);
    verify(repository, never()).persist(any(HoleEntity.class));
    verifyNoInteractions(producer);
  }

  @Test
  void should_delete_hole() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));

    given()
        .when()
        .delete(ENDPOINT + "/1")
        .then()
        .statusCode(204);

    verify(repository, atLeastOnce()).deleteById(1L);
    verify(producer, atLeastOnce()).holeDeleted(any(HolePayload.class));
  }

  @Test
  void should_do_nothing_on_delete_if_hole_not_found() {
    given()
        .when()
        .delete(ENDPOINT + "/1")
        .then()
        .statusCode(204);

    verify(repository, never()).deleteById(anyLong());
    verifyNoInteractions(producer);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find hole with id " + id),
            "path", is("/holes/" + id));
  }

  private static HoleEntity holeMock() {
    var course = new CourseEntity(1L, "DG Course", List.of());
    return new HoleEntity(1L, 1, 3, 90, course);
  }

}
