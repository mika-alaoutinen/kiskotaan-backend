package mikaa.players.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.players.domain.Player;

@Component
@RequiredArgsConstructor
class KafkaProducer implements PlayerProducer {

  private final KafkaTemplate<Long, PlayerEvent> template;

  @Override
  public void playerAdded(Player player) {
    send(Action.ADD, player);
  }

  @Override
  public void playerDeleted(Player player) {
    send(Action.DELETE, player);
  }

  @Override
  public void playerUpdated(Player player) {
    send(Action.UPDATE, player);
  }

  private void send(Action action, Player player) {
    var payload = new PlayerPayload(player.id(), player.firstName(), player.lastName());
    var event = new PlayerEvent(action, payload);
    template.send(PlayerTopics.PLAYER_STATE, payload.getId(), event);
  }

}
