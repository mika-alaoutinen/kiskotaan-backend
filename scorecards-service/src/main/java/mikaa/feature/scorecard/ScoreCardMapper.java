package mikaa.feature.scorecard;

import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.feature.course.CourseEntity;

public interface ScoreCardMapper {

  static ScoreCard from(ScoreCardEntity entity) {
    var course = mapCourse(entity.getCourse());

    var players = entity.getPlayers()
        .stream()
        .map(p -> new Player(p.getExternalId(), p.getFirstName(), p.getLastName()))
        .toList();

    var scores = entity.getScores()
        .stream()
        .map(ScoreMapper::score)
        .toList();

    return new ScoreCard(entity.getId(), course, players, scores);
  }

  private static Course mapCourse(CourseEntity entity) {
    var holes = entity.getHoles()
        .stream()
        .map(h -> new Hole(h.getNumber(), h.getPar()))
        .toList();

    return new Course(entity.getExternalId(), entity.getName(), holes);
  }

}
