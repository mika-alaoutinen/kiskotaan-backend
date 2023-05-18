package mikaa.queries.player;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.feature.player.PlayerEntity;
import mikaa.queries.dto.PlayerDTO;

@Path("players")
@RequiredArgsConstructor
public class PlayerResource {

  private final PlayerReader players;

  @GET
  @Path("/{id}")
  public Uni<PlayerDTO> getPlayer(int id) {
    return players.findOne(id).map(PlayerResource::toDto);
  }

  @GET
  public Multi<PlayerDTO> getPlayers(@RestQuery String name) {
    return players.findAll().map(PlayerResource::toDto);
  }

  private static PlayerDTO toDto(PlayerEntity entity) {
    return new PlayerDTO(entity.getExternalId(), entity.getFirstName(), entity.getLastName());
  }

}
