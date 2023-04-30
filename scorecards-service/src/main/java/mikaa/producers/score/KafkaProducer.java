package mikaa.producers.score;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.ScorePayload;
import mikaa.config.OutgoingChannels;

@ApplicationScoped
class KafkaProducer implements ScoreProducer {

  @Inject
  @Channel(OutgoingChannels.Score.SCORE_ADDED)
  Emitter<ScorePayload> addEmitter;

  @Inject
  @Channel(OutgoingChannels.Score.SCORE_DELETED)
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
