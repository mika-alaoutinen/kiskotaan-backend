package mikaa.feature.players;

import java.util.Collection;

import org.eclipse.microprofile.graphql.GraphQLApi;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.Player;

@ApplicationScoped
@GraphQLApi
@RequiredArgsConstructor
public class PlayerResource {

  private final PlayerService service;

  public Collection<Player> players() {
    return service.streamPlayers();
  }

}
