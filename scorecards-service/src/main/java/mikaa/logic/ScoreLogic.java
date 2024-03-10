package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ScoreLogic {

  static Map<Long, PlayerScore> results(ScoreCardInput input) {
    return groupScoresByPlayer(input)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calculatePlayerScores(entry.getValue())));
  }

  static Map<Integer, List<ScoreEntry>> scoresByHole(ScoreCardInput input) {
    return input.scores().stream().collect(Collectors.groupingBy(ScoreEntry::hole));
  }

  static Map<Long, List<ScoreEntry>> scoresByPlayer(ScoreCardInput input) {
    return groupScoresByPlayer(input);
  }

  private static PlayerScore calculatePlayerScores(Collection<ScoreEntry> playerScores) {
    int result = playerScores.stream().mapToInt(entry -> entry.score() - entry.par()).sum();
    int total = playerScores.stream().mapToInt(ScoreEntry::score).sum();
    return new PlayerScore(playerScores.size(), result, total);
  }

  private static Map<Long, List<ScoreEntry>> groupScoresByPlayer(ScoreCardInput input) {
    return input.scores().stream().collect(Collectors.groupingBy(ScoreEntry::playerId));
  }

}
