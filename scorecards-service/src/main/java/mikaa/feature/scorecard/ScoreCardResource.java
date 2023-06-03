package mikaa.feature.scorecard;

import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;

@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private final ScoreCardMapper mapper;
  private final ScoreCardService service;

  @Override
  @Transactional
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    var scoreCard = service.add(newScoreCardDTO);
    return mapper.toDto(scoreCard);
  }

  @Override
  @Transactional
  public void deleteScoreCard(Integer id) {
    service.delete(id);
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    var scoreCard = service.findOrThrow(id);
    return mapper.toDto(scoreCard);
  }

  @Override
  public List<ScoreCardSummaryDTO> getScoreCards() {
    return service.findAll()
        .stream()
        .map(mapper::toSummary)
        .toList();
  }

}
