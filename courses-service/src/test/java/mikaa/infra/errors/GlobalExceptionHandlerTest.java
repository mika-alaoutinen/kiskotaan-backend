package mikaa.infra.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
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
    when(uri.getRequestUri()).thenReturn(new URI("https://testuri:8082/api/courses/1"));
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
    assertEquals("/api/courses/1", body.path());
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
    assertNotNull(body.timestamp());
    assertEquals(400, body.status());
    assertEquals("Bad Request", body.error());
    assertEquals("/api/courses/1", body.path());

    var validationErrors = body.validationErrors();
    assertEquals(1, validationErrors.size());

    assertEquals(new ValidationError("obj.field.test", "Test validation error"), validationErrors.get(0));
  }

}
