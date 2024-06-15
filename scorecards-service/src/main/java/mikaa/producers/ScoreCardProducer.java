package mikaa.producers;

import mikaa.domain.ScoreCard;

public interface ScoreCardProducer {

  static final String SCORECARD_STATE = "scorecard-state";

  void scoreCardAdded(ScoreCard scoreCard);

  void scoreCardDeleted(ScoreCard scoreCard);

  void scoreCardUpdated(ScoreCard scoreCard);

}
