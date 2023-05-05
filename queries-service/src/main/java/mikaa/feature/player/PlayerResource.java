package mikaa.feature.player;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("players")
@RequiredArgsConstructor
public class PlayerResource {

  @GET
  @Path("/{id}")
  public Uni<String> getPlayer(int id) {
    return Uni.createFrom().item("get one");
  }

  @GET
  public Multi<String> getPlayers(@RestQuery String name) {
    return Multi.createFrom().item("get many");
  }

}
