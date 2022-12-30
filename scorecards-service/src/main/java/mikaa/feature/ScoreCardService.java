package mikaa.feature;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.errors.NotFoundException;
import mikaa.model.NewScoreCardDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService {

  private final ScoreCardRepository repository;
  private final CourseRepository courseRepository;

  List<ScoreCardEntity> findAll() {
    return repository.listAll();
  }

  Optional<ScoreCardEntity> findOne(long id) {
    return repository.findByIdOptional(id);
  }

  ScoreCardEntity add(NewScoreCardDTO newScoreCard) {
    var courseId = newScoreCard.getCourseId().longValue();
    var course = courseRepository.findByIdOptional(courseId)
        .orElseThrow(() -> courseNotFound(courseId));

    var playerIds = newScoreCard.getPlayersIds()
        .stream()
        .map(BigDecimal::longValue)
        .toList();

    ScoreCardEntity entity = new ScoreCardEntity(course, playerIds);
    repository.persist(entity);
    return entity;
  }

  private static NotFoundException courseNotFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

}
