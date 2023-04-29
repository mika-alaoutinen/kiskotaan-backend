package mikaa.players.events;

public interface PlayerProducer {

  void playerAdded(PlayerPayload payload);

  void playerDeleted(PlayerPayload payload);

  void playerUpdated(PlayerPayload payload);

}
