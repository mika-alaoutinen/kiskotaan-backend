package mikaa.infra;

import java.time.OffsetDateTime;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.model.ErrorBodyDTO;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class GlobalExceptionHandler {

  private final UriInfo uri;

  @ServerExceptionMapper(NotFoundException.class)
  RestResponse<ErrorBodyDTO> handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = getPath(uri);
    var body = new ErrorBodyDTO()
        .status(404)
        .error("Not Found")
        .message(msg)
        .path(path)
        .timestamp(OffsetDateTime.now());

    log.info(msg);
    return RestResponse.status(Status.NOT_FOUND, body);
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

}
