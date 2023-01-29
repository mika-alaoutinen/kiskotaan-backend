package mikaa.feature.scorecard;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private final ModelMapper mapper;
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
        .map(this::mapScoreCard)
        .toList();
  }

  private ScoreCardDTO mapScoreCard(ScoreCardEntity scoreCard) {
    return mapper.map(scoreCard, ScoreCardDTO.class);
  }

}
