package mikaa.feature;

import java.util.Collection;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.domain.NewPlayer;
import mikaa.domain.Player;
import mikaa.uni.UniCollection;
import mikaa.uni.UniItem;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService {

  private final PlayerProducer producer;
  private final PlayerRepository repository;
  private final PlayerValidator validator;

  @WithSession
  Uni<Collection<Player>> findAll(String nameFilter) {
    var filters = nameFilter.split(" ");

    var players = filters.length > 1
        ? repository.findByFirstAndLastname(filters[0], filters[1])
        : repository.findByFirstOrLastname(filters[0]);

    return UniCollection.fromList(players)
        .map(PlayerService::fromEntity)
        .unwrap();
  }

  @WithSession
  Uni<Player> findOne(long id) {
    return UniItem.from(repository.findById(id))
        .map(PlayerService::fromEntity)
        .orThrow(new NotFoundException("Could not find player with id " + id));
  }

  @WithTransaction
  Uni<Player> add(NewPlayer newPlayer) {
    return UniItem.from(validator.validateUniqueName(newPlayer))
        .map(PlayerService::fromNewPlayer)
        .flatMap(repository::persistAndFlush) // flush to create ID for entity
        .map(PlayerService::fromEntity)
        .call(producer::playerAdded)
        .unwrap();
  }

  @WithTransaction
  Uni<Player> updateName(long id, NewPlayer updated) {
    return UniItem.from(repository.findById(id))
        .call(x -> validator.validateUniqueName(updated))
        .map(entity -> {
          entity.setFirstName(updated.firstName());
          entity.setLastName(updated.lastName());
          return entity;
        })
        .flatMap(repository::persist)
        .map(PlayerService::fromEntity)
        .call(producer::playerUpdated)
        .orThrow(new NotFoundException("Could not find player with id " + id));
  }

  @WithTransaction
  Uni<Void> delete(long id) {
    return UniItem.from(repository.deleteById(id))
        .map(PlayerService::fromEntity)
        .call(producer::playerDeleted)
        .discard();
  }

  private static Player fromEntity(PlayerEntity entity) {
    return new Player(entity.getId(), entity.getFirstName(), entity.getLastName());
  }

  private static PlayerEntity fromNewPlayer(NewPlayer newPlayer) {
    return new PlayerEntity(newPlayer.firstName(), newPlayer.lastName());
  }

}
