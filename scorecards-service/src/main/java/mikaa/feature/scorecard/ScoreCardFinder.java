package mikaa.feature.scorecard;

import mikaa.domain.ScoreCard;

public interface ScoreCardFinder {

  ScoreCard findByIdOrThrow(long id);

}
