package mikaa.producers;

import java.util.stream.Collectors;

import mikaa.domain.Player;
import mikaa.domain.Score;
import mikaa.domain.ScoreCard;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;
import mikaa.logic.ScoreCardInput;
import mikaa.logic.ScoreLogic;

interface ScoreCardMapper {

  static ScoreCardPayload toPayload(ScoreCard scoreCard) {
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
