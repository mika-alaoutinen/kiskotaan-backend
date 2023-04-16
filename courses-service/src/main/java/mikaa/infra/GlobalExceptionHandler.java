package mikaa.infra;

import java.time.OffsetDateTime;
import java.util.List;

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
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;
import mikaa.model.ErrorBodyDTO;
import mikaa.model.ValidationErrorBodyDTO;
import mikaa.model.ValidationErrorDTO;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
class GlobalExceptionHandler {

  private final UriInfo uri;

  @ServerExceptionMapper(ConstraintViolationException.class)
  RestResponse<ValidationErrorBodyDTO> handleConstraintViolation(ConstraintViolationException ex) {
    log.info(ex.getMessage());

    var errors = ex.getConstraintViolations()
        .stream()
        .map(GlobalExceptionHandler::fromConstraintViolation)
        .toList();

    var body = validationErrorBody(errors, uri);

    return RestResponse.status(Status.BAD_REQUEST, body);
  }

  @ServerExceptionMapper(NotFoundException.class)
  RestResponse<ErrorBodyDTO> handleNotFound(NotFoundException ex) {
    var msg = ex.getMessage();
    var path = getPath(uri);
    var body = new ErrorBodyDTO()
        .error("Not Found")
        .message(msg)
        .path(path)
        .status(404)
        .timestamp(OffsetDateTime.now());

    log.info(msg);

    return RestResponse.status(Status.NOT_FOUND, body);
  }

  @ServerExceptionMapper(ValidationException.class)
  RestResponse<ValidationErrorBodyDTO> handleValidation(ValidationException ex) {
    log.info(ex.getMessage());

    var errors = ex.getErrors()
        .stream()
        .map(GlobalExceptionHandler::fromValidationError)
        .toList();

    var body = validationErrorBody(errors, uri);

    return RestResponse.status(Status.BAD_REQUEST, body);
  }

  private static ValidationErrorBodyDTO validationErrorBody(List<ValidationErrorDTO> errors, UriInfo uri) {
    return new ValidationErrorBodyDTO()
        .timestamp(OffsetDateTime.now())
        .status(400)
        .error("Bad Request")
        .message("Invalid request body")
        .path(getPath(uri))
        .validationErrors(errors);
  }

  // Constraint violations have a path of methodName.objectName.objectField.
  // Return objectName.objectField as error field.
  private static ValidationErrorDTO fromConstraintViolation(ConstraintViolation<?> violation) {
    var path = violation.getPropertyPath().toString();
    var field = path.substring(path.indexOf(".") + 1);

    return new ValidationErrorDTO()
        .field(field)
        .message(violation.getMessage());
  }

  private static ValidationErrorDTO fromValidationError(ValidationError error) {
    return new ValidationErrorDTO()
        .field(error.field())
        .message(error.message());
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

}
