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
public class PlayersController {

  private final PlayerService service;

  @GET
  public Uni<Collection<Player>> findPlayers() {
    return service.findAll();
  }

  @POST
  public Uni<Player> addPlayer(@Valid NewPlayer newPlayer) {
    return service.add(newPlayer);
  }

  @GET
  @Path("{id}")
  public Uni<Player> findPlayer(long id) {
    return service.findOne(id);
  }

  @DELETE
  @Path("{id}")
  public Uni<Void> deletePlayer(long id) {
    return service.delete(id);
  }

  @POST
  @Path("{id}")
  public Uni<Player> updatePlayerName(long id, @Valid NewPlayer updatedPlayer) {
    return service.updateName(id, updatedPlayer);
  }

}
