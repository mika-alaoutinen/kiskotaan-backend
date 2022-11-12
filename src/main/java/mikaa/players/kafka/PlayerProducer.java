package mikaa.players.kafka;

import mikaa.players.events.Player;

public interface PlayerProducer {

  void send(Player player);

}
