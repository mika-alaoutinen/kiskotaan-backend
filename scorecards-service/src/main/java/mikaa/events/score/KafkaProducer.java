package mikaa.events.score;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class KafkaProducer implements ScoreProducer {

  @Inject
  @Channel("scores-out")
  Emitter<ScoreEvent> emitter;

  @Override
  public void scoreAdded(ScorePayload payload) {
    send(new ScoreEvent(ScoreEventType.SCORE_ADDED, payload));
  }

  @Override
  public void scoreDeleted(ScorePayload payload) {
    send(new ScoreEvent(ScoreEventType.SCORE_DELETED, payload));
  }

  private void send(ScoreEvent event) {
    emitter.send(event).toCompletableFuture().join();
  }

}
