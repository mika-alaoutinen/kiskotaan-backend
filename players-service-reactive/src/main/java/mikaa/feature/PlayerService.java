package mikaa.feature;

import java.util.List;

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

  private final PlayerRepository repository;

  Uni<List<Player>> findAll() {
    UniCollection.from(repository.listAll());

    return repository.listAll().map(
        players -> players.stream().map(PlayerService::fromEntity).toList());
  }

  Uni<Player> findOne(long id) {
    return UniItem.from(repository.findById(id))
        .map(PlayerService::fromEntity)
        .orThrow(new NotFoundException("Could not find player with ID " + id));
  }

  Uni<Player> add(NewPlayer newPlayer) {
    // How to do validation logic?
    return UniItem.from(Uni.createFrom().item(fromNewPlayer(newPlayer)))
        .flatMap(repository::persist)
        .map(PlayerService::fromEntity)
        .unwrap();
  }

  Uni<Player> updateName(long id, NewPlayer updated) {
    return UniItem.from(repository.findById(id))
        .map(entity -> {
          entity.setFirstName(updated.firstName());
          entity.setLastName(updated.lastName());
          return entity;
        })
        .flatMap(repository::persist)
        .map(PlayerService::fromEntity)
        .orThrow(new NotFoundException("Could not find player with ID " + id));
  }

  Uni<Void> delete(long id) {
    return repository.deleteById(id).replaceWithVoid();
  }

  private static Player fromEntity(PlayerEntity entity) {
    return new Player(entity.getId(), entity.getFirstName(), entity.getLastName());
  }

  private PlayerEntity fromNewPlayer(NewPlayer newPlayer) {
    return new PlayerEntity(newPlayer.firstName(), newPlayer.lastName());
  }

}
