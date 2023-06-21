package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

public interface ScoreLogic {

  static Map<Long, PlayerScore> calculatePlayerScores(ScoreCardEntity scoreCard) {
    var course = scoreCard.getCourse();
    var playerScores = scoresByPlayers(scoreCard.getScores());

    return playerScores.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> toPlayerScore(entry.getValue(), course)));
  }

  private static Map<Long, List<ScoreEntity>> scoresByPlayers(Collection<ScoreEntity> scores) {
    return scores.stream().collect(
        Collectors.groupingBy(score -> score.getPlayer().getExternalId()));
  }

  private static PlayerScore toPlayerScore(Collection<ScoreEntity> playerScores, CourseEntity course) {
    var entries = playerScores.stream().map(ScoreLogic::toScoreEntry).toList();
    var result = ScoreCalculator.result(playerScores, course);
    var total = ScoreCalculator.total(playerScores);
    return new PlayerScore(entries, result, total);
  }

  private static ScoreEntry toScoreEntry(ScoreEntity score) {
    return new ScoreEntry(score.getId(), score.getHole(), score.getScore());
  }

}
