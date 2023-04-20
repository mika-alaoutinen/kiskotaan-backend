package mikaa.feature.score;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.events.score.ScorePayload;
import mikaa.events.score.ScoreProducer;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.model.NewScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreService {

  private final PlayerFinder playerFinder;
  private final ScoreCardFinder scoreCardFinder;
  private final ScoreProducer producer;
  private final ScoreRepository repository;

  ScoreEntity findOrThrow(long id) {
    return repository.findByIdOptional(id)
        .orElseThrow(() -> new NotFoundException("Could not find score with id " + id));
  }

  ScoreEntity addScore(long id, NewScoreDTO newScore) {
    var scoreCard = scoreCardFinder.findOrThrow(id);
    var player = playerFinder.findOrThrow(newScore.getPlayerId().longValue());
    var score = new ScoreEntity(newScore.getHole(), newScore.getScore());

    scoreCard.addScore(score);
    player.addScore(score);

    repository.persist(score);
    producer.scoreAdded(ScorePayload.from(score));

    return score;
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(ScorePayload::from)
        .ifPresent(payload -> {
          repository.deleteById(id);
          producer.scoreDeleted(payload);
        });
  }

}
