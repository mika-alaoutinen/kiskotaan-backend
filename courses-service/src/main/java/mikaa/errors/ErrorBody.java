package mikaa.errors;

import java.time.LocalDateTime;

public record ErrorBody(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path) {
}
