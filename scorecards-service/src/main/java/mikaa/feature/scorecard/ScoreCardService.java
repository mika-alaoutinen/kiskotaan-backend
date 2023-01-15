package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.feature.course.CourseService;
import mikaa.feature.player.PlayerService;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.score.ScoreService;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.NewScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
public class ScoreCardService {

  private final CourseService courseService;
  private final PlayerService playerService;
  private final ScoreService scoreService;
  private final ScoreCardRepository repository;

  public ScoreCardEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  List<ScoreCardEntity> findAll() {
    return repository.listAll();
  }

  ScoreCardEntity add(NewScoreCardDTO newScoreCard) {
    var course = courseService.findOrThrow(newScoreCard.getCourseId().longValue());

    var players = newScoreCard.getPlayerIds()
        .stream()
        .map(BigDecimal::longValue)
        .map(playerService::findOrThrow)
        .collect(Collectors.toSet());

    var entity = new ScoreCardEntity(course, players);
    repository.persist(entity);
    return entity;
  }

  void delete(long id) {
    repository.deleteById(id);
  }

  ScoreEntity addScore(long scoreCardId, NewScoreDTO newScore) {
    var scoreCard = findOrThrow(scoreCardId);
    return scoreService.addScore(newScore, scoreCard);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

}
