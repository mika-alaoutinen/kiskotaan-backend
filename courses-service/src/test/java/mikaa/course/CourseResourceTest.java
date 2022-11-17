package mikaa.course;

import org.junit.jupiter.api.Test;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.hole.HoleEntity;

import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;

import java.util.List;

@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @Test
  void should_get_all_courses() {
    PanacheMock.mock(CourseEntity.class);

    List<HoleEntity> holes = List.of(
        new HoleEntity(1, 3, 80),
        new HoleEntity(2, 4, 120));

    List<PanacheEntityBase> courses = List.of(new CourseEntity(1, "DG Course", holes));

    when(CourseEntity.listAll()).thenReturn(courses);

    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "[0].id", is(1),
            "[0].name", is("DG Course"),
            "[0].holes", is(2),
            "[0].par", is(7));
  }
}
