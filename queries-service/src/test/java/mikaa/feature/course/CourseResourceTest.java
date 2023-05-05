package mikaa.feature.course;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.dto.CourseDTO;
import mikaa.feature.TestData;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @Test
  void should_get_all_courses() {
    var response = given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(CourseDTO[].class);

    assertEquals(1, response.length);
    assertCourse(response[0], TestData.COURSE);
  }

  @Test
  void should_get_course_by_id() {
    var response = given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(CourseDTO.class);

    assertCourse(response, TestData.COURSE);
  }

  private static void assertCourse(CourseDTO course, CourseDTO expected) {
    assertEquals(course.id(), expected.id());
    assertEquals(course.name(), expected.name());
    assertEquals(course.par(), expected.par());
    assertEquals(course.holes().size(), expected.holes().size());
  }

}
