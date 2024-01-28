package mikaa.producers;

import io.smallrye.reactive.messaging.annotations.Broadcast;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.config.InternalChannels;
import mikaa.feature.scorecard.ScoreCardEntity;

/**
 * Broadcasts events to an internal channel, where other producers then publish
 * them to outgoing topics in different formats.
 */
@ApplicationScoped
class ScoreCardEventProcessor implements ScoreCardProducer {

  @Broadcast
  @Channel(InternalChannels.SCORECARD_STATE)
  @Inject
  Emitter<ScoreCardRecord> emitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    send(Action.ADD, entity);
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    send(Action.DELETE, entity);
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    send(Action.UPDATE, entity);
  }

  private void send(Action action, ScoreCardEntity entity) {
    emitter.send(new ScoreCardRecord(action, entity));
  }

}
