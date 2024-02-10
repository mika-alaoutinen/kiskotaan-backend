package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ScoreLogic {

  static ScoresByPlayer scoresByPlayer(ScoreCardInput scoreCard) {
    var results = calculateRoundScores(scoreCard);
    var scores = groupScoresByPlayer(scoreCard.getScores());
    return new ScoresByPlayer(results, scores);
  }

  static ScoresByHole scoresByHole(ScoreCardInput scoreCard) {
    var results = calculateRoundScores(scoreCard);
    var scores = scoreEntries(scoreCard.getScores()).collect(Collectors.groupingBy(ScoreEntry::getHole));
    return new ScoresByHole(results, scores);
  }

  private static Map<Long, PlayerScore> calculateRoundScores(ScoreCardInput scoreCard) {
    return groupScoresByPlayer(scoreCard.getScores())
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calculatePlayerScores(entry.getValue(), scoreCard.getHoles())));
  }

  private static PlayerScore calculatePlayerScores(Collection<ScoreEntry> playerScores, Collection<HoleInput> holes) {
    int result = ScoreCalculator.result(playerScores, holes);
    int total = ScoreCalculator.total(playerScores);
    return new PlayerScore(playerScores.size(), result, total);
  }

  private static Map<Long, List<ScoreEntry>> groupScoresByPlayer(Collection<ScoreInput> scores) {
    return scoreEntries(scores).collect(Collectors.groupingBy(ScoreEntry::getPlayerId));
  }

  private static Stream<ScoreEntry> scoreEntries(Collection<ScoreInput> scores) {
    return scores.stream().map(s -> new ScoreEntry(s.getId(), s.getPlayerId(), s.getHole(), s.getScore()));
  }

}
