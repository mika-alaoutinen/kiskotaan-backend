package mikaa.infra;

import java.time.LocalDateTime;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import lombok.extern.slf4j.Slf4j;
import mikaa.errors.ErrorBody;
import mikaa.errors.NotFoundException;

@Slf4j
class GlobalErrorHandler {

  @ServerExceptionMapper(NotFoundException.class)
  Response handleNotFound(NotFoundException ex) {
    log.info("Resource not found " + ex.getMessage());

    var body = new ErrorBody(
        LocalDateTime.now(),
        404,
        "Not Found",
        ex.getMessage(),
        "path");

    return Response.status(404).entity(body).build();
  }

}
