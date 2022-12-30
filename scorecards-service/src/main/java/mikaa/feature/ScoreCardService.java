package mikaa.feature;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.feature.course.CourseService;
import mikaa.model.NewScoreCardDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService {

  private final CourseService courseService;
  private final ScoreCardRepository repository;

  List<ScoreCardEntity> findAll() {
    return repository.listAll();
  }

  Optional<ScoreCardEntity> findOne(long id) {
    return repository.findByIdOptional(id);
  }

  ScoreCardEntity add(NewScoreCardDTO newScoreCard) {
    var course = courseService.findOrThrow(newScoreCard.getCourseId());

    var playerIds = newScoreCard.getPlayersIds()
        .stream()
        .map(BigDecimal::longValue)
        .toList();

    ScoreCardEntity entity = new ScoreCardEntity(course, playerIds);
    repository.persist(entity);
    return entity;
  }

}
