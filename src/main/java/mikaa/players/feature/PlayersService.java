package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class PlayersService {

  private final PlayersRepository repository;

  List<Player> findAll() {
    return repository.findAll();
  }

  Optional<Player> findOne(long id) {
    return repository.findById(id);
  }

  Player add(Player newPlayer) {
    return repository.save(newPlayer);
  }

  void delete(long id) {
    repository.findById(id).ifPresent(repository::delete);
  }
}
