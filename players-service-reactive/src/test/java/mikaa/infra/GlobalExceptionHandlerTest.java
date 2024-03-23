package mikaa.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;

@QuarkusTest
class GlobalExceptionHandlerTest {

  @InjectMock
  private UriInfo uri;

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setup() throws URISyntaxException {
    when(uri.getRequestUri()).thenReturn(new URI("https://testuri:8081/players/1"));
    handler = new GlobalExceptionHandler(uri);
  }

  @Test
  void should_handle_404_errors() throws URISyntaxException {
    var response = handler.handleNotFound(new NotFoundException("Test error message"));
    assertEquals(404, response.getStatus());

    var body = response.getEntity();
    assertNotNull(body.timestamp());
    assertEquals(404, body.status());
    assertEquals("Not Found", body.error());
    assertEquals("Test error message", body.message());
    assertEquals("/players/1", body.path());
  }

  @Test
  void shoud_handle_validation_errors() throws URISyntaxException {
    var validationException = ValidationException.from(new ValidationError("object.name", "Invalid name")).get();
    var response = handler.handleValidation(validationException);

    assertEquals(400, response.getStatus());

    var body = response.getEntity();
    assertNotNull(body.timestamp());
    assertEquals(400, body.status());
    assertEquals("Bad Request", body.error());
    assertEquals("Invalid request body", body.message());
    assertEquals("/courses/1", body.path());

    var expectedError = new ValidationError("object.name", "Invalid name");

    var validationErrors = body.validationErrors();
    assertEquals(1, validationErrors.size());
    assertEquals(expectedError, validationErrors.get(0));

  }

}
