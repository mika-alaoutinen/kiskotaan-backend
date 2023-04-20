package mikaa.events.score;

import mikaa.feature.score.ScoreEntity;

public record ScorePayload(
    Long id,
    int hole,
    int score,
    long playerId,
    long scoreCardId) {

  public static ScorePayload from(ScoreEntity entity) {
    return new ScorePayload(
        entity.getId(),
        entity.getHole(),
        entity.getScore(),
        entity.getPlayer().getExternalId(),
        entity.getScorecard().getId());
  }
}
