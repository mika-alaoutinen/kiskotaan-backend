package mikaa.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

class ScoreLogicTest {

  @Test
  void returns_player_results_and_scores_by_player() {
    var scoresByPlayer = ScoreLogic.calculateScoresByPlayer(scoreCard());

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
    var scoresByHole = ScoreLogic.calculateScoresByHole(scoreCard());

    var hole1Scores = scoresByHole.getScores().get(1);
    assertEquals(3, hole1Scores.get(313l));
    assertEquals(4, hole1Scores.get(314l));

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

  private static ScoreCardEntity scoreCard() {
    var holes = IntStream.rangeClosed(1, 3)
        .mapToObj(i -> new HoleEntity(i, i + 2))
        .toList();

    var course = new CourseEntity(1, holes, "Course");

    var aku = new PlayerEntity(313, "Aku", "Ankka");
    var iines = new PlayerEntity(314, "Iines", "Ankka");
    var players = Set.of(aku, iines);

    var scores = List.of(
        new ScoreEntity(1, 3, aku),
        new ScoreEntity(2, 3, aku),
        new ScoreEntity(3, 5, aku),
        new ScoreEntity(1, 4, iines),
        new ScoreEntity(2, 4, iines),
        new ScoreEntity(3, 5, iines));

    return new ScoreCardEntity(10L, course, players, scores);
  }

}
