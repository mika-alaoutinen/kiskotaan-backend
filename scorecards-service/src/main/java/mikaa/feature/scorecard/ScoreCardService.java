package mikaa.feature.scorecard;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.feature.course.CourseService;
import mikaa.feature.player.PlayerEntity;
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
    List<PlayerEntity> players = List.of();

    ScoreCardEntity entity = new ScoreCardEntity(course, players);
    repository.persist(entity);
    return entity;
  }

}
