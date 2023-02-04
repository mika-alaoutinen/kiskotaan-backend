package mikaa.infra.errors;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.errors.ValidationError;

/**
 * DTO representing a validation error. Returning this should result in a 400
 * Bad Request response.
 */
@RegisterForReflection
record ValidationErrorBody(
    LocalDateTime timestamp,
    int status,
    String error,
    String path,
    List<ValidationError> validationErrors) {

  ValidationErrorBody(String path, List<ValidationError> validationErrors) {
    this(LocalDateTime.now(), 400, "Bad Request", path, validationErrors);
  }

}
