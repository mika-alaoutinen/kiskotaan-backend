package mikaa.players.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import mikaa.players.errors.BadRequestException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  static final String PATH = "/players";
  static final GlobalExceptionHandler HANDLER = new GlobalExceptionHandler();

  @Mock
  private HttpServletRequest req;

  @BeforeEach
  void setup() {
    when(req.getRequestURI()).thenReturn(PATH);
  }

  @Test
  void should_handle_bad_request() {
    var response = HANDLER.handleBadRequest(new BadRequestException("Test error message"), req);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    var body = response.getBody();
    assertEquals("Bad Request", body.getError());
    assertEquals("Test error message", body.getMessage());
    assertEquals(PATH, body.getPath());
    assertEquals(400, body.getStatus());
    assertNotNull(body.getTimestamp());
  }

}
