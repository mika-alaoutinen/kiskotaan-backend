package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mikaa.players.events.Player;
import mikaa.players.events.PlayerEvents;
import mikaa.players.kafka.PlayerProducer;

@Service
@RequiredArgsConstructor
class PlayersService {

  private final PlayerProducer producer;
  private final PlayersRepository repository;

  List<PlayerEntity> findAll() {
    return repository.findAll();
  }

  Optional<PlayerEntity> findOne(long id) {
    return repository.findById(id);
  }

  @Transactional
  PlayerEntity add(PlayerEntity newPlayer) {
    var saved = repository.save(newPlayer);
    var player = new Player(saved.getId(), saved.getFirstName(), saved.getLastName());
    producer.send(PlayerEvents.add(player));
    return saved;
  }

  Optional<PlayerEntity> update(long id, PlayerEntity edited) {
    return repository.findById(id)
        .map(player -> {
          player.setFirstName(edited.getFirstName());
          player.setLastName(edited.getLastName());
          return player;
        }).map(repository::save);
  }

  void delete(long id) {
    repository.findById(id).ifPresent(repository::delete);
  }
}
