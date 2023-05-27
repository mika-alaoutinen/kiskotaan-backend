package mikaa.queries.scorecard;

import java.util.Collection;
import java.util.function.ToIntFunction;

import mikaa.feature.scorecard.Course;
import mikaa.feature.scorecard.Hole;
import mikaa.feature.scorecard.Player;
import mikaa.feature.scorecard.Score;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.queries.dto.CourseDTO;
import mikaa.queries.dto.CourseSummaryDTO;
import mikaa.queries.dto.HoleDTO;

interface ScoreCardMapper {

  static ScoreCardDTO toDto(ScoreCardEntity entity) {
    var course = toCourse(entity.getCourse());
    var players = entity.getPlayers()
        .stream()
        .map(ScoreCardMapper::toPlayer)
        .toList();

    return new ScoreCardDTO(
        entity.getExternalId(),
        course,
        players);
  }

  private static CourseDTO toCourse(Course course) {
    int coursePar = calculateCoursePar(course);
    var holes = course.getHoles()
        .stream()
        .map(hole -> new HoleDTO(
            hole.getExternalId(),
            hole.getNumber(),
            hole.getDistance(),
            hole.getPar()))
        .toList();

    return new CourseDTO(
        course.getExternalId(),
        course.getName(),
        coursePar,
        holes);
  }

  private static PlayerDTO toPlayer(Player player) {
    var scores = player.getScores()
        .stream()
        .map(score -> new ScoreDTO(
            score.getExternalId(),
            score.getHole(),
            score.getScore()))
        .toList();

    return new PlayerDTO(
        player.getExternalId(),
        player.getFirstName(),
        player.getLastName(),
        scores);
  }

  static ScoreCardSummaryDTO toSummary(ScoreCardEntity entity) {
    var course = toSummary(entity.getCourse());
    var players = entity.getPlayers()
        .stream()
        .map(p -> toSummary(p, course.par()))
        .toList();

    return new ScoreCardSummaryDTO(
        entity.getExternalId(),
        course,
        players);
  }

  private static CourseSummaryDTO toSummary(Course course) {
    var coursePar = calculateCoursePar(course);
    var holes = course.getHoles().size();

    return new CourseSummaryDTO(
        course.getExternalId(),
        course.getName(),
        coursePar,
        holes);
  }

  private static PlayerSummaryDTO toSummary(Player player, int coursePar) {
    var roundScore = calculateRoundScore(player, coursePar);

    return new PlayerSummaryDTO(
        player.getExternalId(),
        player.getFirstName(),
        player.getLastName(),
        roundScore - coursePar,
        roundScore);
  }

  private static int calculateCoursePar(Course course) {
    return sumItems(course.getHoles(), Hole::getPar);
  }

  private static int calculateRoundScore(Player player, int coursePar) {
    // Ignore holes that have no score. Calculate score as par instead of zero.
    return sumItems(player.getScores(), Score::getScore);
  }

  private static <T> int sumItems(Collection<T> items, ToIntFunction<? super T> mapper) {
    return items.stream().mapToInt(mapper::applyAsInt).sum();
  }

}
