package mikaa.feature.score;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardService;
import mikaa.model.NewScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreService {

  private final PlayerFinder playerFinder;
  private final ScoreCardService scoreCardService;
  private final ScoreRepository repository;

  ScoreEntity findOrThrow(long id) {
    return repository.findByIdOptional(id)
        .orElseThrow(() -> new NotFoundException("Could not find score with id " + id));
  }

  ScoreEntity addScore(long id, NewScoreDTO newScore) {
    var scoreCard = scoreCardService.findOrThrow(id);
    var player = playerFinder.findOrThrow(newScore.getPlayerId().longValue());
    var score = new ScoreEntity(newScore.getHole(), newScore.getScore());

    scoreCard.addScore(score);
    player.addScore(score);

    repository.persist(score);
    return score;
  }

  void delete(long id) {
    repository.deleteById(id);
  }

}
