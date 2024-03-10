package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.domain.NewScoreCard;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;

@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private final ScoreCardService service;

  @Override
  @Transactional
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    var newScoreCard = new NewScoreCard(
        newScoreCardDTO.getCourseId().longValue(),
        newScoreCardDTO.getPlayerIds().stream().map(BigDecimal::longValue).toList());

    return ScoreCardMapper.toDto(service.add(newScoreCard));
  }

  @Override
  @Transactional
  public void deleteScoreCard(Integer id) {
    service.delete(id);
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    return ScoreCardMapper.toDto(service.findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public List<ScoreCardSummaryDTO> getScoreCards() {
    return service.findAll()
        .stream()
        .map(ScoreCardMapper::toSummary)
        .toList();
  }

}
