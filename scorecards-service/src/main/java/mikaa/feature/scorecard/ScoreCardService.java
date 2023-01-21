package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.feature.course.CourseService;
import mikaa.feature.player.PlayerFinder;
import mikaa.model.NewScoreCardDTO;

@ApplicationScoped
@RequiredArgsConstructor
public class ScoreCardService {

  private final CourseService courseService;
  private final PlayerFinder playerFinder;
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
        .map(playerFinder::findOrThrow)
        .collect(Collectors.toSet());

    var entity = new ScoreCardEntity(course, players);
    repository.persist(entity);
    return entity;
  }

  void delete(long id) {
    repository.deleteById(id);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

}
