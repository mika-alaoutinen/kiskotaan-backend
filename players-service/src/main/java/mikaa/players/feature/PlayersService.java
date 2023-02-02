package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mikaa.players.events.Player;
import mikaa.players.events.PlayerEvents;
import mikaa.players.kafka.KafkaProducer;

@Service
@RequiredArgsConstructor
class PlayersService {

  private final KafkaProducer producer;
  private final PlayersRepository repository;
  private final PlayerValidator validator;

  List<PlayerEntity> findAll() {
    return repository.findAll();
  }

  Optional<PlayerEntity> findOne(long id) {
    return repository.findById(id);
  }

  PlayerEntity add(PlayerEntity newPlayer) {
    validator.validateUniqueName(newPlayer);

    var saved = repository.save(newPlayer);
    var player = toPlayer(saved);

    producer.send(PlayerEvents.add(player));
    return saved;
  }

  Optional<PlayerEntity> update(long id, PlayerEntity edited) {
    validator.validateUniqueName(edited);

    var saved = repository.findById(id)
        .map(player -> {
          player.setFirstName(edited.getFirstName());
          player.setLastName(edited.getLastName());
          return player;
        }).map(repository::save);

    saved.map(PlayersService::toPlayer)
        .map(PlayerEvents::update)
        .ifPresent(producer::send);

    return saved;
  }

  void delete(long id) {
    repository.findById(id)
        .map(PlayersService::toPlayer)
        .map(PlayerEvents::delete)
        .ifPresent(event -> {
          repository.deleteById(id);
          producer.send(event);
        });
  }

  private static Player toPlayer(PlayerEntity entity) {
    return new Player(entity.getId(), entity.getFirstName(), entity.getLastName());
  }

}
