package mikaa.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class ScoreLogicTest {

  @Test
  void calculates_round_results_by_player() {
    var results = ScoreLogic.results(input());

    var akuResult = results.get(313l);
    assertEquals(-1, akuResult.result());
    assertEquals(11, akuResult.total());
    assertEquals(3, akuResult.holesPlayed());

    var iinesResult = results.get(314l);
    assertEquals(1, iinesResult.result());
    assertEquals(13, iinesResult.total());
    assertEquals(3, iinesResult.holesPlayed());
  }

  @Test
  void calculates_scores_by_player() {
    var scoresByPlayer = ScoreLogic.scoresByPlayer(input());

    var akuScores = scoresByPlayer.get(313l);
    assertEquals(2, akuScores.get(1).hole());
    assertEquals(3, akuScores.get(1).score());

    var iinesScores = scoresByPlayer.get(314l);
    assertEquals(3, iinesScores.get(2).hole());
    assertEquals(5, iinesScores.get(2).score());
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
