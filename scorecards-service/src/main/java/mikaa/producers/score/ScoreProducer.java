package mikaa.producers.score;

import mikaa.ScorePayload;

public interface ScoreProducer {

  void scoreAdded(ScorePayload payload);

  void scoreDeleted(ScorePayload payload);

}
