package mikaa.producers.score;

import mikaa.kiskotaan.domain.ScorePayload;

public interface ScoreProducer {

  void scoreAdded(ScorePayload payload);

  void scoreDeleted(ScorePayload payload);

}
