package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

public interface ScoreLogic {

  static ScoresByPlayer calculateScoresByPlayer(ScoreCardEntity scoreCard) {
    var results = calculateRoundScores(scoreCard);

    var playerScores = scoreCard.getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getPlayer().getExternalId()));

    var scores = playerScores.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> mapScoreEntries(entry.getValue())));

    return new ScoresByPlayer(results, scores);
  }

  private static List<ScoreEntry> mapScoreEntries(Collection<ScoreEntity> scores) {
    return scores.stream()
        .map(score -> new ScoreEntry(
            score.getId(),
            score.getPlayer().getExternalId(),
            score.getHole(),
            score.getScore()))
        .toList();
  }

  static ScoresByHole calculateScoresByHole(ScoreCardEntity scoreCard) {
    var results = calculateRoundScores(scoreCard);

    var scoresByHole = scoreCard.getScores()
        .stream()
        .collect(Collectors.groupingBy(ScoreEntity::getHole));

    var scores = scoresByHole.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            entry -> toScoresByHole(entry.getValue())));

    return new ScoresByHole(results, scores);
  }

  private static Map<Long, Integer> toScoresByHole(Collection<ScoreEntity> scoresForHole) {
    return scoresForHole.stream().collect(
        Collectors.toMap(
            score -> score.getPlayer().getExternalId(),
            score -> score.getScore()));
  }

  private static Map<Long, PlayerScore> calculateRoundScores(ScoreCardEntity scoreCard) {
    var playerScores = scoreCard.getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getPlayer().getExternalId()));

    return playerScores.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> toPlayerScore(entry.getValue(), scoreCard.getCourse())));
  }

  private static PlayerScore toPlayerScore(Collection<ScoreEntity> playerScores, CourseEntity course) {
    var result = ScoreCalculator.result(playerScores, course);
    var total = ScoreCalculator.total(playerScores);
    return new PlayerScore(playerScores.size(), result, total);
  }

}
