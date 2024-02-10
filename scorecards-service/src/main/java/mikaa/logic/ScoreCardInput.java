package mikaa.logic;

import java.util.Collection;
import java.util.stream.Collectors;

import mikaa.feature.course.HoleEntity;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;

public record ScoreCardInput(
    long id,
    Collection<ScoreEntry> scores) {

  public static ScoreCardInput from(ScoreCardEntity entity) {
    var holePars = entity.getCourse()
        .getHoles()
        .stream()
        .collect(Collectors.toMap(HoleEntity::getNumber, HoleEntity::getPar));

    var scores = entity.getScores()
        .stream()
        .map(s -> {
          int par = holePars.getOrDefault(s.getHole(), 0);
          return new ScoreEntry(s.getId(), s.getPlayer().getExternalId(), s.getHole(), par, s.getScore());
        })
        .toList();

    return new ScoreCardInput(entity.getId(), scores);
  }

  public static ScoreCardInput from(ScoreCardPayload payload) {
    var scores = payload.getScores()
        .stream()
        .map(s -> new ScoreEntry(s.getId(), s.getPlayerId(), s.getHole(), s.getPar(), s.getScore()))
        .toList();

    return new ScoreCardInput(payload.getId(), scores);
  }

}
