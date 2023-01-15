package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.AddNewScoreApi;
import mikaa.api.ScoresApi;
import mikaa.model.NewScoreDTO;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
public class ScoreResource implements AddNewScoreApi, ScoresApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final ScoreService service;

  @Override
  @Transactional
  public ScoreDTO addScore(Integer id, @Valid @NotNull NewScoreDTO newScoreDTO) {
    return mapScore(service.addScore(id, newScoreDTO));
  }

  @Override
  public ScoreDTO getScore(Integer id) {
    return mapScore(new ScoreEntity());
  }

  @Override
  @Transactional
  public void deleteScore(Integer id) {
    service.delete(id);
  }

  private static ScoreDTO mapScore(ScoreEntity score) {
    return MAPPER.map(score, ScoreDTO.class);
  }

}
