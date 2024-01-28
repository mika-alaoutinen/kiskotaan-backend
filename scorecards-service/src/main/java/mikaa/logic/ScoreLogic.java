package mikaa.logic;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

public interface ScoreLogic {

  static Map<Long, PlayerScore> calculatePlayerScores(ScoreCardEntity scoreCard) {
    var playerScores = scoreCard.getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getPlayer().getExternalId()));

    return playerScores.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> toPlayerScore(entry.getValue(), scoreCard.getCourse())));
  }

  private static PlayerScore toPlayerScore(Collection<ScoreEntity> scores, CourseEntity course) {
    var entries = scores.stream().map(ScoreLogic::toScoreEntry).toList();
    var result = ScoreCalculator.result(scores, course);
    var total = ScoreCalculator.total(scores);
    return new PlayerScore(entries, result, total);
  }

  private static ScoreEntry toScoreEntry(ScoreEntity score) {
    return new ScoreEntry(score.getId(), score.getHole(), score.getScore());
  }

  static Map<Integer, ScoresByHole> calculateScoresByHole(ScoreCardEntity scoreCard) {
    var scoresByHole = scoreCard.getScores()
        .stream()
        .collect(Collectors.groupingBy(ScoreEntity::getHole));

    return scoresByHole.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            entry -> toScoresByHole(entry.getKey(), entry.getValue(), scoreCard.getCourse())));
  }

  private static ScoresByHole toScoresByHole(
      int holeNumber,
      Collection<ScoreEntity> scoresForHole,
      CourseEntity course) {

    int par = ScoreCalculator.getPar(holeNumber, course);

    var playerScores = scoresForHole.stream().collect(
        Collectors.toMap(score -> score.getPlayer().getExternalId(), score -> score.getScore()));

    return new ScoresByHole(par, playerScores);
  }

}
