package mikaa.feature;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.smallrye.common.annotation.Blocking;
import mikaa.api.ScoreCardsApi;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreRowDTO;

@ApplicationScoped
@Blocking
class ScoreCardResource implements ScoreCardsApi {

  @Override
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    return null;
  }

  @Override
  public void deleteScoreCard(Integer id) {
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    return null;
  }

  @Override
  public List<ScoreCardDTO> getScoreCards() {
    return null;
  }

  @Override
  public ScoreRowDTO updateScores(Integer id, @Valid @NotNull ScoreRowDTO scoreRowDTO) {
    return null;
  }

}
