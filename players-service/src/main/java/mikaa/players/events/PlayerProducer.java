package mikaa.players.events;

import mikaa.PlayerPayload;

public interface PlayerProducer {

  void playerAdded(PlayerPayload payload);

  void playerDeleted(PlayerPayload payload);

  void playerUpdated(PlayerPayload payload);

}
