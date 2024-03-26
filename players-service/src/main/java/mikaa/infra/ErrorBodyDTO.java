package mikaa.infra;

import java.time.OffsetDateTime;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
record ErrorBodyDTO(
    String error,
    String message,
    String path,
    int status,
    OffsetDateTime timestamp) {
}
