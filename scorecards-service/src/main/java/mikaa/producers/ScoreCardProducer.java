package mikaa.producers;

import mikaa.feature.scorecard.ScoreCardEntity;

public interface ScoreCardProducer {

  void scoreCardAdded(ScoreCardEntity entity);

  void scoreCardDeleted(ScoreCardEntity entity);

  void scoreCardUpdated(ScoreCardEntity entity);

}
