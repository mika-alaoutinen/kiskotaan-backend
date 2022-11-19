package mikaa.infra;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import lombok.extern.slf4j.Slf4j;
import mikaa.errors.ErrorBody;
import mikaa.errors.NotFoundException;

@Slf4j
class GlobalErrorHandler {

  @ServerExceptionMapper(NotFoundException.class)
  Response handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = ex.getPath();

    log.info(msg);

    return Response
        .status(404)
        .entity(new ErrorBody(404, "Not Found", msg, path))
        .build();
  }

}
