package mikaa.feature.scorecard;

import java.util.Collection;

import mikaa.domain.ScoreCard;

public interface ScoreCardFinder {

  Collection<ScoreCard> findAll();

  ScoreCard findByIdOrThrow(long id);

}
