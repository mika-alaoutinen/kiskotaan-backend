package mikaa.feature;

import org.eclipse.microprofile.reactive.messaging.Channel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.domain.Player;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
class PlayerProducer {

  @Inject
  @Channel("player-state")
  MutinyEmitter<Record<Long, PlayerEvent>> emitter;

  Uni<Void> playerAdded(Player player) {
    return send(Action.ADD, player);
  }

  Uni<Void> playerDeleted(Player player) {
    return send(Action.DELETE, player);
  }

  Uni<Void> playerUpdated(Player player) {
    return send(Action.UPDATE, player);
  }

  private Uni<Void> send(Action action, Player player) {
    var payload = new PlayerPayload(player.id(), player.firstName(), player.lastName());
    var record = Record.of(payload.getId(), new PlayerEvent(action, payload));
    return emitter.send(record);
  }

}
