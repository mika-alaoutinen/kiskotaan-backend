package mikaa.infra;

import java.time.OffsetDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.errors.NotFoundException;
import mikaa.model.ErrorBodyDTO;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
class GlobalErrorHandler {

  private final UriInfo uri;

  @ServerExceptionMapper(NotFoundException.class)
  RestResponse<ErrorBodyDTO> handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = getPath(uri);
    var body = new ErrorBodyDTO()
        .error("Not found")
        .message(msg)
        .path(path)
        .status(404)
        .timestamp(OffsetDateTime.now());

    log.info(msg);

    return RestResponse.status(Status.NOT_FOUND, body);
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

}
