package mikaa.infra;

import java.time.OffsetDateTime;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.model.ErrorBodyDTO;
import mikaa.model.ValidationErrorBodyDTO;
import mikaa.model.ValidationErrorDTO;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
class GlobalErrorHandler {

  private final UriInfo uri;

  @ServerExceptionMapper(ConstraintViolationException.class)
  RestResponse<ValidationErrorBodyDTO> handleConstraintViolation(ConstraintViolationException ex) {
    var body = fromException(ex, uri);
    log.info(ex.getMessage());

    return RestResponse.status(Status.BAD_REQUEST, body);
  }

  @ServerExceptionMapper(NotFoundException.class)
  RestResponse<ErrorBodyDTO> handleNotFound(NotFoundException ex) {
    String msg = ex.getMessage();
    String path = getPath(uri);
    var body = new ErrorBodyDTO()
        .error("Not Found")
        .message(msg)
        .path(path)
        .status(404)
        .timestamp(OffsetDateTime.now());

    log.info(msg);

    return RestResponse.status(Status.NOT_FOUND, body);
  }

  private static ValidationErrorBodyDTO fromException(ConstraintViolationException ex, UriInfo uri) {
    var errors = ex.getConstraintViolations()
        .stream()
        .map(GlobalErrorHandler::fromConstraintViolation)
        .toList();

    return new ValidationErrorBodyDTO()
        .timestamp(OffsetDateTime.now())
        .status(400)
        .error("Bad Request")
        .message("Invalid request body")
        .path(getPath(uri))
        .validationErrors(errors);
  }

  private static ValidationErrorDTO fromConstraintViolation(ConstraintViolation<?> violation) {
    return new ValidationErrorDTO()
        .field(violation.getPropertyPath().toString())
        .message(violation.getMessage());
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

}
