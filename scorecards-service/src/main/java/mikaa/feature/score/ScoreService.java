package mikaa.feature.score;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.NewScore;
import mikaa.domain.Score;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.producers.ScoreCardProducer;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreService {

  private final PlayerFinder playerFinder;
  private final ScoreCardFinder scoreCardFinder;
  private final ScoreCardProducer producer;
  private final ScoreRepository repository;

  Score findOrThrow(long id) {
    return repository.findByIdOptional(id)
        .map(score -> mapScore(score, score.getPlayer().getExternalId()))
        .orElseThrow(() -> new NotFoundException("Could not find score with id " + id));
  }

  Score addScore(long id, NewScore newScore) {
    var scoreCard = scoreCardFinder.findOrThrow(id);
    var player = playerFinder.findOrThrow(newScore.playerId());
    var scoreEntity = new ScoreEntity(newScore.hole(), newScore.score());

    scoreCard.addScore(scoreEntity);
    player.addScore(scoreEntity);
    repository.persist(scoreEntity);
    producer.scoreCardUpdated(scoreCard);

    return mapScore(scoreEntity, player.getExternalId());
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(score -> score.getScorecard().removeScore(score))
        .ifPresent(scoreCard -> {
          repository.deleteById(id);
          producer.scoreCardUpdated(scoreCard);
        });
  }

  private static Score mapScore(ScoreEntity score, long playerId) {
    return new Score(score.getId(), playerId, score.getHole(), score.getScore());
  }

}
