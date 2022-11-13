package mikaa.players.kafka;

import mikaa.players.events.PlayerEvents.PlayerEvent;

public interface KafkaProducer {

  void send(PlayerEvent event);

}
