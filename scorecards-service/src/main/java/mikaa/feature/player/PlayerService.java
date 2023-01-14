package mikaa.feature.player;

import java.math.BigDecimal;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PlayerService {

  private final PlayerRepository repository;

  public PlayerEntity findOrThrow(BigDecimal id) {
    long playerId = id.longValue();
    return repository.findByIdOptional(playerId).orElseThrow(() -> notFound(playerId));
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find player with id " + id);
  }

}
