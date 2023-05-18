package mikaa.producers.score;

import mikaa.kiskotaan.scorecards.ScorePayload;

public interface ScoreProducer {

  void scoreAdded(ScorePayload payload);

  void scoreDeleted(ScorePayload payload);

}
