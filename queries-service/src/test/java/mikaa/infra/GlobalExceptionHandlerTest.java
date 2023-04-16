package mikaa.infra;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import mikaa.model.ErrorBodyDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@QuarkusTest
class GlobalExceptionHandlerTest {

  @InjectMock
  private UriInfo uri;

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setup() throws URISyntaxException {
    when(uri.getRequestUri()).thenReturn(new URI("https://testuri:8083/scorecards/1"));
    handler = new GlobalExceptionHandler(uri);
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

}
