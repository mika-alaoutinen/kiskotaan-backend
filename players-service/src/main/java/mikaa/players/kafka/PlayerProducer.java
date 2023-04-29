package mikaa.players.kafka;

import mikaa.players.events.PlayerPayload;

public interface PlayerProducer {

  void playerAdded(PlayerPayload payload);

  void playerDeleted(PlayerPayload payload);

  void playerUpdated(PlayerPayload payload);

}
