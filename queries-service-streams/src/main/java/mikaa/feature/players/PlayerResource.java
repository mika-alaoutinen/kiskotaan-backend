package mikaa.feature.players;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.Player;

@ApplicationScoped
@GraphQLApi
@RequiredArgsConstructor
public class PlayerResource {

  private static final QueryFilter EMPTY_FILTER = new QueryFilter("");

  private final PlayerService service;

  @Description("Find all players. Players can also be filtered by first name and/or lastname.")
  @Query
  public Collection<Player> players(QueryFilter filters) {
    return service.streamPlayers();
  }

  @Description("Find player by id.")
  @Query
  public Optional<Player> player(long id) {
    return service.findPlayer(id);
  }

  public static record QueryFilter(String name) {
  }

}
