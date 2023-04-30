package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mikaa.PlayerPayload;
import mikaa.players.events.PlayerProducer;

@Service
@RequiredArgsConstructor
class PlayersService {

  private final PlayerProducer producer;
  private final PlayersRepository repository;
  private final PlayerValidator validator;

  List<PlayerEntity> findAll(String nameFilter) {
    var filters = nameFilter.split(" ");

    return filters.length > 1
        ? repository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
            filters[0], filters[1])
        : repository.findByFirstOrLastname(filters[0]);
  }

  Optional<PlayerEntity> findOne(long id) {
    return repository.findById(id);
  }

  PlayerEntity add(PlayerEntity newPlayer) {
    validator.validateUniqueName(newPlayer);
    var saved = repository.save(newPlayer);
    producer.playerAdded(toPayload(saved));
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

    saved.map(PlayersService::toPayload).ifPresent(producer::playerUpdated);

    return saved;
  }

  void delete(long id) {
    repository.findById(id)
        .map(PlayersService::toPayload)
        .ifPresent(payload -> {
          repository.deleteById(id);
          producer.playerDeleted(payload);
        });
  }

  private static PlayerPayload toPayload(PlayerEntity entity) {
    return new PlayerPayload(entity.getId(), entity.getFirstName(), entity.getLastName());
  }

}
