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
            entry -> calculatePlayerScores(entry.getValue(), scoreCard.holes())));
  }

  private static PlayerScore calculatePlayerScores(Collection<ScoreEntry> playerScores, Collection<HoleInput> holes) {
    int result = ScoreCalculator.result(playerScores, holes);
    int total = ScoreCalculator.total(playerScores);
    return new PlayerScore(playerScores.size(), result, total);
  }

  private static Map<Long, List<ScoreEntry>> groupScoresByPlayer(ScoreCardInput scoreCard) {
    return scoreEntries(scoreCard).collect(Collectors.groupingBy(ScoreEntry::getPlayerId));
  }

  private static Stream<ScoreEntry> scoreEntries(ScoreCardInput scoreCard) {
    return scoreCard.scores().stream().map(score -> {
      int par = scoreCard.holes()
          .stream()
          .filter(hole -> hole.number() == score.hole())
          .findFirst()
          .map(HoleInput::par)
          .orElse(0);

      return new ScoreEntry(score.id(), score.playerId(), score.hole(), par, score.score());
    });
  }

}
