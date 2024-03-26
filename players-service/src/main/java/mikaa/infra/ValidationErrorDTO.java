package mikaa.infra;

import java.time.OffsetDateTime;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.errors.ValidationError;

@RegisterForReflection
record ValidationErrorDTO(
    String error,
    String message,
    String path,
    int status,
    OffsetDateTime timestamp,
    List<ValidationError> validationErrors) {
}
