package mikaa.events.score;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class KafkaProducer implements ScoreProducer {

  @Inject
  @Channel("score-added")
  Emitter<ScorePayload> addEmitter;

  @Inject
  @Channel("score-deleted")
  Emitter<ScorePayload> deleteEmitter;

  @Override
  public void scoreAdded(ScorePayload payload) {
    addEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void scoreDeleted(ScorePayload payload) {
    deleteEmitter.send(payload).toCompletableFuture().join();
  }

}
