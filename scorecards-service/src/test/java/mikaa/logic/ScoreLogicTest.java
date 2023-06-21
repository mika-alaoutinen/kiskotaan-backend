package mikaa.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
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
  void returns_player_scores_as_map() {
    var holes = IntStream.rangeClosed(1, 3).mapToObj(i -> new HoleEntity(i, i + 2)).toList();
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

    var playerScores = ScoreLogic.calculatePlayerScores(new ScoreCardEntity(10L, course, players, scores));

    var akuScore = playerScores.get(313l);
    assertEquals(-1, akuScore.getResult());
    assertEquals(11, akuScore.getTotal());

    var iinesScore = playerScores.get(314l);
    assertEquals(1, iinesScore.getResult());
    assertEquals(13, iinesScore.getTotal());

  }

}
