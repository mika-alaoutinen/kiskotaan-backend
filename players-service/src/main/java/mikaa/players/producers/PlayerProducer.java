package mikaa.players.producers;

import mikaa.kiskotaan.player.PlayerPayload;

public interface PlayerProducer {

  void playerAdded(PlayerPayload payload);

  void playerDeleted(PlayerPayload payload);

  void playerUpdated(PlayerPayload payload);

}
