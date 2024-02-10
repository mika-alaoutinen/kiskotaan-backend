package mikaa.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ScoreLogicTest {

  @Test
  void returns_player_results_and_scores_by_player() {
    var scoresByPlayer = ScoreLogic.scoresByPlayer(input());

    var akuScores = scoresByPlayer.getScores().get(313l);
    assertEquals(2, akuScores.get(1).getHole());
    assertEquals(3, akuScores.get(1).getScore());

    var iinesScores = scoresByPlayer.getScores().get(314l);
    assertEquals(3, iinesScores.get(2).getHole());
    assertEquals(5, iinesScores.get(2).getScore());

    assertResults(scoresByPlayer.getResults());
  }

  @Test
  void returns_player_results_and_scores_by_hole() {
    var scoresByHole = ScoreLogic.scoresByHole(input());

    var hole1Scores = scoresByHole.getScores().get(1);
    assertEquals(3, hole1Scores.get(0).getScore());
    assertEquals(313, hole1Scores.get(0).getPlayerId());
    assertEquals(4, hole1Scores.get(1).getScore());
    assertEquals(314, hole1Scores.get(1).getPlayerId());

    assertResults(scoresByHole.getResults());
  }

  private static void assertResults(Map<Long, PlayerScore> results) {
    var akuResult = results.get(313l);
    assertEquals(-1, akuResult.getResult());
    assertEquals(11, akuResult.getTotal());
    assertEquals(3, akuResult.getHolesPlayed());

    var iinesResult = results.get(314l);
    assertEquals(1, iinesResult.getResult());
    assertEquals(13, iinesResult.getTotal());
    assertEquals(3, iinesResult.getHolesPlayed());
  }

  private static ScoreCardInput input() {
    var scores = List.of(
        new ScoreInput(1, 313, 1, 3, 3),
        new ScoreInput(2, 313, 2, 4, 3),
        new ScoreInput(3, 313, 3, 5, 5),
        new ScoreInput(4, 314, 1, 3, 4),
        new ScoreInput(5, 314, 2, 4, 4),
        new ScoreInput(6, 314, 3, 5, 5));

    return new ScoreCardInput(10l, scores);
  }

}
