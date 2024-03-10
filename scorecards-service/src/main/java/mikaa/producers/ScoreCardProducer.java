package mikaa.producers;

import mikaa.domain.ScoreCard;

public interface ScoreCardProducer {

  void scoreCardAdded(ScoreCard scoreCard);

  void scoreCardDeleted(ScoreCard scoreCard);

  void scoreCardUpdated(ScoreCard scoreCard);

}
