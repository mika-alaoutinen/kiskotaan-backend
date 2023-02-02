package mikaa.players.feature;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.players.errors.BadRequestException;

@Component
@RequiredArgsConstructor
class PlayerValidator {

  private final PlayersRepository repository;

  void validateUniqueName(PlayerEntity player) {
    if (repository.existsPlayerByFirstNameAndLastName(player.getFirstName(), player.getLastName())) {
      throw new BadRequestException("Found existing player with the same name");
    }
  }

}
