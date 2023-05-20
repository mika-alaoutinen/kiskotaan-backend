package mikaa.players.producers;

import mikaa.kiskotaan.domain.PlayerPayload;

public interface PlayerProducer {

  void playerAdded(PlayerPayload payload);

  void playerDeleted(PlayerPayload payload);

  void playerUpdated(PlayerPayload payload);

}
