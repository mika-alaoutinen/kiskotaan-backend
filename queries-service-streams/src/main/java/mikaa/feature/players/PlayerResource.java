package mikaa.feature.players;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.domain.Player;

// Everything in the resource class has to be public for smallrye-graphql to work
@ApplicationScoped
@GraphQLApi
@RequiredArgsConstructor
@Slf4j
public class PlayerResource {

  private final PlayerService service;

  @Description("Find all players. Players can also be filtered by first name and/or lastname.")
  @Query
  public Collection<Player> players(PlayerQueryFilters filters) {
    log.info("Querying players with filters " + filters);
    return service.streamPlayers(filters);
  }

  @Description("Find player by id.")
  @Query
  public Optional<Player> player(long id) {
    return service.findPlayer(id);
  }

}
