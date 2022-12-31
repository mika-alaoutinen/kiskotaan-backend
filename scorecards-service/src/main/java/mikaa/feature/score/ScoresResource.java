package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.ScoresApi;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
public class ScoresResource implements ScoresApi {

  @Override
  public ScoreDTO addScore(Integer id, @Valid @NotNull ScoreDTO scoreDTO) {
    return null;
  }

  @Override
  public void deleteScore(Integer id) {

  }

}
