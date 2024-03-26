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

  Uni<NewPlayer> validateUniqueName(NewPlayer newPlayer) {
    return repository.existsByFirstNameAndLastName(newPlayer.firstName(), newPlayer.lastName())
        .invoke(exists -> {
          if (exists) {
            var error = new ValidationError("firstName, lastName", "Found existing player with the same name.");
            throw new ValidationException(error);
          }
        }).replaceWith(newPlayer);
  }

}
