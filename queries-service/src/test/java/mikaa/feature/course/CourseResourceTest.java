package mikaa.feature.course;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;

import static io.restassured.RestAssured.given;

/**
 * Insert test data with Liquibase. See src/main/resources/db.
 */
@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @Test
  void should_get_all_courses() {
    var courseSummaries = given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(CourseSummaryDTO[].class);

    assertEquals(2, courseSummaries.length);

    var laajis = courseSummaries[0];
    assertEquals(1, laajis.id());
    assertEquals("Frisbeegolf Laajis", laajis.name());
    assertEquals(58, laajis.par());
    assertEquals(18, laajis.holes());

    var keljo = courseSummaries[1];
    assertEquals(2, keljo.id());
    assertEquals("Keljonkankaan frisbeegolfrata", keljo.name());
    assertEquals(63, keljo.par());
    assertEquals(18, keljo.holes());
  }

  @Test
  void should_get_course_by_id() {
    var laajis = given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(CourseDTO.class);

    assertEquals(1, laajis.id());
    assertEquals("Frisbeegolf Laajis", laajis.name());
    assertEquals(58, laajis.par());
    assertEquals(18, laajis.holes().size());

    var hole9 = laajis.holes().get(8);
    assertEquals(9, hole9.number());
    assertEquals(4, hole9.par());
    assertEquals(172, hole9.distance());
  }

  @Test
  void should_handle_course_not_found() {
    given()
        .when()
        .get(ENDPOINT + "/99")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON);
  }

}
