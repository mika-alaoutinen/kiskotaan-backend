package mikaa.feature.scorecard;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.feature.course.CourseService;
import mikaa.feature.player.PlayerService;
import mikaa.model.NewScoreCardDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService {

  private final CourseService courseService;
  private final PlayerService playerService;
  private final ScoreCardRepository repository;

  List<ScoreCardEntity> findAll() {
    return repository.listAll();
  }

  Optional<ScoreCardEntity> findOne(long id) {
    return repository.findByIdOptional(id);
  }

  ScoreCardEntity add(NewScoreCardDTO newScoreCard) {
    var course = courseService.findOrThrow(newScoreCard.getCourseId());

    var players = newScoreCard.getPlayerIds()
        .stream()
        .map(playerService::findOrThrow)
        .collect(Collectors.toSet());

    var entity = new ScoreCardEntity(course, players);
    repository.persist(entity);
    return entity;
  }

  void delete(long id) {
    repository.deleteById(id);
  }

}
