package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ScoreLogic {

  static ScoresByPlayer scoresByPlayer(ScoreCardInput input) {
    var results = calculateRoundScores(input);
    var scores = groupScoresByPlayer(input);
    return new ScoresByPlayer(results, scores);
  }

  static ScoresByHole scoresByHole(ScoreCardInput input) {
    var results = calculateRoundScores(input);
    var scores = input.scores()
        .stream()
        .collect(Collectors.groupingBy(ScoreEntry::hole));

    return new ScoresByHole(results, scores);
  }

  private static Map<Long, PlayerScore> calculateRoundScores(ScoreCardInput input) {
    return groupScoresByPlayer(input)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calculatePlayerScores(entry.getValue())));
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
