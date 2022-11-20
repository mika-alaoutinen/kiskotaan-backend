package mikaa.errors;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ValidationErrorBody(
    LocalDateTime timestamp,
    int status,
    String error,
    String path,
    List<ValidationError> validationErrors) {

  public ValidationErrorBody(String path, List<ValidationError> validationErrors) {
    this(LocalDateTime.now(), 400, "Bad Request", path, validationErrors);
  }

}
