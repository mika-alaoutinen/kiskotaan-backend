package mikaa.errors;

import java.util.List;
import java.util.Optional;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

  private final List<ValidationError> errors;

  private ValidationException(ValidationError... errors) {
    super();
    this.errors = List.of(errors);
  }

  /**
   * Factory method for creating a new validation exception. Wraps the new
   * exception in Optional because it does not make sense to create an exception
   * without errors.
   * 
   * @param array of validation errors
   * @return new validation exception or empty, if no errors were given
   */
  public static Optional<ValidationException> from(ValidationError... errors) {
    return errors.length > 0
        ? Optional.of(new ValidationException(errors))
        : Optional.empty();
  }

  public static void maybeThrow(ValidationError... errors) {
    from(errors).ifPresent(ex -> {
      throw ex;
    });
  }

}
