package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.feature.player.PlayerService;
import mikaa.feature.scorecard.ScoreCardService;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreService {

  private final PlayerService playerService;
  private final ScoreCardService scoreCardService;
  private final ScoreRepository repository;

  ScoreEntity addScore(long scoreCardId, ScoreDTO newScore) {
    var scoreCard = scoreCardService.findOrThrow(scoreCardId);
    var player = playerService.findOrThrow(newScore.getPlayerId());

    ScoreEntity score = new ScoreEntity(newScore.getHole(), newScore.getScore());
    scoreCard.addScore(score);
    player.addScore(score);

    repository.persist(score);
    return score;
  }

}
