package mikaa.feature.scorecard;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.NewScoreCard;
import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.CourseFinder;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.score.ScoreMapper;
import mikaa.producers.ScoreCardProducer;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService implements ScoreCardFinder {

  private final CourseFinder courseFinder;
  private final PlayerFinder playerFinder;
  private final ScoreCardProducer producer;
  private final ScoreCardRepository repository;

  @Override
  public ScoreCardEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  List<ScoreCard> findAll() {
    return repository.streamAll().map(ScoreCardService::mapScoreCard).toList();
  }

  ScoreCard findByIdOrThrow(long id) {
    return mapScoreCard(findOrThrow(id));
  }

  ScoreCard add(NewScoreCard newScoreCard) {
    var course = courseFinder.findOrThrow(newScoreCard.courseId());

    var players = newScoreCard.playerIds()
        .stream()
        .map(playerFinder::findOrThrow)
        .collect(Collectors.toSet());

    var entity = new ScoreCardEntity(course, players);

    repository.persist(entity);
    producer.scoreCardAdded(entity);

    return mapScoreCard(entity);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .ifPresent(entity -> {
          repository.deleteById(id);
          producer.scoreCardDeleted(entity);
        });
  }

  private static Course mapCourse(CourseEntity entity) {
    var holes = entity.getHoles()
        .stream()
        .map(h -> new Hole(h.getNumber(), h.getPar()))
        .toList();

    return new Course(entity.getExternalId(), entity.getName(), holes);
  }

  private static ScoreCard mapScoreCard(ScoreCardEntity entity) {
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

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

}
