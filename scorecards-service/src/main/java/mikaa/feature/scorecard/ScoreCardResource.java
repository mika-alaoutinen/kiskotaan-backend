package mikaa.feature.scorecard;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final ScoreCardService service;

  @Override
  @Transactional
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    return mapScoreCard(service.add(newScoreCardDTO));
  }

  @Override
  @Transactional
  public void deleteScoreCard(Integer id) {
    service.delete(id);
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    return mapScoreCard(service.findOrThrow(id));
  }

  @Override
  public List<ScoreCardDTO> getScoreCards() {
    return service.findAll()
        .stream()
        .map(ScoreCardResource::mapScoreCard)
        .toList();
  }

  private static ScoreCardDTO mapScoreCard(ScoreCardEntity scoreCard) {
    return MAPPER.map(scoreCard, ScoreCardDTO.class);
  }

}
