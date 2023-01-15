package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.ScoresApi;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
public class ScoreResource implements ScoresApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final ScoreService service;

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
