package mikaa.infra.errors;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path.Node;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
class GlobalExceptionHandler {

  private final UriInfo uri;

  @ServerExceptionMapper(ConstraintViolationException.class)
  RestResponse<ValidationErrorBody> handleConstraintViolation(ConstraintViolationException ex) {
    log.info(ex.getMessage());

    var errors = ex.getConstraintViolations()
        .stream()
        .map(GlobalExceptionHandler::fromConstraintViolation)
        .toList();

    return fromValidationErrors(errors);
  }

  @ServerExceptionMapper(NotFoundException.class)
  RestResponse<ErrorBody> handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = getPath(uri);
    var body = new ErrorBody(404, "Not Found", msg, path);

    log.info(msg);
    return RestResponse.status(Status.NOT_FOUND, body);
  }

  @ServerExceptionMapper(ValidationException.class)
  RestResponse<ValidationErrorBody> handleValidationException(ValidationException ex) {
    log.info(ex.getMessage());

    var errors = ex.getErrors()
        .stream()
        .map(e -> new ValidationError(e.field(), e.message()))
        .toList();

    return fromValidationErrors(errors);
  }

  private RestResponse<ValidationErrorBody> fromValidationErrors(List<ValidationError> errors) {
    var body = new ValidationErrorBody(getPath(uri), errors);
    return RestResponse.status(Status.BAD_REQUEST, body);
  }

  private static ValidationError fromConstraintViolation(ConstraintViolation<?> violation) {
    String field = "";
    for (Node node : violation.getPropertyPath()) {
      field = node.getName();
    }

    return new ValidationError(field, violation.getMessage());
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

}
