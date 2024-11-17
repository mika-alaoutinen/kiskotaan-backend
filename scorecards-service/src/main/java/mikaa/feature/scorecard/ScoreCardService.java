package mikaa.feature.scorecard;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.NewScoreCard;
import mikaa.domain.ScoreCard;
import mikaa.feature.course.CourseFinder;
import mikaa.feature.player.PlayerFinder;
import mikaa.producers.ScoreCardProducer;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService implements ScoreCardFinder {

  private final CourseFinder courseFinder;
  private final PlayerFinder playerFinder;
  private final ScoreCardProducer producer;
  private final ScoreCardRepository repository;

  public ScoreCardEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  @Override
  public ScoreCard findByIdOrThrow(long id) {
    return ScoreCardMapper.from(findOrThrow(id));
  }

  List<ScoreCard> findAll() {
    return repository.streamAll().map(ScoreCardMapper::from).toList();
  }

  ScoreCard add(NewScoreCard newScoreCard) {
    var course = courseFinder.findOrThrow(newScoreCard.courseId());

    var players = newScoreCard.playerIds()
        .stream()
        .map(playerFinder::findOrThrow)
        .collect(Collectors.toSet());

    var entity = new ScoreCardEntity(course, players);

    repository.persist(entity);
    producer.scoreCardAdded(ScoreCardMapper.from(entity));

    return ScoreCardMapper.from(entity);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .ifPresent(entity -> {
          repository.deleteById(id);
          producer.scoreCardDeleted(ScoreCardMapper.from(entity));
        });
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

}
