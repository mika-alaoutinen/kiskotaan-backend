package mikaa.producers;

import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.feature.results.ScoreCardInput;
import mikaa.feature.results.RoundResultCalculator;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;

interface ScoreCardMapper {

  static ScoreCardPayload toPayload(ScoreCard scoreCard) {
    var playerIds = scoreCard.players()
        .stream()
        .map(Player::id)
        .toList();

    var results = RoundResultCalculator.results(ScoreCardInput.from(scoreCard))
        .stream()
        .map(roundScore -> RoundResult.newBuilder()
            .setPlayerId(roundScore.playerId())
            .setResult(roundScore.total())
            .setTotal(roundScore.total())
            .build())
        .toList();

    var scores = scoreCard.scores()
        .stream()
        .map(score -> ScoreEntry.newBuilder()
            .setId(score.id())
            .setPlayerId(score.playerId())
            .setScoreCardId(scoreCard.id())
            .setHole(score.hole())
            .setScore(score.score())
            .build())
        .toList();

    return ScoreCardPayload.newBuilder()
        .setId(scoreCard.id())
        .setCourseId(scoreCard.course().id())
        .setPlayerIds(playerIds)
        .setResults(results)
        .setScores(scores)
        .build();
  }

}
