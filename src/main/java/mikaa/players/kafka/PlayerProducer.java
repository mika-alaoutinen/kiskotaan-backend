package mikaa.players.kafka;

import mikaa.players.events.PlayerEvents.PlayerEvent;

public interface PlayerProducer {

  void send(PlayerEvent event);

}
