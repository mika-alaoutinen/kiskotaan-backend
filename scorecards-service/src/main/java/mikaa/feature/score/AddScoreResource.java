package mikaa.feature.score;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import mikaa.api.AddNewScoreApi;
import mikaa.domain.NewScore;
import mikaa.domain.Score;
import mikaa.model.NewScoreDTO;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
class AddScoreResource implements AddNewScoreApi {

  private final ScoreService service;

  @Override
  @Transactional
  public ScoreDTO addScore(Integer id, @Valid @NotNull NewScoreDTO newScoreDTO) {
    var newScore = new NewScore(newScoreDTO.getPlayerId().longValue(), newScoreDTO.getHole(), newScoreDTO.getScore());
    var score = service.addScore(id, newScore);
    return mapScore(score);
  }

  private static ScoreDTO mapScore(Score score) {
    ScoreDTO dto = new ScoreDTO();
    dto.setId(BigDecimal.valueOf(score.id()));
    dto.setPlayerId((int) score.playerId());
    dto.setHole(score.hole());
    dto.setScore(score.score());
    return dto;
  }

}
