package mikaa.producers;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.domain.Score;
import mikaa.kiskotaan.scorecard.ScoreEntry;

@ApplicationScoped
class ScoreStateProducer implements ScoreProducer {

  @Inject
  @Channel(ScoreProducer.SCORES_CHANNEL)
  Emitter<Record<Long, ScoreEntry>> emitter;

  @Override
  public void scoreAdded(long scoreCardId, Score score) {
    var entry = new ScoreEntry(score.id(), score.playerId(), scoreCardId, score.hole(), score.score());
    var record = Record.of(score.id(), entry);
    emitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void scoreDeleted(long scoreId) {
    emitter.send(Record.of(scoreId, null)).toCompletableFuture().join();
  }

}
