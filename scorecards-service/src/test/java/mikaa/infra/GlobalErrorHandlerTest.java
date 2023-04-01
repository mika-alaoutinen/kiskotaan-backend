package mikaa.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import mikaa.model.ErrorBodyDTO;
import mikaa.model.ValidationErrorDTO;

@QuarkusTest
class GlobalErrorHandlerTest {

  @InjectMock
  private UriInfo uri;

  private GlobalErrorHandler handler;

  @BeforeEach
  void setup() throws URISyntaxException {
    when(uri.getRequestUri()).thenReturn(new URI("https://testuri:8083/scorecards/1"));
    handler = new GlobalErrorHandler(uri);
  }

  @Test
  void should_handle_404_errors() throws URISyntaxException {
    var response = handler.handleNotFound(new NotFoundException("Test error message"));
    assertEquals(404, response.getStatus());

    ErrorBodyDTO body = response.getEntity();
    assertNotNull(body.getTimestamp());
    assertEquals(404, body.getStatus());
    assertEquals("Not Found", body.getError());
    assertEquals("Test error message", body.getMessage());
    assertEquals("/scorecards/1", body.getPath());
  }

  @Test
  void should_handle_constraint_violation_errors() throws URISyntaxException {
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn("Test validation error");
    when(path.toString()).thenReturn("obj.field.test");

    var response = handler.handleConstraintViolation(new ConstraintViolationException(Set.of(violation)));
    assertEquals(400, response.getStatus());

    var body = response.getEntity();
    assertNotNull(body.getTimestamp());
    assertEquals(400, body.getStatus());
    assertEquals("Bad Request", body.getError());
    assertEquals("Invalid request body", body.getMessage());
    assertEquals("/scorecards/1", body.getPath());

    var expectedError = new ValidationErrorDTO()
        .field("obj.field.test")
        .message("Test validation error");

    var validationErrors = body.getValidationErrors();
    assertEquals(1, validationErrors.size());
    assertEquals(expectedError, validationErrors.get(0));
  }

}
