package mikaa.errors;

import java.util.Set;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

  private final Set<ValidationError> errors;

  public ValidationException(ValidationError... errors) {
    super();
    this.errors = Set.of(errors);
  }

}
