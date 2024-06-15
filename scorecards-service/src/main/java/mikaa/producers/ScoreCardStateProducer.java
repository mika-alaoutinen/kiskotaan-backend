package mikaa.producers;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;
import mikaa.logic.ScoreCardInput;
import mikaa.logic.ScoreLogic;
import mikaa.config.OutgoingChannels;
import mikaa.domain.Player;
import mikaa.domain.Score;
import mikaa.domain.ScoreCard;

@ApplicationScoped
class ScoreCardStateProducer implements ScoreCardProducer {

  @Inject
  @Channel(OutgoingChannels.SCORECARD_STATE)
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
    var event = new ScoreCardEvent(action, toPayload(scoreCard));
    var record = Record.of(scoreCard.id(), event);
    emitter.send(record).toCompletableFuture().join();
  }

  private ScoreCardPayload toPayload(ScoreCard scoreCard) {
    var playerIds = scoreCard.players().stream().map(Player::id).toList();

    var results = ScoreLogic.results(ScoreCardInput.from(scoreCard))
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> new RoundResult(entry.getValue().result(), entry.getValue().total())));

    var scores = scoreCard.scores()
        .stream()
        .map(score -> mapScore(scoreCard.id(), score))
        .toList();

    return new ScoreCardPayload(
        scoreCard.id(),
        scoreCard.course().id(),
        playerIds,
        results,
        scores);
  }

  private static ScoreEntry mapScore(long scoreCardId, Score score) {
    return new ScoreEntry(
        score.id(),
        score.playerId(),
        scoreCardId,
        score.hole(),
        score.score());
  }

}
