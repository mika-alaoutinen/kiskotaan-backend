package mikaa.feature.score;

import java.math.BigDecimal;

import mikaa.domain.Score;
import mikaa.model.ScoreDTO;

public interface ScoreMapper {

  static ScoreDTO dto(Score score) {
    return new ScoreDTO()
        .id(BigDecimal.valueOf(score.id()))
        .playerId((int) score.playerId())
        .hole(score.hole())
        .score(score.score());
  }

  static Score score(ScoreEntity score) {
    long playerId = score.getPlayer().getExternalId();
    return new Score(score.getId(), playerId, score.getHole(), score.getScore());
  }

}
