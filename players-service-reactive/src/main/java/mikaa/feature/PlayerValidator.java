package mikaa.feature;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.NewPlayer;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerValidator {

  private final PlayerRepository repository;

  Uni<NewPlayer> validate(NewPlayer newPlayer) {
    return validateUniqueName(newPlayer);
  }

  private Uni<NewPlayer> validateUniqueName(NewPlayer newPlayer) {
    return repository.existsByFirstNameAndLastName(newPlayer.firstName(), newPlayer.lastName())
        .invoke(exists -> {
          if (exists) {
            var error = new ValidationError("player", "Found existing player with the same name");
            ValidationException.maybeThrow(error);
          }
        }).replaceWith(newPlayer);
  }

}
