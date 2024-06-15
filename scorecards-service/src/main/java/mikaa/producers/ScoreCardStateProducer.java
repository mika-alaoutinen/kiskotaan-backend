package mikaa.producers;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.domain.ScoreCard;

@ApplicationScoped
class ScoreCardStateProducer implements ScoreCardProducer {

  @Inject
  @Channel(ScoreCardProducer.SCORECARD_STATE)
  Emitter<Record<Long, ScoreCardEvent>> emitter;

  @Override
  public void scoreCardAdded(ScoreCard scoreCard) {
    sendEvent(Action.ADD, scoreCard);
  }

  @Override
  public void scoreCardDeleted(ScoreCard scoreCard) {
    sendEvent(Action.DELETE, scoreCard);
  }

  @Override
  public void scoreCardUpdated(ScoreCard scoreCard) {
    sendEvent(Action.UPDATE, scoreCard);
  }

  private void sendEvent(Action action, ScoreCard scoreCard) {
    var event = new ScoreCardEvent(action, ScoreCardMapper.toPayload(scoreCard));
    var record = Record.of(scoreCard.id(), event);
    emitter.send(record).toCompletableFuture().join();
  }

}
