package mikaa.infra.errors;

import java.time.LocalDateTime;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
record ErrorBody(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path) {

  ErrorBody(int status, String error, String message, String path) {
    this(LocalDateTime.now(), status, error, message, path);
  }

}
