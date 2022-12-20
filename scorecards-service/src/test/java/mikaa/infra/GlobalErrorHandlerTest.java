package mikaa.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import mikaa.errors.NotFoundException;
import mikaa.model.ErrorBodyDTO;

@QuarkusTest
class GlobalErrorHandlerTest {

  @InjectMock
  private UriInfo uri;

  private GlobalErrorHandler handler;

  @BeforeEach
  void setup() throws URISyntaxException {
    when(uri.getRequestUri()).thenReturn(new URI("https://testuri:8082/api/scorecards/1"));
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
    assertEquals("/api/scorecards/1", body.getPath());
  }

}
