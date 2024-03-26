package mikaa.errors;

import java.util.List;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

  private final List<ValidationError> errors;

  public ValidationException(ValidationError... errors) {
    super();
    this.errors = List.of(errors);
  }

}
