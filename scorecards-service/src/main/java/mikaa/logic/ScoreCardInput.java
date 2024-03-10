package mikaa.logic;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import mikaa.domain.Hole;
import mikaa.domain.Score;
import mikaa.domain.ScoreCard;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;

public record ScoreCardInput(long id, Collection<ScoreEntry> scores) {

  public static ScoreCardInput from(ScoreCard scoreCard) {
    var holePars = scoreCard.course()
        .holes()
        .stream()
        .collect(Collectors.toMap(Hole::number, Hole::par));

    var scores = scoreCard.scores()
        .stream()
        .map(s -> toScoreEntry(s, holePars))
        .toList();

    return new ScoreCardInput(scoreCard.id(), scores);
  }

  private static ScoreEntry toScoreEntry(Score score, Map<Integer, Integer> holePars) {
    int par = holePars.getOrDefault(score.hole(), 0);
    return new ScoreEntry(score.id(), score.playerId(), score.hole(), par, score.score());
  }

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
