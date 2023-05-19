package mikaa.producers.scorecard;

import mikaa.kiskotaan.domain.ScoreCardPayload;

public interface ScoreCardProducer {

  void scoreCardAdded(ScoreCardPayload payload);

  void scoreCardDeleted(ScoreCardPayload payload);

}
