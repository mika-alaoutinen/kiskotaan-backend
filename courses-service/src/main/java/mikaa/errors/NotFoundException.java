package mikaa.errors;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
  private final String path;

  public NotFoundException(String message) {
    super(message);
    this.path = "";
  }

  public NotFoundException(String message, String path) {
    super(message);
    this.path = path;
  }

}
