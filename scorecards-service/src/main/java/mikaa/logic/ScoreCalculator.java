package mikaa.logic;

import java.util.Collection;
import java.util.stream.Collectors;

class ScoreCalculator {

  private ScoreCalculator() {
    // Do not initiate
  }

  private static record Score(int par, int score) {
  }

  static int result(Collection<ScoreEntry> playerScores, Collection<HoleInput> holes) {
    return playerScores.stream()
        .map(s -> new Score(getPar(s.getHole(), holes), s.getScore()))
        .mapToInt(entry -> entry.score - entry.par)
        .sum();
  }

  static int total(Collection<ScoreEntry> scores) {
    return scores.stream().mapToInt(ScoreEntry::getScore).sum();
  }

  private static int getPar(int holeNumber, Collection<HoleInput> holes) {
    var coursePars = holes.stream().collect(
        Collectors.toMap(HoleInput::number, HoleInput::par));

    return coursePars.getOrDefault(holeNumber, 0);
  }

}
