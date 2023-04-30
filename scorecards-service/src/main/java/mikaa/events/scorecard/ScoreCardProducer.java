package mikaa.events.scorecard;

import mikaa.ScoreCardPayload;

public interface ScoreCardProducer {

  void scoreCardAdded(ScoreCardPayload payload);

  void scoreCardDeleted(ScoreCardPayload payload);

}
