package mikaa.infra;

import java.time.OffsetDateTime;

record ErrorBody(
    String error,
    String message,
    String path,
    int status,
    OffsetDateTime timestamp) {

  static ErrorBody notFound(String message, String path) {
    return new ErrorBody("Not Found", message, path, 404, OffsetDateTime.now());
  }

}
