package mikaa.events.scorecard;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@RegisterForReflection
public record ScoreCardPayload(Long id, long courseId, List<Long> playerIds) {

  public static ScoreCardPayload from(ScoreCardEntity entity) {
    var playerIds = entity.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();

    return new ScoreCardPayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds);
  }

}
