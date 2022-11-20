package mikaa.infra;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.errors.ErrorBody;
import mikaa.errors.NotFoundException;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationErrorBody;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
class GlobalErrorHandler {

  private final UriInfo uri;

  @ServerExceptionMapper(ConstraintViolationException.class)
  Response handleConstraintViolation(ConstraintViolationException ex) {
    log.info(ex.getMessage());

    return Response
        .status(Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON)
        .entity(fromException(ex, uri))
        .build();
  }

  @ServerExceptionMapper(NotFoundException.class)
  Response handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = getPath(uri);

    log.info(msg);

    return Response
        .status(Status.NOT_FOUND)
        .type(MediaType.APPLICATION_JSON)
        .entity(new ErrorBody(404, "Not Found", msg, path))
        .build();
  }

  private static ValidationErrorBody fromException(ConstraintViolationException ex, UriInfo uri) {
    var errors = ex.getConstraintViolations()
        .stream()
        .map(GlobalErrorHandler::fromConstraintViolation)
        .toList();

    return new ValidationErrorBody(getPath(uri), errors);
  }

  private static ValidationError fromConstraintViolation(ConstraintViolation<?> violation) {
    return new ValidationError(violation.getPropertyPath().toString(), violation.getMessage());
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

}
