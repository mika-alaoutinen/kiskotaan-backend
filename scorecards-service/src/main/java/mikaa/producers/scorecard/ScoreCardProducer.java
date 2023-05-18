package mikaa.producers.scorecard;

import mikaa.kiskotaan.scorecards.ScoreCardPayload;

public interface ScoreCardProducer {

  void scoreCardAdded(ScoreCardPayload payload);

  void scoreCardDeleted(ScoreCardPayload payload);

}
