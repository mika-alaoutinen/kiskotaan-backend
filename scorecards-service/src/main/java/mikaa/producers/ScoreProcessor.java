package mikaa.producers;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreEntry;

@ApplicationScoped
class ScoreProcessor {

  static final String SCORE_ENTRIES = "scores-incoming";

  // Use emitter instead of @Outgoing channel to have access to a db transaction
  @Inject
  @Channel(ScoreCardProducer.SCORECARD_STATE)
  Emitter<Record<Long, ScoreCardEvent>> emitter;

  @Inject
  private ScoreCardFinder scoreCardFinder;

  @Incoming(SCORE_ENTRIES)
  @Transactional
  void sendScoreCardState(ScoreEntry score) {
    long scoreCardId = score.getScoreCardId();
    var scoreCard = scoreCardFinder.findByIdOrThrow(scoreCardId);
    var payload = ScoreCardMapper.toPayload(scoreCard);
    var record = Record.of(scoreCardId, new ScoreCardEvent(Action.UPDATE, payload));
    emitter.send(record).toCompletableFuture().join();
  }

}
