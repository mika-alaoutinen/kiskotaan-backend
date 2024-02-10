package mikaa.producers;

import mikaa.feature.scorecard.ScoreCardEntity;

public interface ScoreCardProducer {

  static final String INTERNAL_SCORECARD_CHANNEL = "scorecard-events";

  void scoreCardAdded(ScoreCardEntity entity);

  void scoreCardDeleted(ScoreCardEntity entity);

  void scoreCardUpdated(ScoreCardEntity entity);

}
