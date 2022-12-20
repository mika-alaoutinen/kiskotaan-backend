package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import io.smallrye.common.annotation.Blocking;
import mikaa.api.ScoreCardsApi;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreRowDTO;

@ApplicationScoped
@Blocking
class ScoreCardResource implements ScoreCardsApi {

  @Override
  public Response addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    return null;
  }

  @Override
  public Response deleteScoreCard(Integer id) {
    return null;
  }

  @Override
  public Response getScoreCard(Integer id) {
    return null;
  }

  @Override
  public Response getScoreCards() {
    return null;
  }

  @Override
  public Response updateScores(Integer id, @Valid @NotNull ScoreRowDTO scoreRowDTO) {
    return null;
  }

}
