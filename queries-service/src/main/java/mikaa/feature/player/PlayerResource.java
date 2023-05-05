package mikaa.feature.player;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerResource {

  public Uni<String> hello() {
    return Uni.createFrom().item("Reactive players");
  }

  public void getPlayer(int id) {
    throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
  }

  public Multi<Void> getPlayers(String name) {
    throw new UnsupportedOperationException("Unimplemented method 'getPlayers'");
  }

}
