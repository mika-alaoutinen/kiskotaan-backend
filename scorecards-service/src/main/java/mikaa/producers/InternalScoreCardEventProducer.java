package mikaa.producers;

import io.smallrye.reactive.messaging.annotations.Broadcast;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;
import mikaa.logic.ScoreCardInput;
import mikaa.logic.ScoreLogic;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@ApplicationScoped
class InternalScoreCardEventProducer implements ScoreCardProducer {

  @Inject
  private ModelMapper mapper;

  @Inject
  @Broadcast
  @Channel(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  Emitter<ScoreCardEvent> emitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    sendEvent(Action.ADD, entity);
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    sendEvent(Action.DELETE, entity);
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    sendEvent(Action.UPDATE, entity);
  }

  private void sendEvent(Action action, ScoreCardEntity entity) {
    var event = new ScoreCardEvent(action, toPayload(entity));
    emitter.send(event).toCompletableFuture().join();
  }

  private ScoreCardPayload toPayload(ScoreCardEntity entity) {
    var results = ScoreLogic.scoresByHole(ScoreCardInput.from(entity))
        .getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    var scores = entity.getScores()
        .stream()
        .map(score -> mapScore(score, entity.getCourse()))
        .toList();

    return new ScoreCardPayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        entity.getPlayerIds(),
        results,
        scores);
  }

  private ScoreEntry mapScore(ScoreEntity score, CourseEntity course) {
    var entry = mapper.map(score, ScoreEntry.class);
    int par = course.getHoles()
        .stream()
        .filter(hole -> hole.getNumber() == score.getHole())
        .findFirst()
        .map(HoleEntity::getPar)
        .orElse(0);

    entry.setPar(par);
    return entry;
  }

}
