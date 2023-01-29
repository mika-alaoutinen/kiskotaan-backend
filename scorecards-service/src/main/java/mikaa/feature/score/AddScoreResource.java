package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.AddNewScoreApi;
import mikaa.model.NewScoreDTO;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
class AddScoreResource implements AddNewScoreApi {

  private final ModelMapper mapper;
  private final ScoreService service;

  @Override
  @Transactional
  public ScoreDTO addScore(Integer id, @Valid @NotNull NewScoreDTO newScoreDTO) {
    var score = service.addScore(id, newScoreDTO);
    return mapper.map(score, ScoreDTO.class);
  }

}
