package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ScoreLogic {

  static ScoresByPlayer scoresByPlayer(ScoreCardInput scoreCard) {
    var results = calculateRoundScores(scoreCard);
    var scores = groupScoresByPlayer(scoreCard);
    return new ScoresByPlayer(results, scores);
  }

  static ScoresByHole scoresByHole(ScoreCardInput scoreCard) {
    var results = calculateRoundScores(scoreCard);
    var scores = scoreEntries(scoreCard).collect(Collectors.groupingBy(ScoreEntry::getHole));
    return new ScoresByHole(results, scores);
  }

  private static Map<Long, PlayerScore> calculateRoundScores(ScoreCardInput scoreCard) {
    return groupScoresByPlayer(scoreCard)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calculatePlayerScores(entry.getValue())));
  }

  private static PlayerScore calculatePlayerScores(Collection<ScoreEntry> playerScores) {
    int result = playerScores.stream()
        .mapToInt(scoreEntry -> scoreEntry.getScore() - scoreEntry.getPar())
        .sum();

    int total = playerScores.stream()
        .mapToInt(ScoreEntry::getScore)
        .sum();

    return new PlayerScore(playerScores.size(), result, total);
  }

  private static Map<Long, List<ScoreEntry>> groupScoresByPlayer(ScoreCardInput scoreCard) {
    return scoreEntries(scoreCard).collect(Collectors.groupingBy(ScoreEntry::getPlayerId));
  }

  private static Stream<ScoreEntry> scoreEntries(ScoreCardInput scoreCard) {
    return scoreCard.scores()
        .stream()
        .map(s -> new ScoreEntry(s.id(), s.playerId(), s.hole(), s.par(), s.score()));
  }

}
