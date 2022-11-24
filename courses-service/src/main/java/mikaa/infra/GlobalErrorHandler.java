package mikaa.infra;

import javax.enterprise.context.RequestScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
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
  RestResponse<ValidationErrorBody> handleConstraintViolation(ConstraintViolationException ex) {
    var body = fromException(ex, uri);
    log.info(ex.getMessage());

    return RestResponse.status(Status.BAD_REQUEST, body);
  }

  @ServerExceptionMapper(NotFoundException.class)
  RestResponse<ErrorBody> handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = getPath(uri);
    var body = new ErrorBody(404, "Not Found", msg, path);

    log.info(msg);
    return RestResponse.status(Status.NOT_FOUND, body);
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