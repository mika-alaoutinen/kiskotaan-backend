package mikaa.feature;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.errors.NotFoundException;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreRowDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private final ScoreCardService service;

  @Override
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    return null;
  }

  @Override
  public void deleteScoreCard(Integer id) {
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    return service.findOne(id)
        .map(ScoreCardMapper::dto)
        .orElseThrow(() -> notFound(id));
  }

  @Override
  public List<ScoreCardDTO> getScoreCards() {
    return List.of();
  }

  @Override
  public ScoreRowDTO updateScores(Integer id, @Valid @NotNull ScoreRowDTO scoreRowDTO) {
    return null;
  }

  private static NotFoundException notFound(int id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

}
