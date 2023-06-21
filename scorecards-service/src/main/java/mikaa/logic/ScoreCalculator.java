package mikaa.logic;

import java.util.Collection;
import java.util.stream.Collectors;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.score.ScoreEntity;

class ScoreCalculator {

  private ScoreCalculator() {
    // Do not initiate
  }

  private static record ScoreEntry(int par, int score) {
  }

  static int result(Collection<ScoreEntity> playerScores, CourseEntity course) {
    var holePars = course.getHoles().stream().collect(
        Collectors.toMap(HoleEntity::getNumber, HoleEntity::getPar));

    return playerScores.stream()
        .map(s -> new ScoreEntry(holePars.getOrDefault(s.getHole(), 0), s.getScore()))
        .mapToInt(entry -> entry.score - entry.par)
        .sum();
  }

  static int total(Collection<ScoreEntity> scores) {
    return scores.stream().mapToInt(ScoreEntity::getScore).sum();
  }

}
