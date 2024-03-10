package mikaa.feature.score;

import java.math.BigDecimal;

import mikaa.domain.Score;
import mikaa.model.ScoreDTO;

interface ScoreMapper {

  static ScoreDTO dto(Score score) {
    ScoreDTO dto = new ScoreDTO();
    dto.setId(BigDecimal.valueOf(score.id()));
    dto.setPlayerId((int) score.playerId());
    dto.setHole(score.hole());
    dto.setScore(score.score());
    return dto;
  }

}
