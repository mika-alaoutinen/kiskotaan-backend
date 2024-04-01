package mikaa.feature.players;

import java.util.Collection;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.Player;
import mikaa.kiskotaan.domain.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService {

  private final PlayerStore store;

  Collection<Player> streamPlayers() {
    return store.streamAll().map(PlayerService::fromPayload).toList();
  }

  private static Player fromPayload(PlayerPayload p) {
    return new Player(p.getId(), p.getFirstName(), p.getLastName());
  }

}
