package mikaa.graphql;

import static io.restassured.RestAssured.given;

import io.restassured.response.ValidatableResponse;

public interface QueryClient {

  private static String buildQuery(String query) {
    return """
        {
          "query": "query TestQuery $query",
          "operationName": "TestQuery",
          "variables": {}
        }
        """.stripIndent().replace("$query", query);
  }

  public static ValidatableResponse query(String query) {
    return given()
        .when()
        .body(buildQuery(query))
        .post("/graphql")
        .then();
  }

}
