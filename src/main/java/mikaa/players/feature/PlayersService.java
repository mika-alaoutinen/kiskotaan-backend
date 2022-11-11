package mikaa.players.feature;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class PlayersService {

  private final PlayersRepository repository;

  List<Player> getAll() {
    return repository.findAll();
  }
}
