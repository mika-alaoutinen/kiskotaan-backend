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
    return playerScores.stream()
        .map(s -> new ScoreEntry(getPar(s.getHole(), course), s.getScore()))
        .mapToInt(entry -> entry.score - entry.par)
        .sum();
  }

  static int total(Collection<ScoreEntity> scores) {
    return scores.stream().mapToInt(ScoreEntity::getScore).sum();
  }

  private static int getPar(int holeNumber, CourseEntity course) {
    var coursePars = course.getHoles().stream().collect(
        Collectors.toMap(HoleEntity::getNumber, HoleEntity::getPar));

    return coursePars.getOrDefault(holeNumber, 0);
  }

}
