package mikaa.infra;

import java.time.OffsetDateTime;
import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
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
  RestResponse<ValidationErrorDTO> handleConstraintViolation(ConstraintViolationException ex) {
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
    var body = new ErrorBodyDTO("Not Found", msg, path, 404, OffsetDateTime.now());
    log.info(msg);

    return RestResponse.status(Status.NOT_FOUND, body);
  }

  private static String getPath(UriInfo uri) {
    return uri.getRequestUri().getPath();
  }

  @ServerExceptionMapper(ValidationException.class)
  RestResponse<ValidationErrorDTO> handleValidation(ValidationException ex) {
    log.info(ex.getMessage());
    var body = validationErrorBody(ex.getErrors(), uri);

    return RestResponse.status(Status.BAD_REQUEST, body);
  }

  // Constraint violations have a path of methodName.objectName.objectField.
  // Return objectName.objectField as error field.
  private static ValidationError fromConstraintViolation(ConstraintViolation<?> violation) {
    var path = violation.getPropertyPath().toString();
    var field = path.substring(path.indexOf(".") + 1);
    return new ValidationError(field, violation.getMessage());
  }

  private static ValidationErrorDTO validationErrorBody(List<ValidationError> errors, UriInfo uri) {
    return new ValidationErrorDTO(
        "Bad Request",
        "Invalid request body",
        getPath(uri),
        400,
        OffsetDateTime.now(),
        errors);
  }

}
