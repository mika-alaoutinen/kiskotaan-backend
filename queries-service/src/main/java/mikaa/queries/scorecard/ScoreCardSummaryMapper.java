package mikaa.queries.scorecard;

import mikaa.feature.scorecard.ScoreCardEntity;

interface ScoreCardSummaryMapper {

  static ScoreCardSummaryDTO toDto(ScoreCardEntity entity) {
    return new ScoreCardSummaryDTO(0, null, null);
  }

}
