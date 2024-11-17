package mikaa.feature.results;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class RoundResultCalculatorTest {

  @Test
  void calculates_round_results_by_player() {
    var results = RoundResultCalculator.results(input());

    var akuResult = scoreForPlayer(313, results);
    assertEquals(-1, akuResult.result());
    assertEquals(11, akuResult.total());
    assertEquals(3, akuResult.holesPlayed());

    var iinesResult = scoreForPlayer(314, results);
    assertEquals(1, iinesResult.result());
    assertEquals(13, iinesResult.total());
    assertEquals(3, iinesResult.holesPlayed());
  }

  private static RoundScore scoreForPlayer(long playerId, List<RoundScore> roundScores) {
    return roundScores.stream()
        .filter(roundScore -> roundScore.playerId() == playerId)
        .findFirst()
        .orElseThrow();
  }

  private static ScoreCardInput input() {
    var scores = List.of(
        new ScoreEntry(1, 313, 1, 3, 3),
        new ScoreEntry(2, 313, 2, 4, 3),
        new ScoreEntry(3, 313, 3, 5, 5),
        new ScoreEntry(4, 314, 1, 3, 4),
        new ScoreEntry(5, 314, 2, 4, 4),
        new ScoreEntry(6, 314, 3, 5, 5));

    return new ScoreCardInput(10l, scores);
  }

}
