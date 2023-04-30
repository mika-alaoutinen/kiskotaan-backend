package mikaa.events.score;

import mikaa.ScorePayload;

public interface ScoreProducer {

  void scoreAdded(ScorePayload payload);

  void scoreDeleted(ScorePayload payload);

}
