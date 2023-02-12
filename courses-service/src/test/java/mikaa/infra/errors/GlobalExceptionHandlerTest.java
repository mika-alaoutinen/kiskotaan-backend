package mikaa.infra.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.constraints.Size;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import mikaa.errors.ValidationError;

@QuarkusTest
class GlobalExceptionHandlerTest {

  @InjectMock
  private UriInfo uri;

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setup() throws URISyntaxException {
    when(uri.getRequestUri()).thenReturn(new URI("https://testuri:8082/courses/1"));
    handler = new GlobalExceptionHandler(uri);
  }

  @Test
  void should_handle_404_errors() throws URISyntaxException {
    var response = handler.handleNotFound(new NotFoundException("Test error message"));
    assertEquals(404, response.getStatus());

    ErrorBody body = response.getEntity();
    assertNotNull(body.timestamp());
    assertEquals(404, body.status());
    assertEquals("Not Found", body.error());
    assertEquals("Test error message", body.message());
    assertEquals("/courses/1", body.path());
  }

  private static class TestClass {
    @Size(min = 1, message = "Test validation error")
    private final String fieldA;

    private TestClass() {
      fieldA = "";
    }
  }

  @Test
  void should_handle_constraint_violation_errors() throws URISyntaxException {
    var validator = Validation.buildDefaultValidatorFactory().getValidator();
    var violations = validator.validate(new TestClass());

    var response = handler.handleConstraintViolation(new ConstraintViolationException(violations));
    assertEquals(400, response.getStatus());

    var body = response.getEntity();
    assertNotNull(body.timestamp());
    assertEquals(400, body.status());
    assertEquals("Bad Request", body.error());
    assertEquals("/courses/1", body.path());

    var validationErrors = body.validationErrors();
    assertEquals(1, validationErrors.size());
    assertEquals(new ValidationError("fieldA", "Test validation error"), validationErrors.get(0));
  }

}
