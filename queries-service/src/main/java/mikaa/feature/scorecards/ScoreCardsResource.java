package mikaa.feature.scorecards;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.model.ScoreCardDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardsResource implements ScoreCardsApi {

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    throw new UnsupportedOperationException("Unimplemented method 'getScoreCard'");
  }

  @Override
  public List<ScoreCardDTO> getScoreCards() {
    throw new UnsupportedOperationException("Unimplemented method 'getScoreCards'");
  }

}
