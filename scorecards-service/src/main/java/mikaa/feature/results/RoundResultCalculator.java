package mikaa.feature.results;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface RoundResultCalculator {

  static List<RoundScore> results(ScoreCardInput input) {
    var scoresByPlayer = input.scores()
        .stream()
        .collect(Collectors.groupingBy(ScoreEntry::playerId));

    return scoresByPlayer.entrySet()
        .stream()
        .map(entry -> calculatePlayerScores(entry.getKey(), entry.getValue()))
        .toList();
  }

  private static RoundScore calculatePlayerScores(long playerId, Collection<ScoreEntry> playerScores) {
    int result = playerScores.stream().mapToInt(entry -> entry.score() - entry.par()).sum();
    int total = playerScores.stream().mapToInt(ScoreEntry::score).sum();
    return new RoundScore(playerId, playerScores.size(), result, total);
  }

}
