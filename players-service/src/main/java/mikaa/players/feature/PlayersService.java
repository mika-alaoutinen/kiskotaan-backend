package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.players.domain.NewPlayer;
import mikaa.players.domain.Player;
import mikaa.players.producers.PlayerProducer;

@Service
@RequiredArgsConstructor
class PlayersService {

  private final PlayerProducer producer;
  private final PlayersRepository repository;
  private final PlayerValidator validator;

  List<Player> findAll(String nameFilter) {
    var filters = nameFilter.split(" ");

    var players = filters.length > 1
        ? repository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
            filters[0], filters[1])
        : repository.findByFirstOrLastname(filters[0]);

    return players.stream().map(PlayersService::toPlayer).toList();
  }

  Optional<Player> findOne(long id) {
    return repository.findById(id).map(PlayersService::toPlayer);
  }

  Player add(NewPlayer newPlayer) {
    var entity = fromNewPlayer(newPlayer);
    validator.validateUniqueName(entity);
    var saved = repository.save(entity);
    producer.playerAdded(toPayload(saved));
    return toPlayer(saved);
  }

  Optional<Player> update(long id, NewPlayer edited) {
    var entity = fromNewPlayer(edited);
    validator.validateUniqueName(entity);

    var saved = repository.findById(id)
        .map(player -> {
          player.setFirstName(edited.firstName());
          player.setLastName(edited.lastName());
          return player;
        }).map(repository::save);

    saved.map(PlayersService::toPayload).ifPresent(producer::playerUpdated);

    return saved.map(PlayersService::toPlayer);
  }

  void delete(long id) {
    repository.findById(id)
        .map(PlayersService::toPayload)
        .ifPresent(payload -> {
          repository.deleteById(id);
          producer.playerDeleted(payload);
        });
  }

  private static PlayerEntity fromNewPlayer(NewPlayer newPlayer) {
    return new PlayerEntity(newPlayer.firstName(), newPlayer.lastName());
  }

  private static PlayerPayload toPayload(PlayerEntity entity) {
    return new PlayerPayload(entity.getId(), entity.getFirstName(), entity.getLastName());
  }

  private static Player toPlayer(PlayerEntity entity) {
    return new Player(entity.getId(), entity.getFirstName(), entity.getLastName());
  }

}
