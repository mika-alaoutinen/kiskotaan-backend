package mikaa.feature.courses;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import mikaa.graphql.QueryClient;

@QuarkusTest
class CourseResourceTest {

  @Test
  void should_return_courses() {
    String query = """
        {
          courses {
            id
            name
            par
          }
        }
        """;

    QueryClient.query(query)
        .statusCode(200)
        .body("data.courses[0].name", Matchers.is("Laajis"));
  }

  @Test
  void should_return_course_by_id() {
    String query = """
        {
          course(id: 1) {
            id
            name
            par
          }
        }
        """;

    QueryClient.query(query)
        .statusCode(200)
        .body("data.course.name", Matchers.is("Laajis"));
  }

}
