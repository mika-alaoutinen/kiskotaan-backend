package mikaa.events.scorecard;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class KafkaProducer implements ScoreCardProducer {

  @Inject
  @Channel("scorecards-out")
  Emitter<ScoreCardEvent> emitter;

  @Override
  public void scoreCardAdded(ScoreCardPayload payload) {
    send(new ScoreCardEvent(ScoreCardEventType.SCORECARD_ADDED, payload));
  }

  @Override
  public void scoreCardDeleted(ScoreCardPayload payload) {
    send(new ScoreCardEvent(ScoreCardEventType.SCORECARD_DELETED, payload));
  }

  private void send(ScoreCardEvent event) {
    emitter.send(event).toCompletableFuture().join();
  }

}
