package mikaa.errors;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ValidationErrorBody(
    LocalDateTime timestamp,
    int status,
    String error,
    List<ValidationError> validationErrors) {

  public ValidationErrorBody(List<ValidationError> validationErrors) {
    this(LocalDateTime.now(), 400, "Bad Request", validationErrors);
  }

}
