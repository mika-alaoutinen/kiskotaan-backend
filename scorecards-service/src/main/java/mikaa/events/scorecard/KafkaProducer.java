package mikaa.events.scorecard;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class KafkaProducer implements ScoreCardProducer {

  @Inject
  @Channel("scorecard-added")
  Emitter<ScoreCardPayload> addEmitter;

  @Inject
  @Channel("scorecard-deleted")
  Emitter<ScoreCardPayload> deleteEmitter;

  @Override
  public void scoreCardAdded(ScoreCardPayload payload) {
    addEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void scoreCardDeleted(ScoreCardPayload payload) {
    deleteEmitter.send(payload).toCompletableFuture().join();
  }

}
