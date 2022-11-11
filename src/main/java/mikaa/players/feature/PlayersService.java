package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class PlayersService {

  private final PlayersRepository repository;

  List<Player> getAll() {
    return repository.findAll();
  }

  Optional<Player> findOne(long id) {
    return repository.findById(id);
  }
}
