package mikaa.feature;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class HelloResourceTest {

  @Test
  void should_respond_with_hello_world() {
    String body = """
        {
          "query": "query HelloWorld { hello }",
          "operationName": "HelloWorld",
          "variables": {}
        }
        """;

    given()
        .when()
        .body(body)
        .post("/graphql")
        .then()
        .statusCode(200)
        .body("data.hello", Matchers.is("Hello world"));
  }

}
