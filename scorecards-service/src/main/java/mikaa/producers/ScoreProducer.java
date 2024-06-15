package mikaa.producers;

import mikaa.domain.Score;

public interface ScoreProducer {

  static final String SCORES_CHANNEL = "scores";

  void scoreAdded(long scoreCardId, Score score);

  void scoreDeleted(long scoreId);

}
