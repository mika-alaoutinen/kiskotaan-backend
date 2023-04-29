package mikaa.feature.scorecards;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.api.QueryScoreCardsApi;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardsResource implements QueryScoreCardsApi {

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    throw new UnsupportedOperationException("Unimplemented method 'getScoreCard'");
  }

  @Override
  public List<ScoreCardSummaryDTO> getScoreCards() {
    throw new UnsupportedOperationException("Unimplemented method 'getScoreCards'");
  }

}
