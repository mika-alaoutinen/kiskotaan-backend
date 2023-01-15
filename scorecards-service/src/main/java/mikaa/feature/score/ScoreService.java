package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.feature.player.PlayerService;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.model.NewScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
public class ScoreService {

  private final PlayerService playerService;
  private final ScoreRepository repository;

  public ScoreEntity addScore(NewScoreDTO newScore, ScoreCardEntity scoreCard) {
    var player = playerService.findOrThrow(newScore.getPlayerId().longValue());
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
