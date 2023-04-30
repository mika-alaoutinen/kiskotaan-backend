package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.ScoreCardPayload;
import mikaa.events.scorecard.ScoreCardProducer;
import mikaa.feature.course.CourseFinder;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.model.NewScoreCardDTO;

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

  List<ScoreCardEntity> findAll() {
    return repository.listAll();
  }

  ScoreCardEntity add(NewScoreCardDTO newScoreCard) {
    var course = courseFinder.findOrThrow(newScoreCard.getCourseId().longValue());

    var players = newScoreCard.getPlayerIds()
        .stream()
        .map(BigDecimal::longValue)
        .map(playerFinder::findOrThrow)
        .collect(Collectors.toSet());

    var entity = new ScoreCardEntity(course, players);

    repository.persist(entity);
    producer.scoreCardAdded(fromEntity(entity));

    return entity;
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(ScoreCardService::fromEntity)
        .ifPresent(payload -> {
          repository.deleteById(id);
          producer.scoreCardDeleted(payload);
        });
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

  private static ScoreCardPayload fromEntity(ScoreCardEntity entity) {
    var playerIds = entity.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();

    return new ScoreCardPayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds);
  }

}
