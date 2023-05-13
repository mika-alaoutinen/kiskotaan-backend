package mikaa.queries.player;

import java.util.List;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.MockData;
import mikaa.queries.dto.PlayerDTO;

@Path("players")
@RequiredArgsConstructor
public class PlayerResource {

  @GET
  @Path("/{id}")
  public Uni<PlayerDTO> getPlayer(int id) {
    return Uni.createFrom().item(MockData.PLAYER);
  }

  @GET
  public Uni<List<PlayerDTO>> getPlayers(@RestQuery String name) {
    return Uni.createFrom().item(List.of(MockData.PLAYER));
  }

}
