package mikaa.players.kafka;

import mikaa.players.events.PlayerEvents.PlayerEvent;

public interface KafkaConsumer {

  void consume(PlayerEvent event);

}
