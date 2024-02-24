package mikaa.players.producers;

import mikaa.players.domain.Player;

public interface PlayerProducer {

  void playerAdded(Player player);

  void playerDeleted(Player player);

  void playerUpdated(Player player);

}
