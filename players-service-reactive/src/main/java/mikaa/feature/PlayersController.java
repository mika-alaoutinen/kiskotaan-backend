package mikaa.feature;

import java.util.Collection;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.domain.NewPlayer;
import mikaa.domain.Player;

@ApplicationScoped
@Path("players")
@RequiredArgsConstructor
class PlayersController {

  private final PlayerService service;

  @GET
  Uni<Collection<Player>> findPlayers() {
    return service.findAll();
  }

  @POST
  Uni<Player> addPlayer(@Valid NewPlayer newPlayer) {
    return service.add(newPlayer);
  }

  @GET
  @Path("{id}")
  Uni<Player> findPlayer(long id) {
    return service.findOne(id);
  }

  @DELETE
  @Path("{id}")
  Uni<Void> deletePlayer(long id) {
    return service.delete(id);
  }

  @POST
  @Path("{id}")
  Uni<Player> updatePlayerName(long id, @Valid NewPlayer updatedPlayer) {
    return service.updateName(id, updatedPlayer);
  }

}
