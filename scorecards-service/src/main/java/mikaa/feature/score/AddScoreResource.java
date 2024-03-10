package mikaa.feature.score;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.AddNewScoreApi;
import mikaa.domain.NewScore;
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
    return ScoreMapper.dto(score);
  }

}
