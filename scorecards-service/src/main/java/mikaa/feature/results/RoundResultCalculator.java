package mikaa.feature.results;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface RoundResultCalculator {

  static Map<Long, RoundScore> results(ScoreCardInput input) {
    var scoresByPlayer = input.scores()
        .stream()
        .collect(Collectors.groupingBy(ScoreEntry::playerId));

    return scoresByPlayer.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calculatePlayerScores(entry.getValue())));
  }

  private static RoundScore calculatePlayerScores(Collection<ScoreEntry> playerScores) {
    int result = playerScores.stream().mapToInt(entry -> entry.score() - entry.par()).sum();
    int total = playerScores.stream().mapToInt(ScoreEntry::score).sum();
    return new RoundScore(playerScores.size(), result, total);
  }

}
