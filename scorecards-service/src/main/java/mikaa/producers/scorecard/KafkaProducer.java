package mikaa.producers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.ScoreCardPayload;
import mikaa.config.OutgoingChannels;

@ApplicationScoped
class KafkaProducer implements ScoreCardProducer {

  @Inject
  @Channel(OutgoingChannels.ScoreCard.SCORECARD_ADDED)
  Emitter<ScoreCardPayload> addEmitter;

  @Inject
  @Channel(OutgoingChannels.ScoreCard.SCORECARD_DELETED)
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
