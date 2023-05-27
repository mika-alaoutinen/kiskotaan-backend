package mikaa.queries.scorecard;

import mikaa.feature.scorecard.Course;
import mikaa.feature.scorecard.Hole;
import mikaa.feature.scorecard.Player;
import mikaa.feature.scorecard.Score;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.queries.dto.CourseDTO;
import mikaa.queries.dto.HoleDTO;

interface ScoreCardMapper {

  static ScoreCardDTO toDto(ScoreCardEntity entity) {
    var course = toCourse(entity.getCourse());
    var players = entity.getPlayers().stream().map(ScoreCardMapper::toPlayer).toList();

    return new ScoreCardDTO(
        entity.getExternalId(),
        course,
        players);
  }

  private static CourseDTO toCourse(Course course) {
    int coursePar = calculateCoursePar(course);
    var holes = course.getHoles().stream().map(ScoreCardMapper::toHole).toList();

    return new CourseDTO(
        course.getExternalId(),
        course.getName(),
        coursePar,
        holes);
  }

  private static HoleDTO toHole(Hole hole) {
    return new HoleDTO(
        hole.getExternalId(),
        hole.getNumber(),
        hole.getDistance(),
        hole.getPar());
  }

  private static PlayerDTO toPlayer(Player player) {
    var scores = player.getScores().stream().map(ScoreCardMapper::toScore).toList();

    return new PlayerDTO(
        player.getExternalId(),
        player.getFirstName(),
        player.getLastName(),
        scores);
  }

  private static ScoreDTO toScore(Score score) {
    return new ScoreDTO(
        score.getExternalId(),
        score.getHole(),
        score.getScore());
  }

  private static int calculateCoursePar(Course course) {
    return course.getHoles().stream().mapToInt(Hole::getPar).sum();
  }

}
