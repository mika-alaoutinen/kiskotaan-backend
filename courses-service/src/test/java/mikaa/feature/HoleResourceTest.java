package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import mikaa.dto.NewHoleDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HoleResourceTest {

  private static final String ENDPOINT = "/holes";

  @InjectMock
  private HoleRepository repository;

  @Test
  void should_update_hole() {
    HoleEntity hole = new HoleEntity(1L, 1, 3, 90, null);
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(hole));

    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO(2, 4, 100))
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

    verify(repository, atLeastOnce()).persist(any(HoleEntity.class));
  }

}
