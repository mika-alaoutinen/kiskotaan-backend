package mikaa.feature.scorecard;

import mikaa.domain.ScoreCard;

public interface ScoreCardFinder {

  ScoreCardEntity findOrThrow(long id);

  ScoreCard findByIdOrThrow(long id);

}
