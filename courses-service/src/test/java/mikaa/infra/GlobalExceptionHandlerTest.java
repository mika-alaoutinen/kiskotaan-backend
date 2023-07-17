package mikaa.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;
import mikaa.model.ValidationErrorDTO;

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

    var body = response.getEntity();
    assertNotNull(body.getTimestamp());
    assertEquals(404, body.getStatus());
    assertEquals("Not Found", body.getError());
    assertEquals("Test error message", body.getMessage());
    assertEquals("/courses/1", body.getPath());
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
    assertNotNull(body.getTimestamp());
    assertEquals(400, body.getStatus());
    assertEquals("Bad Request", body.getError());
    assertEquals("/courses/1", body.getPath());

    var expectedError = new ValidationErrorDTO()
        .field("fieldA")
        .message("Test validation error");

    var validationErrors = body.getValidationErrors();
    assertEquals(1, validationErrors.size());
    assertEquals(expectedError, validationErrors.get(0));
  }

  @Test
  void shoud_handle_validation_errors() throws URISyntaxException {
    var validationException = ValidationException.from(new ValidationError("object.name", "Invalid name")).get();
    var response = handler.handleValidation(validationException);

    assertEquals(400, response.getStatus());

    var body = response.getEntity();
    assertNotNull(body.getTimestamp());
    assertEquals(400, body.getStatus());
    assertEquals("Bad Request", body.getError());
    assertEquals("Invalid request body", body.getMessage());
    assertEquals("/courses/1", body.getPath());

    var expectedError = new ValidationErrorDTO()
        .field("object.name")
        .message("Invalid name");

    var validationErrors = body.getValidationErrors();
    assertEquals(1, validationErrors.size());
    assertEquals(expectedError, validationErrors.get(0));

  }

}
